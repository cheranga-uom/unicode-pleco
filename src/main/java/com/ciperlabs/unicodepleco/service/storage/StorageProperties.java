package com.ciperlabs.unicodepleco.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    @Value("${storage.Document.root}")
    private String rootDocumentDirectory;

    @Value("${storage.Document.uploadDocx}")
    private String uploadDocx;

    @Value("${storage.Document.uploadExcel}")
    private String uploadExcel;

    @Value("${storage.Document.uploadPDF}")
    private String uploadPDF;

    @Value("${storage.Document.uploadDoc}")
    private String uploadDoc;

    @Value("${storage.Document.convertedDocx}")
    private String convertedDocx;

    @Value("${storage.Document.convertedExcel}")
    private String convertedExcel;

    @Value("${storage.Document.pdfToWordPDF}")
    private String pdfToWordPDF;

    @Value("${storage.Document.pdfToWordDocx}")
    private String pdfToWordDocx;

    public String getRootDocumentDirectory() {
        return rootDocumentDirectory;
    }

    public String getUploadDocx() {
        return uploadDocx;
    }

    public String getUploadExcel() {
        return uploadExcel;
    }

    public String getUploadPDF() {
        return uploadPDF;
    }

    public String getUploadDoc() {
        return uploadDoc;
    }

    public String getConvertedDocx() {
        return convertedDocx;
    }

    public String getConvertedExcel() {
        return convertedExcel;
    }

    public String getPdfToWordPDF() {
        return pdfToWordPDF;
    }

    public String getPdfToWordDocx() {
        return pdfToWordDocx;
    }
}