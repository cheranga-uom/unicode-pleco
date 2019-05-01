package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.documentHandler.util.FontLogAbs;
import com.ciperlabs.unicodepleco.documentHandler.util.FontState;
import com.ciperlabs.unicodepleco.model.Conversion;
import com.ciperlabs.unicodepleco.model.FileType;
import com.ciperlabs.unicodepleco.model.User;
import com.ciperlabs.unicodepleco.repository.ConversionRepository;
import com.ciperlabs.unicodepleco.repository.UserRepository;
import com.ciperlabs.unicodepleco.service.storage.StorageException;
import com.ciperlabs.unicodepleco.service.storage.StorageFileNotFoundException;
import com.ciperlabs.unicodepleco.service.storage.StorageService;
import com.ciperlabs.unicodepleco.service.storage.StoredFile;
import org.jodconverter.DocumentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@Configuration
//@ComponentScan(basePackages = {
//        "org.jodconverter",
//})
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private StorageService storageService;
    @Autowired
    private ConversionRepository conversionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Environment environment;

    @Autowired
    private DocumentConverter documentConverter;

//    @Autowired
//    public FileController(StorageService storageService, ConversionRepository conversionRepository, UserRepository userRepository,
//                          Environment environment, DocumentConverter documentConverter) {
//        this.storageService = storageService;
//        this.conversionRepository = conversionRepository;
//        this.userRepository = userRepository;
//        this.environment = environment;
//        this.documentConverter = documentConverter;
//    }


    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity serveFile(@PathParam("conversionId") int conversionId,Principal principal) {
                                                                    //TODO fix this concurrent downloads might effect
        System.out.println("ConversionId : "+conversionId);
        if (!conversionRepository.findById(conversionId).isPresent()) {

            Map<String, String> response = new LinkedHashMap<>();
            response.put("status","notAvailable");
            return ResponseEntity.badRequest().body(response);
        }
        Conversion conversion = conversionRepository.findById(conversionId).get();

        String filePath = conversion.getOutputFilePath();
        logger.debug("Download FileName : "+ filePath );

        if (principal != null) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            Map<String, String> details = new LinkedHashMap<>();
            details = (Map<String, String>) authentication.getDetails();
            logger.info("details = " + details);  // id, email, name, link etc.

            System.out.println(conversion.getConversionId() + "conversion");

            if (conversion.getUser() == null){

                Long userId = Long.valueOf(details.get("id"));
                User user = userRepository.getOne(userId);
                        conversion.setUser(user);
                        conversionRepository.save(conversion);
                Resource resource = storageService.loadAsResource(filePath);
                logger.debug("Attaching file to download user unknownv: "+conversion.getInputFileName());
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);

            }
            else if (conversion.getUser().getId().equals(Long.valueOf(details.get("id")))){

                Resource resource = storageService.loadAsResource(conversion.getOutputFilePath());
                logger.debug("Attaching file to download : "+ conversion.getInputFileName());
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);

            }
            else {
                Map<String, String> response = new LinkedHashMap<>();
                response.put("status","invalidUser");
                return ResponseEntity.badRequest().body(response);
            }

        }
        else {
            Map<String, String> response = new LinkedHashMap<>();
            response.put("status","notLoggedIn");
            return ResponseEntity.badRequest().body(response);
        }


    }

    @RequestMapping("/upload")
    @ResponseBody
    public Map handleFileUpload(@RequestParam("file") MultipartFile maltipartFile,
                                    @RequestParam("inputfiletype") String inputFileType, Principal principal ) throws StorageException {

        /*
            Will return fontLog , conversionId , status , fileType (converted File)
            Split fontLog with | and then ; to get the font status for a font
         */

        logger.info("Recieved File Type : "+ inputFileType);

        Map<String, String> map = new LinkedHashMap<>();
        Conversion conversion = new Conversion();

        boolean conversionSuccess = handleConversion(maltipartFile,inputFileType,map,conversion);

        if (conversionSuccess){
            if (principal != null) {
                OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
                Authentication authentication = oAuth2Authentication.getUserAuthentication();
                Map<String, String> details = new LinkedHashMap<>();
                details = (Map<String, String>) authentication.getDetails();
                logger.info("details = " + details);  // id, email, name, link etc.

                Long userId = Long.valueOf(details.get("id")+"");
                User user = userRepository.getOne(userId);
                conversion.setUser(user);
                conversionRepository.save(conversion);

                map.put("status","success");
            }

            else {
                map.put("status","notLoggedIn");

            }
        }

        return map;
    }



    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageException(StorageException exc) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "history";
    }

    private String arrayListToString(ArrayList<FontLogAbs> fontLogAbs) {

        String[] knownFonts = {"Iskoola Pota", "Times New Roman", "Calibri", "Liberation Serif", "Latha"};

        String fontLogS = "";

        for (FontLogAbs fontLog : fontLogAbs) {
            logger.info("Font : " + fontLog.getFont());
            fontLogS += fontLog.getFont() + ";";

            boolean knwonFont = Arrays.stream(knownFonts).anyMatch(fontLog.getFont()::equals);

            if(knwonFont){
                fontLogS += "No Unicode Conversion Applied" + "|";

            }
            else {
                if (fontLog.getStatus() == FontState.NO_UNICODE_SUPPORT_AVAILABLE_YET) {
                    fontLogS += "No Unicode Support Available yet" + "|";
                } else if (fontLog.getStatus() == FontState.SUPPORTED) {
                    fontLogS += "Supported" + "|";
                } else {
                    fontLogS += fontLog.getStatus() + "|";

                }
            }

        }
        return fontLogS;
    }

    private boolean handleConversion(MultipartFile maltipartFile, String inputFileType,Map<String, String> map, Conversion conversion){

        StoredFile uploadedDocument = new StoredFile();

        ArrayList<FontLogAbs> fontLogAbs = new ArrayList<>();
        DocumentHandler documentHandler = new DocumentHandler(storageService, environment, documentConverter, fontLogAbs);
        StoredFile convertedFile = documentHandler.convertFile(maltipartFile,inputFileType);

        if (convertedFile != null) {


            if(convertedFile.getFileType() == FileType.DOCX){
                map.put("fileType",FileType.DOCX+"");
                conversion.setInputFileType(FileType.DOCX);
                uploadedDocument = storageService.store(maltipartFile, "uploaded/docx/");

            }
            else if(convertedFile.getFileType() == FileType.EXCEL){
                map.put("fileType",FileType.EXCEL+"");
                conversion.setInputFileType(FileType.EXCEL);
                uploadedDocument = storageService.store(maltipartFile, "uploaded/excel/");

            }
            else if(convertedFile.getFileType() == FileType.PDF){
                map.put("fileType",FileType.DOCX+"");
                conversion.setInputFileType(FileType.PDF);
                uploadedDocument = storageService.store(maltipartFile, "uploaded/pdf/");

            }
            else if(convertedFile.getFileType() == FileType.DOC){
                map.put("fileType",FileType.DOCX+"");
                conversion.setInputFileType(FileType.DOC);
                uploadedDocument = storageService.store(maltipartFile, "uploaded/doc/");

            }

            conversion.setInputFileName(uploadedDocument.getFileName());
            conversion.setInputFilePath(uploadedDocument.getPath());
            conversion.setInputFileType(uploadedDocument.getFileType());
            conversion.setOutputFileName(convertedFile.getFileName());
            conversion.setOutputFilePath(convertedFile.getPath());
            conversionRepository.save(conversion);
            map.put("conversionId",conversion.getConversionId()+"");
            map.put("filename","Unicode - " + uploadedDocument.getFileName());
            logger.info(map.get("filename"));
            map.put("fontLog", arrayListToString(fontLogAbs));

            return true;

        } else {
            map.put("status","Error Converting File");
            return false;
        }
    }

    @RequestMapping("/api/upload")
    @ResponseBody
    public Map handleApiUpload(@RequestParam("file") MultipartFile maltipartFile,
                               @RequestParam("inputfiletype") String inputFileType, String apiKey ) throws StorageException{

        /*
            Will return fontLog , conversionId , status , fileType (converted File)
            Split fontLog with | and then ; to get the font status for a font
         */

        logger.info("Recieved File Type : "+ inputFileType);

        Map<String, String> map = new LinkedHashMap<>();
        Conversion conversion = new Conversion();

        boolean conversionSuccess = handleConversion(maltipartFile,inputFileType,map,conversion);

//        if (conversionSuccess){
//            if (apiKey != null) {
//                OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
//                Authentication authentication = oAuth2Authentication.getUserAuthentication();
//                Map<String, String> details = new LinkedHashMap<>();
//                details = (Map<String, String>) authentication.getDetails();
//                logger.info("details = " + details);  // id, email, name, link etc.
//
//                Long userId = Long.valueOf(details.get("id")+"");
//                User user = userRepository.getOne(userId);
//                conversion.setUser(user);
//                conversionRepository.save(conversion);
//
//                map.put("status","success");
//            }
//
//            else {
//                map.put("status","notLoggedIn");
//
//            }
//        }

        return map;

    }

    @RequestMapping("/api/test")
    @ResponseBody
    public String testAPI(@RequestParam("key") String key, Principal principal){

        logger.info("principal " + principal.getName());
        return key+" success";
    }

}