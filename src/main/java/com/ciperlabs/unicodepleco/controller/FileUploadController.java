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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;


@RestController
public class FileUploadController {

    private final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final StorageService storageService;
    @Autowired
    private ConversionRepository conversionRepository;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/test")
    @ResponseBody
    public String uploadForm(Model model) throws IOException {

        return "asdfas";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile maltipartFile,
                                   RedirectAttributes redirectAttributes) throws StorageException {

        DocumentHandler documentHandler = new DocumentHandler(storageService);
        StoredFile convertedFile = documentHandler.convertFile(maltipartFile);
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

            conversion.setUserId("1");
            conversionRepository.save(conversion);

            return conversion.getConversionId().toString();


        } else {
            return "error";
        }


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