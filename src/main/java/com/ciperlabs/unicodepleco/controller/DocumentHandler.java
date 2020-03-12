package com.ciperlabs.unicodepleco.controller;

import com.ciperlabs.unicodepleco.documentHandler.Excel.EXLToUnicode;
import com.ciperlabs.unicodepleco.documentHandler.util.FontLogAbs;
import com.ciperlabs.unicodepleco.documentHandler.word.HWPFtoXWPF;
import com.ciperlabs.unicodepleco.documentHandler.word.WDXToUnicode;
import com.ciperlabs.unicodepleco.model.FileType;
import com.ciperlabs.unicodepleco.service.storage.StorageProperties;
import com.ciperlabs.unicodepleco.service.storage.StorageService;
import com.ciperlabs.unicodepleco.service.storage.StoredFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLException;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jodconverter.DocumentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;

/*
This class will handle the received stored file objects to identify the file type and convert it accordingly
 */
public class DocumentHandler {

//    @Autowired
    private StorageProperties storageProperties;// = new StorageProperties();

    private final Logger logger = LoggerFactory.getLogger(DocumentHandler.class);
    private String rootDocumentDirectory;// = storageProperties.getRootDocumentDirectory();
    private String docxConvertedLocation;// = storageProperties.getConvertedDocx();    //"converted/docx/";
    private String excelConvertedLocation;// = storageProperties.getConvertedExcel();  // "converted/excel/";
    private String pdfToWordPdfLocation;// = storageProperties.getPdfToWordPDF();      //"pdfToWord/pdf/";
    private String pdfToWordDocxLocation;// = storageProperties.getPdfToWordDocx();    //"pdfToWord/docx/";

    private String docxLocation;// = rootDocumentDirectory + docxConvertedLocation;
    private String excelLocation;// = rootDocumentDirectory + excelConvertedLocation;

    private StorageService storageService;
    private Environment environment;
    private DocumentConverter documentConverter;
    private ArrayList<FontLogAbs> fontLogAbs;


    @Value("${pdftoword.API}")
    private String pdfToWordAPI;// = "localhost:5000/api/converter";


    public DocumentHandler(StorageService storageService, Environment environment, DocumentConverter documentConverter, ArrayList<FontLogAbs> fontLogAbs, StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
        this.storageService = storageService;
        this.environment = environment;
        this.documentConverter = documentConverter;
        this.pdfToWordAPI = environment.getProperty("pdftoword.API");
        this.fontLogAbs = fontLogAbs;

        rootDocumentDirectory = storageProperties.getRootDocumentDirectory();
        docxConvertedLocation = storageProperties.getConvertedDocx();    //"converted/docx/";
        excelConvertedLocation = storageProperties.getConvertedExcel();  // "converted/excel/";
        pdfToWordPdfLocation = storageProperties.getPdfToWordPDF();      //"pdfToWord/pdf/";
        pdfToWordDocxLocation = storageProperties.getPdfToWordDocx();    //"pdfToWord/docx/";

        docxLocation = rootDocumentDirectory + docxConvertedLocation;
        excelLocation = rootDocumentDirectory + excelConvertedLocation;

    }

    public StoredFile convertFile(MultipartFile multipartFile, String fileType) {


        StoredFile convertedDocument;

        if (fileType.equals("officeXML")) {
            try {
                logger.info("Trying to Convert as a docx  file");
                convertedDocument = tryDocx(multipartFile, fontLogAbs);
            } catch (POIXMLException poiXMLException) {
                logger.info("DOCX Conversion attempt failed. Trying as an excel File");
                convertedDocument = tryExcel(multipartFile, fontLogAbs);
            }
        } else if (fileType.equals("PDF")) {

            logger.info("Trying ti convert as a PDF file");
            convertedDocument = tryPDF(multipartFile, fontLogAbs);

        } else if(fileType.equals("officeDOC")){
            logger.info("Trying convert as a doc file");
            convertedDocument = tryDoc(multipartFile, fontLogAbs);
        }

        else convertedDocument = null;


        return convertedDocument;
    }

    private StoredFile tryDocx(MultipartFile multipartFile, ArrayList<FontLogAbs> fontLogAbs) {

        StoredFile convertedDocument = null;
        try {

            XWPFDocument docx = new XWPFDocument(multipartFile.getInputStream());       // Convert fileinut stream to a XWPF document
            WDXToUnicode docxConverter = new WDXToUnicode(docx, fontLogAbs);                        // Docx Converter
            POIXMLDocument convertedFile = docxConverter.startConversion();             // Converting the document

            System.out.println(multipartFile.getOriginalFilename());
            convertedDocument = saveFile(docxConvertedLocation, multipartFile.getOriginalFilename(), convertedFile);
            convertedDocument.setFileType(FileType.DOCX);

        } catch (IOException e) {
            logger.error(e.getMessage());           //TODO logging stack trace
        }
        return convertedDocument;
    }

