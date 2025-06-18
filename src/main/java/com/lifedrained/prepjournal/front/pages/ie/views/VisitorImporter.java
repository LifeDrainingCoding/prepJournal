package com.lifedrained.prepjournal.front.pages.ie.views;

import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.ExcelParser;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.front.views.Refreshable;
import com.lifedrained.prepjournal.front.i18n.CustomUploadI18N;
import com.lifedrained.prepjournal.repo.GroupsRepo;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.lifedrained.prepjournal.repo.entities.GroupEntity;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvException;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class VisitorImporter extends VerticalLayout implements Refreshable {

    private static final Logger log = LogManager.getLogger(VisitorImporter.class);
    private final MultiFileMemoryBuffer buffer;
    private final CustomUploadI18N upload;
    private final GlobalVisitorService service;
    private final GroupsRepo groupsRepo;

    public VisitorImporter(GlobalVisitorService service, GroupsRepo groupsRepo) {
        this.service = service;
        this.groupsRepo = groupsRepo;
        add(new H1("Вы можете загрузить таблицу excel в формате csv или xlsx для импорта обучающихся"));
        buffer = new MultiFileMemoryBuffer();
        upload =  new CustomUploadI18N(buffer);

        upload.addSucceededListener( event -> {
            log.info(event.getFileName());
            String fileName = event.getFileName();

            if (FilenameUtils.getExtension(fileName).equals("csv")) {
                processFile(buffer.getInputStream(fileName));
            }else {
                ExcelParser.parseExcel(buffer.getInputStream(fileName),
                        ((lists, throwable) -> {
                    if (throwable != null) {
                            Notify.error("Ошибка при парсинге excel файла");

                        throw new RuntimeException(throwable);
                    }
                    importToDB(lists);
                }), true);
            }

        });
        setAlignItems(Alignment.STRETCH);
        add(upload);
    }

    public void processFile(InputStream data){
        InputStreamReader isr = new InputStreamReader(data, StandardCharsets.UTF_8);

        CSVParser parser =  new CSVParserBuilder().withErrorLocale(Locale.forLanguageTag("RU"))
                .withSeparator(';').withIgnoreLeadingWhiteSpace(true)
                .withFieldAsNull( CSVReaderNullFieldIndicator.BOTH).build();
        try( CSVReader reader = new CSVReaderBuilder(isr).withCSVParser(parser).build()){
            List<String[]> rows = reader.readAll();
            rows.removeFirst();

            rows.forEach(s -> {
                if (!(s.length == GlobalVisitor.PARAMS_LENGTH)) {
                    log.error("Ошибка при чтении строки. Пропуск строки. {} ", Arrays.deepToString(s));
                    Notify.warning(String.format("Ошибка при чтении строки %d ," +
                                    " она будет пропущена и не добавлена", rows.indexOf(s)));
                    return;
                }
                if (Arrays.stream(s).anyMatch(Objects::isNull)) {
                    return;
                }

                try {

                    String name, birthDateS, courseS,groupName ,enrollDateS, enrollIdS, innIdS,
                            passport, snils;
                    name = s[0];
                    birthDateS = s[1];
                    courseS = s[2];
                    groupName = s[3];
                    enrollDateS = s[4];
                    enrollIdS = s[5];
                    innIdS = s[6];
                    passport = s[7];
                    snils = s[8];

                    Date birthDate = DateUtils.getDateFromString(birthDateS);
                    byte course = Byte.parseByte(courseS);

                    GroupEntity group;
                    Optional<GroupEntity> optGroup =  groupsRepo.findByName(groupName);
                    if (checkForGroup(optGroup)){
                        group = optGroup.get();
                    }else {
                        Notify.error("Группа "+groupName+" не существует");
                        return;
                    }
                    GlobalVisitor gv = assingParams(name, enrollDateS, enrollIdS, innIdS, passport, snils, group, birthDate, course);
                    service.saveDetached(gv);

                }catch (Exception ex){
                    log.warn(ex);
                }
            });

            refresh();
            Notify.success("Успешно сохранено");

        }  catch (IOException | CsvException e) {
            Notify.error("Ошибка при импорте таблицы, проверьте корректность " +
                    "введенных данных либо поставьте разделитель ';' ");
            log.error("Ошибка при импорте таблицы: ",e);
        }

    }

    private void importToDB(List<List<String>> excelData){
        List<GlobalVisitor> visitors = new ArrayList<>();
        log.info("excel data size: {}", excelData.size());
        excelData.removeFirst();
        excelData.forEach(row -> {

            try {


                String name, birthDateS, courseS, groupName, enrollDateS, enrollIdS, innIdS,
                        passport, snils;

                name = row.get(0);
                birthDateS = row.get(1);
                courseS = row.get(2);
                groupName = row.get(3);

                GroupEntity group;
                Optional<GroupEntity> optGroup = groupsRepo.findByName(groupName);
                if (checkForGroup(optGroup)) {
                    group = optGroup.get();
                } else {
                    log.error("не существует группа {}", groupName);
                    Notify.error("Указана несуществующая группа с именем "+groupName);
                    return;
                }

                enrollDateS = row.get(4);
                enrollIdS = row.get(5);
                innIdS = row.get(6);
                passport = row.get(7);
                snils = row.get(8);

                Date birthDate = DateUtils.getDateFromString(birthDateS);
                double courseD = Double.parseDouble(courseS);
                byte course = (byte) courseD;

                GlobalVisitor gv = assingParams(name, enrollDateS, enrollIdS, innIdS, passport, snils, group, birthDate, course);
                visitors.add(gv);

            } catch (Exception e) {
                e.printStackTrace();
                Notify.error("Неверный формат табличных данных, проверьте формат данных");
                return;
            }
        });

        service.saveAll(visitors);

        refresh();
        Notify.success("Успешно импортированы данные обучающихся");
    }

    private GlobalVisitor assingParams(String name, String enrollDateS, String enrollIdS, String innIdS, String passport, String snils, GroupEntity group, Date birthDate, byte course) {
        Date enrollDate = DateUtils.getDateFromString(enrollDateS);

        enrollIdS =  enrollIdS.replaceAll("\\D","");
        innIdS = innIdS.replaceAll("\\D","");
        long enrollId = Long.parseLong(enrollIdS);
        long innId = Long.parseLong(innIdS);


        LocalDate localDate = DateUtils.asLocalDate(birthDate);
        int age = (int) ChronoUnit.YEARS.between(localDate, LocalDate.now());

        GlobalVisitor gv = new GlobalVisitor(name,birthDate, age, course, group, enrollDate, enrollId, innId, passport, snils);
        return gv;
    }

    private boolean checkForGroup( Optional<GroupEntity> optGroup){

        if (optGroup.isPresent()) {
           return true;
        }else {
            Notify.error("УКАЗАНА НЕСУЩЕСТВУЮЩАЯ ГРУППА! Строчка с несуществующей группой будет пропущена",
                    10, Notification.Position.TOP_CENTER);
            return false;
        }
    }

}
