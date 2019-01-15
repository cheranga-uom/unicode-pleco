package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.UnicodePlecoApplication;
import com.ciperlabs.unicodepleco.documentHandler.word.WDXToUnicode;
import com.ciperlabs.unicodepleco.model.Conversion;
import com.ciperlabs.unicodepleco.repository.ConversionRepository;
import com.ciperlabs.unicodepleco.service.storage.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class FileUploadController {

    private final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final StorageService storageService;
    @Autowired
    private ConversionRepository conversionRepository;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }


    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathParam("convertionid") int convertionid,Principal principal) {
                                                                    //TODO fix this concurrent downloads might effect
        System.out.println(convertionid);
        Conversion conversion = conversionRepository.findById(convertionid).get();

        String filePath = conversion.getOutputFilePath();
        logger.info("Download FileName : "+ filePath );

        if (principal != null) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            Authentication authentication = oAuth2Authentication.getUserAuthentication();
            Map<String, String> details = new LinkedHashMap<>();
            details = (Map<String, String>) authentication.getDetails();
            logger.info("details = " + details);  // id, email, name, link etc.

            System.out.println(conversion.getConversionId() + "conversion");

            if (conversion.getUserId() == null || conversion.getUserId().equals("")){
                        conversion.setUserId(details.get("id"));
                        conversionRepository.save(conversion);
                Resource resource = storageService.loadAsResource(conversion.getOutputFilePath());
                System.out.println("Attaching file to download user unknown");
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);

            }
            else if (conversion.getUserId().equals(details.get("id"))){
//                try {
//                    Path file = new File(conversion.getOutputFilePath()).toPath();
//                    Resource resource = new UrlResource(file.toUri());
//                    logger.info(" file path : "+file.toUri());
//                    if (resource.exists() || resource.isReadable()) {
//
//                        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                                "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
//
//                    } else {
//                        throw new StorageFileNotFoundException(
//                                "Could not read file: " + filename);
////                return ResponseEntity.notFound().build();
//
//                    }
//                } catch (MalformedURLException e) {
//                    throw new StorageFileNotFoundException("Could not read file: " + filename, e);
//                }
                Resource resource = storageService.loadAsResource(conversion.getOutputFilePath());
                System.out.println("Attaching file to download");
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);

            }
            else {
                Map<String, String> response = new LinkedHashMap<>();
                response.put("status","invalidUser");
                return (ResponseEntity<Resource>) ResponseEntity.badRequest();
            }

        }
        else {
            Map<String, String> response = new LinkedHashMap<>();
            response.put("status","notLoggedIn");
            return (ResponseEntity<Resource>) ResponseEntity.badRequest();
        }


    }

    @RequestMapping("/upload")
    @ResponseBody
    public Map handleFileUpload(@RequestParam("file") MultipartFile maltipartFile,
                                   RedirectAttributes redirectAttributes, Principal principal) throws StorageException {

        DocumentHandler documentHandler = new DocumentHandler(storageService);
        StoredFile convertedFile = documentHandler.convertFile(maltipartFile);
        Map<String, String> map = new LinkedHashMap<>();

        if (convertedFile != null) {

            StoredFile uploadedDocument = storageService.store(maltipartFile, "uploaded/docx/");
            Conversion conversion = new Conversion();

            conversion.setInputFileName(uploadedDocument.getFileName());
            conversion.setInputFilePath(uploadedDocument.getPath());
            conversion.setInputFileType(maltipartFile.getContentType());
            conversion.setOutputFileName(convertedFile.getFileName());
            conversion.setOutputFilePath(convertedFile.getPath());
            conversion.setInputFileType("docx");
            conversionRepository.save(conversion);
            map.put("conversionId",conversion.getConversionId()+"");
            map.put("filename","Unicode - " + uploadedDocument.getFileName());


            if (principal != null) {
                OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
                Authentication authentication = oAuth2Authentication.getUserAuthentication();
                Map<String, String> details = new LinkedHashMap<>();
                details = (Map<String, String>) authentication.getDetails();
                logger.info("details = " + details);  // id, email, name, link etc.

                conversion.setUserId(details.get("id"));
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
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "history";
    }

}