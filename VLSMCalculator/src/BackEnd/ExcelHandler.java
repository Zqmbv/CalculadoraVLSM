package BackEnd;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PropertyTemplate;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.swing.JTable;

public abstract class ExcelHandler {

    public static boolean exportToExcel(JTable table, String path, String networkInfo) throws Exception {
        File file = new File(path);
        DefaultIndexedColorMap colorMap = new DefaultIndexedColorMap();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte VLSM");

            //region Style
            XSSFColor cTitle = new XSSFColor(new Color(0, 32, 96), colorMap);  
            XSSFColor cHeader = new XSSFColor(new Color(0, 112, 192), colorMap); 
            XSSFColor cColumn = new XSSFColor(new Color(221, 235, 247), colorMap); 

            Font fTitle = createFont(workbook, "Calibri", (short) 14, true, IndexedColors.WHITE.getIndex());
            Font fHeader = createFont(workbook, "Calibri", (short) 12, true, IndexedColors.WHITE.getIndex());
            Font fData = createFont(workbook, "Calibri", (short) 11, false, IndexedColors.BLACK.getIndex());

            XSSFCellStyle csTitle = (XSSFCellStyle) workbook.createCellStyle();
            csTitle.setFont(fTitle);
            csTitle.setFillForegroundColor(cTitle);
            csTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            applyAlignment(csTitle);

            XSSFCellStyle csHeader = (XSSFCellStyle) workbook.createCellStyle();
            csHeader.setFont(fHeader);
            csHeader.setFillForegroundColor(cHeader);
            csHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            applyAlignment(csHeader);

            CellStyle csSubnet = workbook.createCellStyle();
            csSubnet.setFont(fData);
            csSubnet.setFillForegroundColor(cColumn);
            csSubnet.setAlignment(HorizontalAlignment.LEFT);
            csSubnet.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle csData = workbook.createCellStyle();
            csData.setFont(fData);
            csData.setAlignment(HorizontalAlignment.LEFT);
            csData.setVerticalAlignment(VerticalAlignment.CENTER);

            // region Table
            int startRow = 2;
            int startCol = 1;
            int totalCols = table.getColumnCount();
            int lastColIndex = startCol + totalCols - 1;

            Row rowTitle = sheet.createRow(startRow-1);
            Cell titleCell = rowTitle.createCell(startCol);
            titleCell.setCellValue("NETWORK: " + networkInfo);
            titleCell.setCellStyle(csTitle);
            sheet.addMergedRegion(new CellRangeAddress(startRow-1, startRow-1, startCol, lastColIndex));

            Row headerRow = sheet.createRow(startRow);
            for (int i=0; i<totalCols; i++) {
                Cell cell = headerRow.createCell(startCol+i);
                cell.setCellValue(table.getColumnName(i));
                cell.setCellStyle(csHeader);
            }

            // region JTable Data
            int currentRowIndex = startRow + 1;
            for (int r=0; r<table.getRowCount(); r++) {
                Row row = sheet.createRow(currentRowIndex++);
                for (int c=0; c<totalCols; c++) {
                    Cell cell = row.createCell(startCol+c);
                    Object value = table.getValueAt(r,c);
                    
                    if(c==0) cell.setCellStyle(csSubnet);
                    else cell.setCellStyle(csData);
                    
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else {
                        cell.setCellValue(value != null ? value.toString() : "");
                    }
                }
            }

            for (int i = 0; i < totalCols; i++) {
                sheet.autoSizeColumn(startCol + i);
            }

            PropertyTemplate pt = new PropertyTemplate();
            CellRangeAddress tableRegion = new CellRangeAddress(startRow-1, currentRowIndex-1, startCol, lastColIndex);
            pt.drawBorders(tableRegion, BorderStyle.MEDIUM, BorderExtent.OUTSIDE); 
            pt.applyBorders(sheet);

            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                workbook.write(out);
                return true;
            } catch (FileNotFoundException FNFE) {
                return false;
            }
        }
    }

    // region Auxiliary
    private static Font createFont(Workbook wb, String name, short size, boolean bold, short colorIndex) {
        Font font = wb.createFont();
        font.setFontName(name);
        font.setFontHeightInPoints(size);
        font.setBold(bold);
        font.setColor(colorIndex);
        return font;
    }

    private static void applyAlignment(CellStyle style) {
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
    }
}