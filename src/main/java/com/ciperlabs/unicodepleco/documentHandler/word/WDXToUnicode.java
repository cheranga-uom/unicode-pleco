package com.ciperlabs.unicodepleco.documentHandler.word;

import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.Engine;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gayan@ciperlabs.com on 4/21/18.
 */
public class WDXToUnicode {

    private XWPFDocument docx = null;
    private static String sinhalaUnicodeFont = "Iskoola Pota";
    private static String tamilUnicodeFont = "Latha";
    private static String sinhalaExtraFont = "Nirmala UI";
    private static String[] nonStartables = {"ා", "ැ", "ෑ", "ි", "ී", "ු", "ූ", "ෘ", "ෙ",
            "ේ", "ෛ", "ො", "ෝ", "ෞ", "ෟ", "ෲ", "ෳ", "්", "்", "ா", "ி", "ீ", "ே", "ெ"};
    private CTFonts sinhalaUnicodeFonts = null;
    private CTFonts tamilUnicodeFonts = null;
    private Engine engine;
    private XWPFStyles documentStyles = null;
    private HashMap<String, XWPFStyle> stylesCopy = null;
    private String currentStyleId = null;
    private HashMap<String, String> styeleConvertedFont = null;                  //StyleId,font

    private WDXToUnicode() {
    }                            // Avoid accidental creation of Object without File

    public WDXToUnicode(XWPFDocument docx) {
        this.docx = docx;
        // Initialize All types of fonts for Sinhala types
        sinhalaUnicodeFonts = CTFonts.Factory.newInstance();
        sinhalaUnicodeFonts.setAscii(sinhalaUnicodeFont);
        sinhalaUnicodeFonts.setHAnsi(sinhalaUnicodeFont);
        sinhalaUnicodeFonts.setCs(sinhalaUnicodeFont);


        // Initialize All types of fonts for Tamil types
        tamilUnicodeFonts = CTFonts.Factory.newInstance();
        tamilUnicodeFonts.setAscii(tamilUnicodeFont);
        tamilUnicodeFonts.setHAnsi(tamilUnicodeFont);
        tamilUnicodeFonts.setCs(tamilUnicodeFont);

        this.engine = new Engine();
        this.documentStyles = this.docx.getStyles();

        this.stylesCopy = new HashMap<>();
        this.styeleConvertedFont = new HashMap<>();
    }

    public XWPFDocument startConversion() {


        convertParagraphs(docx);
        convertFooter(docx);
        convertHeader(docx);
        convertTables(docx);
        updateStylesToNewFont();
        return docx;
    }

    private void convertParagraphs(XWPFDocument docx) {
        List<XWPFParagraph> paragraphs = docx.getParagraphs();
        int paragraphsCount = paragraphs.size();
        int currentParagraphPosition = 0;

        while (currentParagraphPosition < paragraphsCount) {

            XWPFParagraph paragraph = paragraphs.get(currentParagraphPosition);
            List<XWPFRun> runs = paragraph.getRuns();
            if (runs != null) {
                int runsLength = runs.size();
                int i = 0;
                while (i < runsLength) {
                    XWPFRun run = runs.get(i);
                    String fontFamily = getFontFamily(run);
                    String[] convertedText = engine.toUnicode(run.getText(0), fontFamily);
                    String sConvertedText = convertedText[0];

                    // Fixing broken Word issues 1st attempt
                    for (String nonStartable : nonStartables) {
                        if (sConvertedText != null && sConvertedText.startsWith(nonStartable)) {
//                            run.setText(text.substring(1),0);
                            sConvertedText = sConvertedText.substring(1);
                            if (i > 0) {
                                XWPFRun preRun = runs.get(i - 1);
                                preRun.setText(nonStartable, -1);
                            } else if (currentParagraphPosition > 0) {
                                List<XWPFRun> olderRuns = paragraphs.get(currentParagraphPosition).getRuns();
                                XWPFRun olderRun = olderRuns.get(olderRuns.size() - 1);
                                olderRun.setText(nonStartable, -1);
                            }
                        }
                    }

                    //fixing broken word issue 2nd attempt
                    for (String nonStartable : nonStartables) {
                        if (sConvertedText != null && sConvertedText.startsWith(nonStartable)) {
//                            run.setText(text.substring(1),0);
                            sConvertedText = sConvertedText.substring(1);
//                            System.out.println(sConvertedText.substring(1)+ " : "+run.getText(0)+nonStartable);
                            if (i > 0) {
                                XWPFRun preRun = runs.get(i - 1);
                                preRun.setText(nonStartable, -1);
                            } else if (currentParagraphPosition > 0) {
                                List<XWPFRun> olderRuns = paragraphs.get(currentParagraphPosition).getRuns();
                                XWPFRun olderRun = olderRuns.get(olderRuns.size() - 1);
                                olderRun.setText(nonStartable, -1);
                            }
                        }
                    }
                    run.setText(sConvertedText, 0);
                    this.setFontFamily(run, convertedText[1]);
                    i++;
                }
            }
            convertTextBoxes(paragraph);                   //Convert Text boxes within the current paragraph
            currentParagraphPosition++;
        }
    }

