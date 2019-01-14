package com.ciperlabs.unicodepleco.documentHandler.PDF;

import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.Engine;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by gayan on 5/7/18.
 */
public class PDFToUnicodeiText {

    static XWPFParagraph p;
    static XWPFRun run;
    static XWPFRun oldRun;

    public void startConvertioin(File file) {

        String pdf = file.getAbsolutePath();
        //Sinhala fonts
        CTRPr sinhalaUnicodeCTRPr = CTRPr.Factory.newInstance();                        // Set All types of fonts for sinhala types
        CTFonts sinfonts = CTFonts.Factory.newInstance();
        sinfonts.setAscii("Iskoola Pota");
        sinfonts.setHAnsi("Iskoola Pota");
        sinfonts.setCs("Iskoola Pota");
        //Tamil fonts
        CTRPr tamilUnicodeCTRPr = CTRPr.Factory.newInstance();                        // Set All types of fonts for tamil types
        CTFonts tamfonts = CTFonts.Factory.newInstance();
        tamfonts.setAscii("Latha");
        tamfonts.setHAnsi("Latha");
        tamfonts.setCs("Latha");
        tamilUnicodeCTRPr.setRFonts(tamfonts);

        try {
            PdfReader reader = new PdfReader(pdf);
            XWPFDocument doc = new XWPFDocument(); //a document for 
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
//			for(int i=17;i<20;i++){		//TODO delete test only
                p = doc.createParagraph();
                run = p.createRun();
//	            run.getCTR().setRPr(sinhalaUnicodeCTRPr);
                TextExtractionStrategy strategy =
                        parser.processContent(i, new FontDetectionStrategy(run, sinhalaUnicodeCTRPr, tamilUnicodeCTRPr));
//                String text = strategy.getResultantText();
                run.addBreak(BreakType.PAGE);
            }
            FileOutputStream out = new FileOutputStream("pdf3.docx");
            doc.write(out);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class FontDetectionStrategy implements TextExtractionStrategy {
    private String combText = "";
    private String text;
    private Engine convertionEngine = new Engine(); //engine
    private CTRPr sinhalaUnicodeCTRPr;
    private CTRPr tamilUnicodeCTRPr;
    private XWPFParagraph p;
    private XWPFRun run;

    public FontDetectionStrategy(XWPFRun run, CTRPr sinhalaUnicodeCTRPr, CTRPr tamilUnicodeCTRPr) {
        this.sinhalaUnicodeCTRPr = sinhalaUnicodeCTRPr;
        this.tamilUnicodeCTRPr = tamilUnicodeCTRPr;
        this.run = run;
        this.p = run.getParagraph();

    }

    @Override
    public void beginTextBlock() {
        // TODO Auto-generated method stub

    }

    @Override
    public void renderText(TextRenderInfo renderInfo) {
//		this.run = p.createRun();
//		PDFToUnicodeiText.run = this.run;
        String combinedFont = renderInfo.getFont().getPostscriptFontName();
//		System.out.println(combinedFont);
        String[] fontList = combinedFont.split("\\+");
        String font = fontList[0];
        if (fontList.length > 1) {
            font = fontList[1];
        }
//		System.out.println(font);
        String text = renderInfo.getText();
        String[] cnvtText = convertionEngine.toUnicode(text, font);
        if (renderInfo.getAscentLine().getStartPoint().get(0) <= 157 && renderInfo.getAscentLine().getStartPoint().get(0) > 154) {
            /* Start of a new Word*/
            XWPFDocument doc = PDFToUnicodeiText.p.getDocument();
            PDFToUnicodeiText.p = doc.createParagraph();
            PDFToUnicodeiText.run = p.createRun();
            run = PDFToUnicodeiText.run;
            p = PDFToUnicodeiText.p;
        } else if (renderInfo.getAscentLine().getStartPoint().get(0) <= 307 && renderInfo.getAscentLine().getStartPoint().get(0) > 304) {
            /* Start of a new word in right column*/
            XWPFDocument doc = PDFToUnicodeiText.p.getDocument();
            PDFToUnicodeiText.p = doc.createParagraph();
            PDFToUnicodeiText.run = p.createRun();
            run = PDFToUnicodeiText.run;
            p = PDFToUnicodeiText.p;

        } else if (renderInfo.getAscentLine().getEndPoint().get(0) <= 294 && renderInfo.getAscentLine().getEndPoint().get(0) > 293) {
            /* Adding a space to the end of a word so that continuous definition format kept intact */
            cnvtText[0] = cnvtText[0] + " ";
//			System.out.println(cnvtText[1]);
        } else if (renderInfo.getAscentLine().getEndPoint().get(0) <= 443 && renderInfo.getAscentLine().getEndPoint().get(0) > 444) {
            /* Adding a space to the end of a word so that continuous definition format kept intact  in right column*/

            cnvtText[0] = cnvtText[0] + " ";
        }
//		else if(renderInfo.getAscentLine().getStartPoint().get(0)<=168 && renderInfo.getAscentLine().getStartPoint().get(0)>167){
//			/* if the definition of the word has continued to the next page */
//
//		}
        if (p.getParagraphText() == null || p.getParagraphText().equals("")) {
            try {
                double d = Double.parseDouble(text);
                XWPFDocument doc = PDFToUnicodeiText.p.getDocument();
                PDFToUnicodeiText.p = doc.createParagraph();
                PDFToUnicodeiText.run = p.createRun();
                run = PDFToUnicodeiText.run;
                p = PDFToUnicodeiText.p;
            } catch (NumberFormatException e) {

            }
        }

        run.setText(cnvtText[0]);
        if (cnvtText[1].equalsIgnoreCase("SINHALA")) {
//			run.getCTR().setRPr(sinhalaUnicodeCTRPr);
            run.setFontFamily("Iskoola Pota");
        } else if (cnvtText[1].equalsIgnoreCase("TAMIL")) {
//			run.getCTR().setRPr(tamilUnicodeCTRPr);
            run.setFontFamily("Latha");
        } else {                                                    //TODO this should be removed appropriately
//			 System.out.println("other font detected");
            CTRPr otherUnicodeCTRPr = CTRPr.Factory.newInstance();                        // Set All types of fonts for sinhala types
            CTFonts otherfonts = CTFonts.Factory.newInstance();
            otherfonts.setAscii("Iskoola Pota");
            otherfonts.setHAnsi("Iskoola Pota");
            otherfonts.setCs("Iskoola Pota");
            run.getCTR().setRPr(otherUnicodeCTRPr);
        }
        System.out.println(renderInfo.getAscentLine().getStartPoint() + cnvtText[0]);

//		renderInfo.getAscentLine().getStartPoint();
//		run.getCTR()
        PDFToUnicodeiText.oldRun = this.run;                    // Set current run as the olderRun


    }

    @Override
    public void endTextBlock() {
        // TODO Auto-generated method stub


    }

    @Override
    public void renderImage(ImageRenderInfo renderInfo) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getResultantText() {
        // TODO Auto-generated method stub
        String tempText = combText;
        combText = "";
//		System.out.println(tempText);
        return tempText;
    }

    public static XWPFDocument getDocument() {
        return null;

    }

}