    private StoredFile tryExcel(MultipartFile multipartFile, ArrayList<FontLogAbs> fontLogAbs) {

        StoredFile convertedDocument = null;
        try {

            XSSFWorkbook excel = new XSSFWorkbook(multipartFile.getInputStream());      // Convert fileinut stream to a XSSFWorkbook document
            EXLToUnicode excelConverter = new EXLToUnicode(excel, fontLogAbs);                       // Excel Converter
            POIXMLDocument convertedFile = excelConverter.startConversion();             // Converting the document

            String excelFileName = FilenameUtils.removeExtension(multipartFile.getOriginalFilename()) + ".xlsx";

            convertedDocument = saveFile(excelConvertedLocation, excelFileName, convertedFile);
            convertedDocument.setFileType(FileType.EXCEL);

        } catch (IOException e) {

            logger.error(e.getMessage());           //TODO logging stack trace

        }
        return convertedDocument;
    }

    private StoredFile tryPDF(MultipartFile multipartFile, ArrayList<FontLogAbs> fontLogAbs) {

        StoredFile convertedDocument = null;


        String uri = pdfToWordAPI;
        logger.info("Sending PDF file to convert  : " + uri);
        RestTemplate restTemplate = new RestTemplate();
        StoredFile storedPDF = storageService.store(multipartFile, pdfToWordPdfLocation);

        String absoluteDirectoryPath = storageService.load("").toAbsolutePath().toString();
        String absolutePDFPath = absoluteDirectoryPath + "/" + storedPDF.getPath();
        logger.info("Abs Path" + absoluteDirectoryPath);
        logger.info("Abs PDF File path PDF to word.. : " + absolutePDFPath);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("pdfFilePath", absolutePDFPath);

        String docxLoadingFilePath = pdfToWordDocxLocation + storedPDF.getFileName() + ".docx";
        String absoluteDocxPath = absoluteDirectoryPath + "/" + docxLoadingFilePath;

        logger.info("pdf to docx Absolute saving location : " + absoluteDocxPath);

        map.add("docxFilePath", absoluteDocxPath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.setExpires(10);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        logger.info(response.toString());

        try {
            logger.info("Trying to load Converted PDF docx filePath .. : " + docxLoadingFilePath);

            File docxFile = storageService.loadAsResource(docxLoadingFilePath).getFile();
            FileItem fileItem = new DiskFileItem(docxFile.getName(), Files.probeContentType(docxFile.toPath()), false, docxFile.getName(), (int) docxFile.length(), docxFile.getParentFile());

            try {
                InputStream input = new FileInputStream(docxFile);
                OutputStream os = fileItem.getOutputStream();
                IOUtils.copy(input, os);
                // Or faster..
                // IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
            } catch (IOException ex) {
                logger.error(ex.toString());
            }

            MultipartFile multipartFile1 = new CommonsMultipartFile(fileItem);

            convertedDocument = tryDocx(multipartFile1, fontLogAbs);
            convertedDocument.setFileType(FileType.PDF);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertedDocument;
    }

    private StoredFile tryDoc(MultipartFile multipartFile, ArrayList<FontLogAbs> fontLogAbs) {
        StoredFile convertedDocument = null;
        try {

            HWPFtoXWPF hwpFtoXWPF = new HWPFtoXWPF(documentConverter);
            InputStream docInputStream = hwpFtoXWPF.convertToDo(multipartFile);         // Convert doc file to docx
            XWPFDocument docx = new XWPFDocument(docInputStream);                       // Convert fileinut stream to a XWPF document
            WDXToUnicode docxConverter = new WDXToUnicode(docx, fontLogAbs);                        // Docx Converter
            POIXMLDocument convertedFile = docxConverter.startConversion();             // Converting the document

            System.out.println(multipartFile.getOriginalFilename());
            String docxFileName = FilenameUtils.removeExtension(multipartFile.getOriginalFilename()) + ".docx";

            convertedDocument = saveFile(docxConvertedLocation, docxFileName, convertedFile);
            convertedDocument.setFileType(FileType.DOCX);

        } catch (IOException e) {
            logger.error("Error while converting docx to unicode.. : "+e.getMessage());           //TODO logging stack trace
        }
        return convertedDocument;
    }

    public StoredFile saveFile(String convertedFileLocation, String originalFileNameWithModifedExt, POIXMLDocument convertedFile) {

        StoredFile convertedDocument = new StoredFile();
        String rootConvertedFileLocation = rootDocumentDirectory + convertedFileLocation;
        try {
            File directory = new File(convertedFileLocation);
            logger.info("Creating Uplo  ad directory if not exist : " + convertedFileLocation + " : " + directory.mkdirs());
            String localTime = LocalDateTime.now().toString() + " ";
            String outPutFileName = localTime + originalFileNameWithModifedExt;
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