    private void convertTables(XWPFDocument docx) {

        List<XWPFTable> tables = docx.getTables();
        for (XWPFTable table : tables) {
            List<XWPFTableRow> rows = table.getRows();
            for (XWPFTableRow row : rows) {
                List<XWPFTableCell> cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    List<XWPFParagraph> cellParagraphs = cell.getParagraphs();
                    for (XWPFParagraph celssParagraph : cellParagraphs) {

                        List<XWPFRun> cellRuns = celssParagraph.getRuns();

                        for (XWPFRun footerTableCellRun : cellRuns) {
                            String fontFamily = getFontFamily(footerTableCellRun);
                            String[] convertedText = engine.toUnicode(footerTableCellRun.getText(0), fontFamily);
                            String sConvertedText = convertedText[0];
                            footerTableCellRun.setText(sConvertedText, 0);

                            this.setFontFamily(footerTableCellRun, convertedText[1]);

                        }

                    }

                }

            }

        }
    }

    private void convertFooter(XWPFDocument docx) {

        List<XWPFFooter> footers = docx.getFooterList();
        int sizeOffootersArray = footers.size();
        if (sizeOffootersArray > 0) {
            System.out.println("Converting footers...");
            for (XWPFFooter footer : footers) {
                // Converting Paragraphs in Footer
                List<XWPFParagraph> footerParagraphs = footer.getParagraphs();
                for (XWPFParagraph footerParagraph : footerParagraphs) {
                    System.out.println("Have Footer Paragraphs");
                    List<XWPFRun> footerRuns = footerParagraph.getRuns();
                    for (XWPFRun footerRun : footerRuns) {
                        String fontFamily = getFontFamily(footerRun);
                        String[] convertedText = engine.toUnicode(footerRun.getText(0), fontFamily);
                        String sConvertedText = convertedText[0];

                        footerRun.setText(sConvertedText, 0);

                        this.setFontFamily(footerRun, convertedText[1]);

                    }

                }

                // Converting tables in footer
                List<XWPFTable> footerTables = footer.getTables();
                for (XWPFTable footerTable : footerTables) {
//         			System.out.println("Have Footer T Paragraphs");
                    List<XWPFTableRow> footerTableRows = footerTable.getRows();
                    for (XWPFTableRow footerTableRow : footerTableRows) {
                        System.out.println("Have Footer T Runs...");
                        List<XWPFTableCell> footerTableCells = footerTableRow.getTableCells();
                        for (XWPFTableCell footerTableCell : footerTableCells) {
                            List<XWPFParagraph> footerTableCellParagraps = footerTableCell.getParagraphs();
                            for (XWPFParagraph footerTableCellParagraph : footerTableCellParagraps) {

                                List<XWPFRun> footerTableCellRuns = footerTableCellParagraph.getRuns();

                                for (XWPFRun footerTableCellRun : footerTableCellRuns) {
                                    String fontFamily = getFontFamily(footerTableCellRun);
                                    String[] convertedText = engine.toUnicode(footerTableCellRun.getText(0), fontFamily);
                                    String sConvertedText = convertedText[0];
                                    footerTableCellRun.setText(sConvertedText, 0);
                                    this.setFontFamily(footerTableCellRun, convertedText[1]);
                                }

                            }

                        }

                    }

                }
            }
        }
    }

    private void convertHeader(XWPFDocument docx) {
        List<XWPFHeader> headers = docx.getHeaderList();
        int sizeOffootersArray = headers.size();
        if (sizeOffootersArray > 0) {
//            System.out.println("Converting headers...");
            for (XWPFHeader header : headers) {
                // Converting Paragraphs in Footer
                List<XWPFParagraph> headerParagraphs = header.getParagraphs();
                for (XWPFParagraph headerParagraph : headerParagraphs) {
//                    System.out.println("Have Footer Paragraphs");
                    List<XWPFRun> headerRuns = headerParagraph.getRuns();
                    for (XWPFRun headerRun : headerRuns) {
//                        System.out.println("Have Footer Runs...");
                        String fontFamily = getFontFamily(headerRun);
                        String[] convertedText = engine.toUnicode(headerRun.getText(0), fontFamily);
                        String sConvertedText = convertedText[0];
                        headerRun.setText(sConvertedText, 0);
//                        System.out.println(sConvertedText);
                        this.setFontFamily(headerRun, convertedText[1]);
                    }

                }

                // Converting tables in header
                List<XWPFTable> headerTables = header.getTables();
                for (XWPFTable headerTable : headerTables) {
                    System.out.println("Have Header T Paragraphs");
                    List<XWPFTableRow> headerTableRows = headerTable.getRows();
                    for (XWPFTableRow headerTableRow : headerTableRows) {
                        System.out.println("Have header T Runs...");
                        List<XWPFTableCell> headerTableCells = headerTableRow.getTableCells();
                        for (XWPFTableCell headerTableCell : headerTableCells) {
                            List<XWPFParagraph> headerTableCellParagraps = headerTableCell.getParagraphs();
                            for (XWPFParagraph headerTableCellParagraph : headerTableCellParagraps) {

                                List<XWPFRun> headerTableCellRuns = headerTableCellParagraph.getRuns();

                                for (XWPFRun headerTableCellRun : headerTableCellRuns) {
                                    String fontFamily = getFontFamily(headerTableCellRun);
                                    String[] convertedText = engine.toUnicode(headerTableCellRun.getText(0), fontFamily);
                                    String sConvertedText = convertedText[0];
                                    headerTableCellRun.setText(sConvertedText, 0);
//                                    System.out.println(sConvertedText);
                                    this.setFontFamily(headerTableCellRun, convertedText[1]);

                                }

                            }

                        }

                    }

                }
            }
        }
    }

    private void convertTextBoxes(XWPFParagraph paragraph) {
            /*
            Invoked from the convert Paragraphs method to reduce the time complexity.
             */
        XmlCursor cursor = paragraph.getCTP().newCursor();
        cursor.selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//*/w:txbxContent/w:p/w:r");

        List<XmlObject> ctrsintxtbx = new ArrayList<>();

        while (cursor.hasNextSelection()) {
            cursor.toNextSelection();
            XmlObject obj = cursor.getObject();
            ctrsintxtbx.add(obj);
        }
        for (XmlObject xmlObject : ctrsintxtbx) {
            CTR ctr = null;
            try {
                ctr = CTR.Factory.parse(xmlObject.toString());
                XWPFRun textBoxRun = new XWPFRun(ctr, paragraph);
//                    textBoxRun.getText(0);
                String fontFamily = getFontFamily(textBoxRun);
                String[] convertedText = engine.toUnicode(textBoxRun.getText(0), fontFamily);
                String sConvertedText = convertedText[0];
                textBoxRun.setText(sConvertedText, 0);
//                    System.out.println(sConvertedText);

                this.setFontFamily(textBoxRun, convertedText[1]);

                xmlObject.set(textBoxRun.getCTR());
            } catch (XmlException e) {
                e.printStackTrace();
            }

        }
    }

    private void updateStylesToNewFont() {

        for (String styleId : stylesCopy.keySet()) {
            XWPFStyle style = stylesCopy.get(styleId);
            String font = styeleConvertedFont.get(styleId);
            if (font.equals("SINHALA")) {                                 // Setting Font Family
//                        run.getCTR().setRPr(sinhalaUnicodeCTRPr);
                try {
                    CTFonts ctFonts = style.getCTStyle().getRPr().getRFonts();
                    if (ctFonts != null) {
                        ctFonts.setAscii(sinhalaUnicodeFont);
                        ctFonts.setCs(sinhalaUnicodeFont);
                        ctFonts.setHAnsi(sinhalaUnicodeFont);
                    }
                } catch (NullPointerException ex) {
                    System.out.println("Null style Id encountered");
                }


            } else if (font.equals("TAMIL")) {
//                        run.getCTR().setRPr(tamilUnicodeCTRPr);
                try {
                    CTFonts ctFonts = style.getCTStyle().getRPr().getRFonts();
                    if (ctFonts != null) {
                        ctFonts.setAscii(tamilUnicodeFont);
                        ctFonts.setCs(tamilUnicodeFont);
                        ctFonts.setHAnsi(tamilUnicodeFont);
                    }
                } catch (NullPointerException nuex) {
                    System.out.println("Null Style Id encountered.!");
                }

            }
        }
    }

    private String getFontFamily(XWPFRun run) {
        String fontFamily = run.getFontFamily();
        if (fontFamily == null) {                                         // When the font in the run is null check for the default fonts in styles.xml
            String styleID = run.getParagraph().getStyleID();
            if (styleID != null) {
                XWPFStyle style;
                if (stylesCopy.containsKey(styleID)) {
                    style = stylesCopy.get(styleID);
                } else {
                    style = this.documentStyles.getStyle(styleID);
                    stylesCopy.put(styleID, style);
                }
                currentStyleId = styleID;
                CTStyle ctStyle = style.getCTStyle();
                CTRPr ctrPr = ctStyle.getRPr();
                if (ctrPr != null) {
                    CTFonts ctFonts = ctrPr.getRFonts();
                    if (ctFonts != null) {
                        fontFamily = ctFonts.getAscii();
                        if (fontFamily == null) {
                            fontFamily = ctFonts.getCs();
                        }
                    }
                }

            }

        } else {
            currentStyleId = null;
        }
        return fontFamily;
    }

    private void setFontFamily(XWPFRun run, String font) {
        run.setFontFamily(font);

//        int fontSize = run.getFontSize();
        if (font.equals("SINHALA")) {                                 // Setting Font Family
//                        run.getCTR().setRPr(sinhalaUnicodeCTRPr);
            CTFonts ctFonts = run.getCTR().getRPr().getRFonts();
            ctFonts.setAscii(sinhalaUnicodeFont);
            ctFonts.setCs(sinhalaUnicodeFont);
            ctFonts.setHAnsi(sinhalaUnicodeFont);
        } else if (font.equals("TAMIL")) {
//                        run.getCTR().setRPr(tamilUnicodeCTRPr);
            CTFonts ctFonts = run.getCTR().getRPr().getRFonts();
            ctFonts.setAscii(tamilUnicodeFont);
            ctFonts.setCs(tamilUnicodeFont);
            ctFonts.setHAnsi(tamilUnicodeFont);
        }
        if (currentStyleId != null) {
            styeleConvertedFont.put(currentStyleId, font);
        }
//        run.setFontSize(fontSize);

    }
}