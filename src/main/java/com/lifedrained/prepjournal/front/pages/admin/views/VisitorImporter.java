package com.lifedrained.prepjournal.front.pages.admin.views;

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
                }));
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
                if (!(s.length >= GlobalVisitor.PARAMS_LENGTH)) {
                    log.error("Ошибка при чтении строки. Пропуск строки. {} ", Arrays.deepToString(s));
                    Notify.warning(String.format("Ошибка при чтении строки %d ," +
                                    " она будет пропущена и не добавлена", rows.indexOf(s)));
                    return;
                }
                if (Arrays.stream(s).anyMatch(Objects::isNull)) {
                    return;
                }

                try {

                    String group = s[4];
                    Date birthDate = DateUtils.getDateFromString(s[1]);
                    LocalDate localDate = DateUtils.asLocalDate(birthDate);
                    int age = (int) ChronoUnit.YEARS.between(localDate, LocalDate.now());

                    GlobalVisitor visitor = new GlobalVisitor(s[0],
                            birthDate,
                            age, s[2], s[3], group,
                            s[5]);

                    addToGroup(group);

                    service.getRepo().save(visitor);
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
        excelData.removeFirst();
        excelData.forEach(row -> {
            GlobalVisitor gv = new GlobalVisitor();

            String name,birthDateS, masterName, speciality, group, notes;

            name = row.get(0);
            birthDateS = row.get(1);
            masterName = row.get(2);
            speciality = row.get(3);
            group = row.get(4);
            notes = row.get(5);

            LocalDate localDate = DateUtils.asLocalDate(DateUtils.getDateFromString(birthDateS));
            int age = (int) ChronoUnit.YEARS.between(localDate, LocalDate.now());

            gv.setName(name);
            gv.setBirthDate(DateUtils.asDate(localDate));
            gv.setAge(age);
            gv.setSpeciality(speciality);
            gv.setGroup(group);
            gv.setNotes(notes);
            gv.setLinkedMasterName(masterName);

            addToGroup(group);
            visitors.add(gv);
        });

        service.getRepo().saveAll(visitors);

        refresh();
        Notify.success("Успешно импортировано данные");
    }

    private void addToGroup(String group){
        if (!groupsRepo.existsByGroup(group)) {
            GroupEntity groupEntity = new GroupEntity();
            groupEntity.setGroup(group);
            groupsRepo.save(groupEntity);
        }
    }
}
