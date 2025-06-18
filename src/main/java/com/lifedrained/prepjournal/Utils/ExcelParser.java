package com.lifedrained.prepjournal.Utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class ExcelParser {
    private static final Logger log = LogManager.getLogger(ExcelParser.class);

    public static void parseExcel(InputStream is, BiConsumer<List<List<String>>, Throwable> whenComplete, boolean skipHeaders) {
            try {
                Workbook workbook = WorkbookFactory.create(is);
                List<List<String>> rows = new ArrayList<>();
                workbook.sheetIterator().forEachRemaining(sheet -> {
                    if (skipHeaders) {
                        sheet.rowIterator().next();
                    }
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
        date = date.replace("-", "_");

        Anchor anchor = new Anchor();
        anchor.setText("Скачать выбранных студентов");
        anchor.setHref(new StreamResource(String.format("Students_%s.xlsx" , date),
                () -> getInputStream(parseEntities(entities))));
        anchor.getElement().setAttribute("download" , true);

        return anchor;
    }

    private static XSSFWorkbook parseEntities(List<GlobalVisitor> entities) {

           return parseAbstractEntities(entities);

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
        style.setDataFormat(workbook.createDataFormat().getFormat("@"));
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

    public static <T extends BaseEntity>  Anchor exportAbstractExcel(List<T> entities , String anchorName) {
        String date  = DateUtils.getStringFromDateTime(LocalDateTime.now());
        date = date.replace("-", "_");

        Anchor anchor = new Anchor();
        anchor.setText(anchorName);
        if (!entities.isEmpty()){
            anchor.setHref(new StreamResource(String.format(entities.get(0).getClass().getSimpleName()+"_%s.xlsx" , date),
                    () -> getInputStream(parseAbstractEntities(entities))));
            anchor.getElement().setAttribute("download" , true);

        }
        return anchor;

    }

    private static <T extends BaseEntity>  XSSFWorkbook parseAbstractEntities(List<T> entities){

        AtomicBoolean isHeadersFilled = new AtomicBoolean(false);

        List<List<String>> rows = new ArrayList<>();
        List<String> headers = new ArrayList<>();

        entities.forEach(entity -> {

            List<String> row = new ArrayList<>();

            List<Field> fields = new ArrayList<>(List.of(entity.getClass().getDeclaredFields()));
            fields.removeIf(field -> field.getName().equalsIgnoreCase("id")
                    || field.getName().equalsIgnoreCase("uid")
                    || field.getName().toLowerCase().contains("json")
                    || field.getName().toLowerCase().contains("length")
                    || field.isAnnotationPresent(OneToMany.class)
                    || field.isAnnotationPresent(Id.class));


            List<Field> entityFields = new ArrayList<>( CollectionUtils.select(fields,
                    field -> field.isAnnotationPresent(ManyToOne.class)
                            || field.isAnnotationPresent(OneToOne.class)));
            fields.removeAll(entityFields);

            if (!isHeadersFilled.get()) {
                fillHeaders(headers, fields);
                fillHeaders(headers, entityFields);
                isHeadersFilled.set(true);
            }

            fields.forEach(field -> {
                field.setAccessible(true);
                try {
                    row.add(ReflectUtils.fieldToString(field, entity));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

            });

            entityFields.forEach(field -> {
                try {
                    field.setAccessible(true);
                    T  entity1 = (T) field.get(entity);

                    if (entity1 != null) {
                        List<Field> entityFields1 = new ArrayList<>(List.of(entity1.getClass().getDeclaredFields()));
                        row.add(ReflectUtils.getEntityName(entityFields1, entity1));
                    }


                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            rows.add(row);
        });
        rows.addFirst(headers);




        return  parseToWorkbook(rows);
    }
    private static void fillHeaders(List<String> headers, List<Field> fields) {
        fields.forEach(field -> {
            headers.add(field.getName());
        });
    }

    private static XSSFWorkbook parseToWorkbook(List<List<String>> rows) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet =  workbook.createSheet();
        CellStyle style = createCellStyle(workbook);

        for (int i = 0; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            XSSFRow excelRow = sheet.createRow(i);

            for (int j = 0; j < row.size(); j++) {

                XSSFCell cell =  excelRow.createCell(j);
                cell.setCellType(CellType.STRING);
                cell.setCellStyle(style);
                cell.setCellValue(row.get(j));
                setAutoSize(sheet,j);
            }
        }
        return workbook;

    }
}
