package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.documentHandler.word.WDXToUnicode;
import com.ciperlabs.unicodepleco.service.storage.StorageProperties;
import com.ciperlabs.unicodepleco.service.storage.StorageService;
import com.ciperlabs.unicodepleco.service.storage.StoredFile;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;

/*
This class will handle the received stored file objects to identify the file type and convert it accordingly
 */
public class DocumentHandler {

    private final Logger logger = LoggerFactory.getLogger(DocumentHandler.class);

    private String docxConvertedLocation = "Documents/converted/docx/";         //TODO read from properties
    private StorageService storageService = null;


    public DocumentHandler(StorageService storageService) {
        this.storageService = storageService;
    }

    public StoredFile convertFile(MultipartFile multipartFile) {
        System.out.println(multipartFile.getContentType());
        StoredFile convertedDocument = new StoredFile();
        String rootConvertedFileLocation = docxConvertedLocation;
        try {

            XWPFDocument docx = new XWPFDocument(multipartFile.getInputStream());      // Convert fileinut stream to a XWPF document
            WDXToUnicode docxConverter = new WDXToUnicode(docx);                    // Docx Converter
            XWPFDocument convertedFile = docxConverter.startConversion();           // Converting the document

            System.out.println(multipartFile.getOriginalFilename());
            File directory = new File(rootConvertedFileLocation);
            logger.info("Creating Upload directory if not exist : " + rootConvertedFileLocation + " : " + directory.mkdirs());
            String localTime = LocalTime.now().toString() + " ";
            String outPutFileName = localTime + multipartFile.getOriginalFilename();
            String outputFileDriectoryAndName = rootConvertedFileLocation + outPutFileName;
            File outputFile = new File(outputFileDriectoryAndName);
            logger.info("Creating output file : " + outputFile.createNewFile());
            try {
                FileOutputStream out = new FileOutputStream(outputFile);
                convertedFile.write(out);

                convertedDocument.setPath(outputFileDriectoryAndName);
                convertedDocument.setFileName(outPutFileName);

                // Create Table entry
                return convertedDocument;

            } catch (IOException e) {
                logger.error(e.getMessage());           //TODO logging stack trace

            }
        } catch (IOException e) {
            logger.error(e.getMessage());           //TODO logging stack trace

        }

        return convertedDocument;
    }
}
