package com.ciperlabs.unicodepleco.documentHandler.Excel;


import com.ciperlabs.unicodepleco.documentHandler.ConvertionEngine.Engine;
import com.ciperlabs.unicodepleco.documentHandler.util.FontLogAbs;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;

import java.util.ArrayList;


public class EXLToUnicode {
    private static String sinhalaUnicodeFont = "Iskoola Pota";
    private static String tamilUnicodeFont = "Latha";
    private XSSFWorkbook workbook;
    private XSSFWorkbook returnWorkbook;
    private Engine engine;
    private ArrayList<FontLogAbs> fontLogs;

    public EXLToUnicode(XSSFWorkbook workbook, ArrayList<FontLogAbs> fontLogs) {
        this.workbook=workbook;
        // Create output Workbook
        this.returnWorkbook = new XSSFWorkbook();
        //Creating a new engine.
        this.engine = new Engine(fontLogs);
//        startConversion();
    }
    public XSSFWorkbook startConversion() {
        convertCells(workbook);
        //convert other elements if will be done in the future! eg : charts
        return workbook;
    }
    public void convertCells(XSSFWorkbook workBook) {
        int numberOfSheets = workBook.getNumberOfSheets();
        System.out.println("WorkBook has " + numberOfSheets + " sheets");
//      Iterating through the sheets
        for (int i = 0; i < numberOfSheets; i++) {
//          creating a new sheet with the same name.
            XSSFSheet sheet = (XSSFSheet) workBook.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            XSSFSheet newSheet = (XSSFSheet) returnWorkbook.createSheet(sheetName);
            System.out.println("Sheet name is " + sheetName);
//              Looping Through each row and cell in the original excel.
            for (Row row : sheet) {
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        // case 1 = cell type is a string
                        case 1:
                            XSSFRichTextString str = (XSSFRichTextString) cell.getRichStringCellValue();
                            System.out.println("***********************Begining of a cell***********************");
                            if (str.numFormattingRuns() > 0) {
                                //richstring for output
                                System.out.println("Number of formatting runs = "+str.numFormattingRuns());
                                XSSFRichTextString richString = new XSSFRichTextString("");
                                //length of a unicode is no equal to a non unicode one so we have to keep track of the correct length.
                                for (int k = 0; k < str.numFormattingRuns() + 1; k++) {
                                    try {
                                        if (str.getFontOfFormattingRun(k) != null) {
                                            String fontname = str.getFontOfFormattingRun(k).getFontName();
                                            int startIndex = str.getIndexOfFormattingRun(k);
                                            int length = str.getLengthOfFormattingRun(k);
                                            String text = str.getString().substring(startIndex, startIndex + length);

                                            System.out.println("Original Font = " + fontname + "\n" + "Original Start Index " + startIndex + "\n" + "Original legth " + length + "\n");
                                            System.out.println("Original text" + text);
                                            String[] convertedText = engine.toUnicode(text, fontname);
                                            System.out.println("Converted text" + convertedText[0]);
                                            //Concatinating the richstrings.
                                            int uicodeStartIndex = richString.length();
                                            int unicodeLegth = convertedText[0].length();
                                            richString = new XSSFRichTextString(richString.getString() + convertedText[0]);
                                            //creating the new font with same size, colour but with the unicode font type.
                                            XSSFFont newFont = str.getFontOfFormattingRun(k);
                                            //selecting sinhala or tamil font

                                            String uicodeFontName = "";
                                            System.out.println("Lengths are from " + startIndex + " to " + (startIndex + length));
                                            if (convertedText[1] == "SINHALA") {
                                                uicodeFontName = sinhalaUnicodeFont;
                                            } else if (convertedText[1] == "TAMIL") {
                                                uicodeFontName = tamilUnicodeFont;
                                            } else {
                                                uicodeFontName = convertedText[1];
                                            }
                                            System.out.println("Output font = " + uicodeFontName);
                                            newFont.setFontName(uicodeFontName); //changing the font
                                            //                                            currentlength
                                            richString.applyFont(uicodeStartIndex, uicodeStartIndex + unicodeLegth, newFont);
                                        }
                                    }
                                    catch (Exception e){
                                        int startIndex = str.getIndexOfFormattingRun(k);
                                        int length = str.getLengthOfFormattingRun(k);
                                        String text = str.getString().substring(startIndex, startIndex + length);

                                        System.out.println("Original Font = " + "Unicode" + "\n" + "Original Start Index " + startIndex + "\n" + "Original legth " + length + "\n");
                                        System.out.println("Original text" + text);
//
                                        //Concatinating the richstrings.
                                        int uicodeStartIndex = richString.length();
                                        richString = new XSSFRichTextString(richString.getString() + text);
//
                                    }
                                }
                                //getting last recorded row number
                                int lastRecodeRowNumber = newSheet.getPhysicalNumberOfRows();
                                XSSFRow newRow = newSheet.getRow(lastRecodeRowNumber - 1);
                                //adding a new row
                                if (newRow == null) {
                                    System.out.println("***** Creating a new row ********");
                                    newRow = newSheet.createRow(lastRecodeRowNumber);
                                }
//                                            XSSFCell cell = row.getCell(k + j);
                                int oldCellIndex = cell.getColumnIndex();
                                Cell newCell = newRow.createCell(oldCellIndex);
                                cell.setCellValue(richString);
                                newCell.setCellValue(richString);
                                System.out.println("column index " + cell.getColumnIndex());
                            } else {
//                                    XSSFRichTextString richString = new XSSFRichTextString();
                                System.out.println("Only one run cell = " + cell.getColumnIndex());
                                XSSFCellStyle style = (XSSFCellStyle) cell.getCellStyle();
                                XSSFFont font = style.getFont();
                                String fontname = font.getFontName();
                                String text = cell.getStringCellValue();
                                System.out.println("Original Font = " + style.getFont().getFontName());
                                System.out.println("Original Text = " + text);
                                String[] convertedText = engine.toUnicode(text, fontname);
                                System.out.println("Converted text =" + convertedText[0]);

                                //getting last recorded cell number
                                //getting last recorded cell number
                                int lastRecodeRowNumber = newSheet.getPhysicalNumberOfRows();
                                XSSFRow newRow = newSheet.getRow(lastRecodeRowNumber - 1);
                                //adding a new row
                                if (newRow == null) {
                                    System.out.println("***** Creating a new row ********");
                                    newRow = newSheet.createRow(lastRecodeRowNumber);
                                }
//                                            XSSFCell cell = row.getCell(k + j);
                                int oldCellIndex = cell.getColumnIndex();
                                Cell newCell = newRow.createCell(oldCellIndex);
                                System.out.println("Converted text from the engine in else : " + convertedText[0]);
                                XSSFRichTextString richString = new XSSFRichTextString(convertedText[0]);
                                //creating the new font with same size, colour but with the unicode font type.
                                XSSFFont newFont = style.getFont();

                                String uniFontName = "";
                                if (convertedText[1] == "SINHALA") {
                                    uniFontName = sinhalaUnicodeFont;
                                } else if (convertedText[1] == "TAMIL") {
                                    uniFontName = tamilUnicodeFont;
                                } else {
                                    uniFontName = convertedText[1];
                                }
                                newFont.setFontName(uniFontName); //changing the font
                                richString.applyFont(newFont);
                                newCell.setCellValue(richString);
                                cell.setCellValue(richString);
//                                    cell.setCellValue(richString);
//                                            System.out.println("column index "+ cell.getColumnIndex())
                            }
                            break;
                        case 0:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                System.out.println(cell.getDateCellValue());
                            } else {
                                System.out.println(cell.getNumericCellValue());
                            }
                            break;
//                            case CellType.BOOLEAN:
//                                System.out.println(cell.getBooleanCellValue());
//                                break;
//                            case CellType.FORMULA:
//                                System.out.println(cell.getCellFormula());
//                                break;
//                            case CellType.BLANK:
//                                System.out.println();
//                                break;
//                            default:
//                                System.out.println();
                    }
                }
            }

        }
    }
    //getting the output workbook.
    public Workbook getWorkBook(){
        return this.returnWorkbook;
    }
    //getting the original workbook which we changed in line 100, 143
    //    public  Workbook getChangedOriginalWorkbook(){
    //        return this.workbook;
    //    }

    //    public static void main(String[] args) {
    //        InputStream fileStream = null;
    //        try {
    //            fileStream = new FileInputStream(
    //                    "C:\\Users\\sudeepa\\Desktop\\ministris.Results.Framework.2016.12.20.xlsx");
    //            Workbook workBook = WorkbookFactory.create(fileStream);
    //            EXLToUnicode exlToUnicode=new EXLToUnicode((XSSFWorkbook) workBook);
    //
    //
    //            try (OutputStream fileOut = new FileOutputStream("C:\\Users\\sudeepa\\Desktop\\workbooks.xlsx")) {
    //                exlToUnicode.getChangedOriginalWorkbook().write(fileOut);
    //            }
    //
    //        } catch (FileNotFoundException e) {
    //            e.printStackTrace();
    //        } catch (InvalidFormatException e) {
    //            e.printStackTrace();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }

}
