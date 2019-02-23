package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.documentHandler.Excel.EXLToUnicode;
import com.ciperlabs.unicodepleco.documentHandler.word.WDXToUnicode;
import com.ciperlabs.unicodepleco.model.FileType;
import com.ciperlabs.unicodepleco.service.storage.StorageService;
import com.ciperlabs.unicodepleco.service.storage.StoredFile;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLException;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/*
This class will handle the received stored file objects to identify the file type and convert it accordingly
 */
public class DocumentHandler {

    private final Logger logger = LoggerFactory.getLogger(DocumentHandler.class);
    private String rootDocumentDirectory = "Documents/";
    private String docxConvertedLocation = "converted/docx/";         //TODO read from properties
    private String excelConvertedLocation = "converted/excel/";
    private String pdfToWordPdfLocation = "pdfToWord/pdf/";
    private String pdfToWordDocxLocation = "pdfToWord/docx/";


    private String docxLocation = rootDocumentDirectory + docxConvertedLocation;
    private String excelLocation = rootDocumentDirectory + excelConvertedLocation;
    private StorageService storageService;
    private Environment environment;


    @Value("${pdftoword.API}")
    private String pdfToWordAPI;// = "localhost:5000/api/converter";


    public DocumentHandler(StorageService storageService, Environment environment) {
        this.storageService = storageService;
        this.environment = environment;
        pdfToWordAPI = environment.getProperty("pdftoword.API");
    }

    public StoredFile convertFile(MultipartFile multipartFile, String fileType) {


        StoredFile convertedDocument;

        if(fileType.equals("officeXML")){
            try {
                logger.info("Trying to Convert as a docx  file");
                convertedDocument = tryDocx(multipartFile);
            } catch (POIXMLException poiXMLException) {
                logger.info("DOCX Conversion attempt failed. Trying as an excel File");
                convertedDocument = tryExcel(multipartFile);
            }
        }
        else if(fileType.equals("PDF")){

            logger.info("Trying ti convert as a PDF file");
            convertedDocument = tryPDF(multipartFile);

        }
        else convertedDocument = null;



        return convertedDocument;
    }

    private StoredFile tryDocx(MultipartFile multipartFile) {

        StoredFile convertedDocument = new StoredFile();
        try {

            XWPFDocument docx = new XWPFDocument(multipartFile.getInputStream());       // Convert fileinut stream to a XWPF document
            WDXToUnicode docxConverter = new WDXToUnicode(docx);                        // Docx Converter
            POIXMLDocument convertedFile = docxConverter.startConversion();             // Converting the document

            System.out.println(multipartFile.getOriginalFilename());
            convertedDocument = saveFile(docxConvertedLocation, multipartFile, convertedFile);
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

            convertedDocument = saveFile(excelConvertedLocation, multipartFile, convertedFile);
            convertedDocument.setFileType(FileType.EXCEL);

        } catch (IOException e) {

            logger.error(e.getMessage());           //TODO logging stack trace

        }
        return convertedDocument;
    }

    private StoredFile tryPDF(MultipartFile multipartFile){
        StoredFile convertedDocument = null;


//        URI uri = null;
//        try {
//            uri = new URI("http","locahost:5000","/api/converter",null);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
        String uri = "http://localhost:5000/api/converter";
        logger.info("Sending PDF file to convert  : "+uri);
        RestTemplate restTemplate = new RestTemplate();
        StoredFile storedPDF = storageService.store(multipartFile, pdfToWordPdfLocation);
        logger.info("Converting PDF to word.. : "+ storedPDF.getPath());

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("pdfFilePath", storedPDF.getPath());

        String docxFilePath = pdfToWordDocxLocation+multipartFile.getOriginalFilename()+".docx";

        logger.info("Converting PDF docx filePath .. : " + docxFilePath);
        map.add("docxFilePath", docxFilePath);

        //TODO form data
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity( uri, request , String.class );
        logger.info(response.toString());

        try {
            File docxFile = storageService.loadAsResource(docxFilePath).getFile();
            FileInputStream docxInputStream = new FileInputStream(docxFile);
            MultipartFile multipartFile1 = new MockMultipartFile(docxFile.getName(),
                    docxFile.getName(), "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    IOUtils.toByteArray(docxInputStream));

            convertedDocument = tryDocx((multipartFile1));
            convertedDocument.setFileType(FileType.PDF);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertedDocument;
    }


    public StoredFile saveFile(String convertedFileLocation, MultipartFile multipartFile, POIXMLDocument convertedFile) {

        StoredFile convertedDocument = new StoredFile();
        String rootConvertedFileLocation = rootDocumentDirectory+convertedFileLocation;
        try {
            File directory = new File(convertedFileLocation);
            logger.info("Creating Uplo  ad directory if not exist : " + convertedFileLocation + " : " + directory.mkdirs());
            String localTime = LocalDateTime.now().toString() + " ";
            String outPutFileName = localTime + multipartFile.getOriginalFilename();
            String outputFileDriectoryAndName = rootConvertedFileLocation + outPutFileName;
            File outputFile = new File(outputFileDriectoryAndName);
            logger.info("Creating output file : " + outputFile.createNewFile());
            FileOutputStream out = new FileOutputStream(outputFile);

            convertedFile.write(out);

            convertedDocument.setPath(convertedFileLocation + outPutFileName);
            convertedDocument.setFileName(outPutFileName);

        } catch (IOException e) {
            logger.error(e.getMessage());           //TODO logging stack trace

        }
        return convertedDocument;
    }
}
