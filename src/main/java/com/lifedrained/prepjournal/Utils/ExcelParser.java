package com.lifedrained.prepjournal.Utils;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ExcelParser {
    private static final String PAGE_SEPARATOR = "#%";
    public static String DELIMITER = ";";
    public static String LINE_SEPARATOR = "\n";

    public static void parseExcel(InputStream is, BiConsumer<List<List<String>>, Throwable> whenComplete) {
            try {

                Workbook workbook = WorkbookFactory.create(is);
                List<List<String>> rows = new ArrayList<>();
                workbook.sheetIterator().forEachRemaining(sheet -> {
                    sheet.rowIterator().next();
                    sheet.rowIterator().forEachRemaining(row -> {

                        List<String> values = new ArrayList<>();
                        row.cellIterator().forEachRemaining(cell -> {

                            String cellValue = null;
                            switch (cell.getCellType()) {
                                case STRING -> cellValue = cell.getStringCellValue().trim();
                                case NUMERIC -> {
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        cellValue = DateUtils.parseLocalTemporal(
                                                DateUtil.getJavaDate(cell.getNumericCellValue()));
                                    }else {

                                        cellValue = String.valueOf(cell.getNumericCellValue());
                                    }

                                }
                                case BOOLEAN -> cellValue = String.valueOf(cell.getBooleanCellValue());
                                case FORMULA -> cellValue = String.valueOf(cell.getCellFormula());
                            }

                            values.add(cellValue);

                        });
                            rows.add(values);

                    });

                });

                whenComplete.accept( rows, null);
            } catch (IOException e) {
                whenComplete.accept( null, e);
                throw new RuntimeException(e);
            }



    }

}
