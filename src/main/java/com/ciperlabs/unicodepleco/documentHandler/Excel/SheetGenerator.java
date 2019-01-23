package com.ciperlabs.unicodepleco.documentHandler.Excel;

import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.*;


public class SheetGenerator {

    protected XSSFSheet nSheet;
    private Workbook wb;
    protected String sheetName;
    protected XSSFSheet oSheet;
    protected int numberOfColumns;
    protected int numberOfRows;

    public SheetGenerator(Workbook wb, String sheetName, XSSFSheet oSheet) {
        this.oSheet = oSheet;
        this.wb = wb;
        this.sheetName = sheetName;
        numberOfRows=oSheet.getPhysicalNumberOfRows();

        genetator(); // Create sheet and add it to the workbook
//        setRowAndColumnSizes();
        System.out.println(sheetName + " sheet created has" + numberOfColumns + "columns and "+ numberOfRows);
        read();
    }
    public void read(){
        for (int i=0;i<numberOfRows;i++){
            Row row=oSheet.getRow(i);
            int numberOfCells =row.getPhysicalNumberOfCells();
            for (int j=0;j<numberOfCells;j++){
                Cell cell=row.getCell(i);
//                XSSFCellStyle style = cell.getCellStyle();
                XSSFRichTextString style = (XSSFRichTextString) cell.getRichStringCellValue();
                int nums=style.numFormattingRuns();
                System.out.println("number of styles are "+nums);
                for (int k = 0; k < nums; k++) {
                      System.out.println(style.getFontOfFormattingRun(k));
//                    System.out.println(style.getIndexOfFormattingRun(k));
                }
//                XSSFFont font = style.getFont();
            }
        }
    }

    protected void genetator() {
        XSSFSheet nSheet = (XSSFSheet) wb.createSheet(sheetName);
        this.nSheet = nSheet;
    }
    private void setRowAndColumnSizes() {
        int physicalRowNumber = oSheet.getPhysicalNumberOfRows();
        int physicalColumnNumber = oSheet.getRow(0).getPhysicalNumberOfCells();

        System.out.println(" physical rows "+physicalRowNumber+ "Physical columns "+physicalColumnNumber);
        // Update the number of rows
        for (int i = 0; i < physicalRowNumber; i++) {

            XSSFRow row = oSheet.getRow(i);
            XSSFCell cell = row.getCell(0);
            if (cell != null) {

                // If the number of rows get by getPhysicalNumberOfRows() method is wrong then get the actual number of rows by table color
                if (cell.getCellStyle().getFillBackgroundColorColor() == null) {
                    this.numberOfRows = i;
                    break;
                }
            }else {
                this.numberOfRows =i;
                break;
            }

            // If the number of rows get by getPhysicalNumberOfRows() method is correct then keep that number
            this.numberOfRows =i+1;

        }

        // Update the number of columns
        XSSFRow row = oSheet.getRow(0);
        for (int i = 0; i < physicalColumnNumber; i++) {

            XSSFCell cell = row.getCell(i);
            if (cell != null) {

                if (cell.getCellStyle().getFillBackgroundColorColor() == null) {
                    this.numberOfColumns = i;
                    break;
                }
            }else {
                this.numberOfColumns =i;
                break;
            }

            // If the number of columns get by getPhysicalNumberOfRows() method is correct then keep that number
            this.numberOfColumns =i+1;

        }
    }
}
