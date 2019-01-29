package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.model.Conversion;
import com.ciperlabs.unicodepleco.model.FileType;
import com.ciperlabs.unicodepleco.model.User;
import com.ciperlabs.unicodepleco.repository.ConversionRepository;
import com.ciperlabs.unicodepleco.repository.UserRepository;
import com.ciperlabs.unicodepleco.service.storage.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final StorageService storageService;

    private ConversionRepository conversionRepository;

    private UserRepository userRepository;

    @Autowired
    public FileController(StorageService storageService, ConversionRepository conversionRepository, UserRepository userRepository) {
        this.storageService = storageService;
        this.conversionRepository = conversionRepository;
        this.userRepository = userRepository;
    }


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
                                   RedirectAttributes redirectAttributes, Principal principal) throws StorageException {

        StoredFile uploadedDocument = new StoredFile();

        DocumentHandler documentHandler = new DocumentHandler(storageService);
        StoredFile convertedFile = documentHandler.convertFile(maltipartFile);
        Map<String, String> map = new LinkedHashMap<>();

        if (convertedFile != null) {

            Conversion conversion = new Conversion();

            if(convertedFile.getFileType() == FileType.DOCX){
                map.put("fileType",FileType.DOCX+"");
                conversion.setInputFileType(FileType.DOCX+"");
                uploadedDocument = storageService.store(maltipartFile, "uploaded/docx/");

            }
            else if(convertedFile.getFileType() == FileType.EXCEL){
                map.put("fileType",FileType.EXCEL+"");
                conversion.setInputFileType(FileType.EXCEL+"");
                uploadedDocument = storageService.store(maltipartFile, "uploaded/excel/");

            }

            conversion.setInputFileName(uploadedDocument.getFileName());
            conversion.setInputFilePath(uploadedDocument.getPath());
            conversion.setInputFileType(uploadedDocument.getFileType()+"");
            conversion.setOutputFileName(convertedFile.getFileName());
            conversion.setOutputFilePath(convertedFile.getPath());
            conversionRepository.save(conversion);
            map.put("conversionId",conversion.getConversionId()+"");
            map.put("filename","Unicode - " + uploadedDocument.getFileName());


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

        } else {
            map.put("status","Error Converting File");
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

}