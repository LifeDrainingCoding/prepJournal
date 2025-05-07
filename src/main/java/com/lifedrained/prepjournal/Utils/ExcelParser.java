package com.lifedrained.prepjournal.Utils;

import com.google.gson.Gson;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ExcelParser {
    private static final String PAGE_SEPARATOR = "#%";
    private static final Logger log = LogManager.getLogger(ExcelParser.class);
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

    public static Anchor exportExcel(List<GlobalVisitor> entities){

        String date  = DateUtils.getStringFromDateTime(LocalDateTime.now());
        date = date.replace(".", "_");

        Anchor anchor = new Anchor();
        anchor.setText("Скачать выбранных студентов");
        anchor.setHref(new StreamResource(String.format("Students_%s.xlsx" , date),
                () -> getInputStream(parseEntities(entities))));
        anchor.getElement().setAttribute("download" , true);

        return anchor;
    }



    private static XSSFWorkbook parseEntities(List<GlobalVisitor> entities) {

        List<Field> fields = new ArrayList<>(List.of(entities.getFirst().getClass().getDeclaredFields()));
        fields.removeIf(field -> field.getName().equalsIgnoreCase("id")
                || field.getName().toLowerCase().contains("json"));

        final int FIELD_SIZE = fields.size();
        try {
            XSSFWorkbook workbook =  new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet();
            CellStyle style = createCellStyle(workbook);

            for (int i = 0; i < entities.size(); i++) {

               GlobalVisitor entity = entities.get(i);
               XSSFRow row =  sheet.createRow(i);

               for (int j = 0; j < FIELD_SIZE ; j++) {

                   Field field = fields.get(j);
                   String value = ReflectUtils.fieldToString(field, entity);

                   if (JSONValidator.isValid(value)) {
                       continue;
                   }



                   XSSFCell cell = row.createCell(j);
                   cell.setCellValue(value);
                   cell.setCellStyle(style);


                   setAutoSize(sheet,j);
               }

           }
           return workbook;
       } catch (IllegalAccessException e) {
           log.error(e);
           throw new RuntimeException(e);
       }
    }

    private static ByteArrayInputStream getInputStream(XSSFWorkbook workbook) {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            workbook.write(baos);
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static CellStyle createCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont font = workbook.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 16);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        return style;
    }
    private static void setAutoSize(Sheet sheet, int i) {
        sheet.autoSizeColumn(i);
    }
}
