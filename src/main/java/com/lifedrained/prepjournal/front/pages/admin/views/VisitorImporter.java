package com.lifedrained.prepjournal.front.pages.admin.views;

import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.front.views.Refreshable;
import com.lifedrained.prepjournal.front.views.widgets.CustomUpload;
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
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class VisitorImporter extends VerticalLayout implements Refreshable {
    private static final Logger log = LogManager.getLogger(VisitorImporter.class);
    private final MultiFileMemoryBuffer buffer;
    private final CustomUpload upload;
    private final GlobalVisitorService service;
    private final GroupsRepo groupsRepo;
    public VisitorImporter(GlobalVisitorService service, GroupsRepo groupsRepo) {
        this.service = service;
        this.groupsRepo = groupsRepo;
        add(new H1("Вы можете загрузить таблицу excel в формате csv для импорта обучающихся"));
        buffer = new MultiFileMemoryBuffer();
        upload =  new CustomUpload(buffer);

        upload.addSucceededListener(new ComponentEventListener<SucceededEvent>() {
            @Override
            public void onComponentEvent(SucceededEvent event) {
                String fileName = event.getFileName();
                processFile(buffer.getInputStream(fileName));
            }
        });
        setAlignItems(Alignment.STRETCH);
        add(upload);
    }

    public void processFile(InputStream data){
        InputStreamReader isr = new InputStreamReader(data, StandardCharsets.UTF_8);

        CSVParser parser =  new CSVParserBuilder().withErrorLocale(Locale.forLanguageTag("RU"))
                .withSeparator(',').withIgnoreLeadingWhiteSpace(true)
                .withFieldAsNull( CSVReaderNullFieldIndicator.BOTH).build();
        List<GlobalVisitor> visitors = new ArrayList<>();
        try( CSVReader reader = new CSVReaderBuilder(isr).withCSVParser(parser).build()){
            List<String[]> rows = reader.readAll();
            rows.remove(0);

            rows.forEach(new Consumer<String[]>() {
                @Override
                public void accept(String[] s) {
                    if (!(s.length >= GlobalVisitor.PARAMS_LENGTH)) {
                        log.error("Ошибка при чтении строки. Пропуск строки. {} ", Arrays.deepToString(s));
                        Notify.warning(String.format("Ошибка при чтении строки %d , она будет пропущена и не добавлена", rows.indexOf(s)));
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
                                Integer.parseInt(s[5]), s[6]);

                        if (!groupsRepo.existsByGroup(group)) {
                            GroupEntity groupEntity = new GroupEntity();
                            groupEntity.setGroup(group);
                            groupsRepo.save(groupEntity);
                        }

                        service.getRepo().save(visitor);
                    }catch (Exception ex){
                        log.warn(ex);
                        return;
                    }
                }
            });


            Notify.success("Успешно сохранено");
        }  catch (IOException | CsvException e) {
            Notify.error("Ошибка при импорте таблицы, проверьте корректность введенных данных либо поставьте разделитель ',' ");
            log.error("Ошибка при импорте таблицы: ",e);
        }


    }
}
