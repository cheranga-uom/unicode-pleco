package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.documentHandler.Excel.EXLToUnicode;
import com.ciperlabs.unicodepleco.documentHandler.word.WDXToUnicode;
import com.ciperlabs.unicodepleco.model.FileType;
import com.ciperlabs.unicodepleco.service.storage.StorageService;
import com.ciperlabs.unicodepleco.service.storage.StoredFile;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private String rootDocumentDirectory = "Documents/";
    private String docxConvertedLocation = "converted/docx/";         //TODO read from properties
    private String excelConvertedLocation = "converted/excel";

    private String docxLocation = rootDocumentDirectory + docxConvertedLocation;
    private String excelLocation = rootDocumentDirectory + excelConvertedLocation;
    private StorageService storageService;


    public DocumentHandler(StorageService storageService) {
        this.storageService = storageService;
    }

    public StoredFile convertFile(MultipartFile multipartFile) {


        StoredFile convertedDocument;

        try {
            logger.info("Trying to Convert as a docx  file");
            convertedDocument = tryDocx(multipartFile);
        } catch (POIXMLException poiXMLException) {
            logger.info("DOCX Conversion attempt failed. Trying as an excel File");
            convertedDocument = tryExcel(multipartFile);
        }

        return convertedDocument;
    }

    private StoredFile tryDocx(MultipartFile multipartFile) {

        StoredFile convertedDocument = new StoredFile();
        try {

            XWPFDocument docx = new XWPFDocument(multipartFile.getInputStream());       // Convert fileinut stream to a XWPF document
            WDXToUnicode docxConverter = new WDXToUnicode(docx);                        // Docx Converter
            POIXMLDocument convertedFile = docxConverter.startConversion();             // Converting the document

            System.out.println(multipartFile.getOriginalFilename());
            convertedDocument = saveFile(docxLocation, multipartFile, convertedFile);
            convertedDocument.setFileType(FileType.DOCX);
        } catch (IOException e) {
            logger.error(e.getMessage());           //TODO logging stack trace

        }
        return convertedDocument;
    }

    private StoredFile tryExcel(MultipartFile multipartFile) {

        StoredFile convertedDocument = new StoredFile();
        try {

            XSSFWorkbook excel = new XSSFWorkbook(multipartFile.getInputStream());      // Convert fileinut stream to a XSSFWorkbook document
            EXLToUnicode excelConverter = new EXLToUnicode(excel);                       // Excel Converter
            POIXMLDocument convertedFile = excelConverter.startConversion();             // Converting the document

            convertedDocument = saveFile(excelLocation, multipartFile, convertedFile);
            convertedDocument.setFileType(FileType.EXCEL);

        } catch (IOException e) {

            logger.error(e.getMessage());           //TODO logging stack trace

        }
        return convertedDocument;
    }

    public StoredFile saveFile(String rootConvertedFileLocation, MultipartFile multipartFile, POIXMLDocument convertedFile) {

        StoredFile convertedDocument = new StoredFile();
        try {
            File directory = new File(rootConvertedFileLocation);
            logger.info("Creating Uplo  ad directory if not exist : " + rootConvertedFileLocation + " : " + directory.mkdirs());
            String localTime = LocalTime.now().toString() + " ";
            String outPutFileName = localTime + multipartFile.getOriginalFilename();
            String outputFileDriectoryAndName = rootConvertedFileLocation + outPutFileName;
            File outputFile = new File(outputFileDriectoryAndName);
            logger.info("Creating output file : " + outputFile.createNewFile());
            FileOutputStream out = new FileOutputStream(outputFile);

            convertedFile.write(out);

            convertedDocument.setPath(docxConvertedLocation + outPutFileName);
            convertedDocument.setFileName(outPutFileName);
            convertedDocument.setFileType(FileType.DOCX);

        } catch (IOException e) {
            logger.error(e.getMessage());           //TODO logging stack trace

        }
        return convertedDocument;
    }
}
