package com.ciperlabs.unicodepleco.documentHandler.word;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jodconverter.DocumentConverter;
import org.jodconverter.document.DefaultDocumentFormatRegistry;
import org.jodconverter.document.DocumentFormat;
import org.jodconverter.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;

public class HWPFtoXWPF {

    private DocumentConverter converter;

    public HWPFtoXWPF(DocumentConverter converter){
        this.converter = converter;
    }

    public InputStream convertToDo(MultipartFile inputFile){
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            String docxFileName = FilenameUtils.removeExtension(inputFile.getOriginalFilename()) + ".docx";

            final DocumentFormat targetFormat =
                    DefaultDocumentFormatRegistry.getFormatByExtension("docx");
            converter
                    .convert(inputFile.getInputStream())
                    .as(
                            DefaultDocumentFormatRegistry.getFormatByExtension(
                                    FilenameUtils.getExtension(docxFileName)))
                    .to(baos)
                    .as(targetFormat)
                    .execute();

            try(ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray())){
                return bais;

            }

        } catch (OfficeException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//
//        File file = new File("/home/gayan/Desktop/CiperLabs/test Samples/test.doc");
//    }
}
