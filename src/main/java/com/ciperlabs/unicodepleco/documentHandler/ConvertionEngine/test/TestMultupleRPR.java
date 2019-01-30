package com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.test;

import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.Engine;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import java.io.*;
import java.util.List;

public class TestMultupleRPR {


        public static void main(String[] args) {
            InputStream fileStream = null;
            try {
                fileStream = new FileInputStream(
                        "/home/gayan/Desktop/CiperLabs/unicode-pleco/Documents/uploaded/docx/2019-01-29T14:41:17.163 INSPECTOR_OF_CUSTOMS_APPLICATION_SINHALA (1).zip");
                 XWPFDocument document = new XWPFDocument(fileStream);

                 List<XWPFParagraph> paragraphs = document.getParagraphs();
                for (XWPFParagraph paragraph: paragraphs) {
                    List<XWPFRun> runs = paragraph.getRuns();
                    for (XWPFRun run: runs) {
//                        System.out.println(run.toString());
                        CTR ctrRun =  run.getCTR();
                        int sizeOfCtr = ctrRun.sizeOfTArray();
//                        System.out.println("CTRPR " + ctrRun);
//                        System.out.println("size of T " + ctrRun.sizeOfTArray());
//                        List<CTText> ctTexts = ctrRun.getTList();
//                        for (CTText ctText: ctTexts) {
//                            System.out.println("CTText : " + ctText.getStringValue());
//
//                        }

                        for (int i = 0; i< sizeOfCtr; i++){
                            System.out.println(run.getText(i));
                        }
//                        System.out.println();
////                                ctrPr.getPosition();
//                        System.out.println();

                    }
                }

                OutputStream fileOut = new FileOutputStream("/home/gayan/Desktop/CiperLabs/test.docx");
                document.write(fileOut);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
