package com.lifedrained.prepjournal.front.pages.ie.views.tabs;

import com.google.gson.Gson;
import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.repo.SubjectRepo;
import com.lifedrained.prepjournal.repo.entities.PlanEntity;
import com.lifedrained.prepjournal.repo.entities.SubjectEntity;
import com.lifedrained.prepjournal.services.PlanService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class PlanIETab extends AbstractIETab<PlanEntity>{

    private static final Logger log = LogManager.getLogger(PlanIETab.class);
    private PlanService planService;

    public PlanIETab() {
        super();
    }

    @Override
    protected String name() {
        return "Импорт\\Экспорт учебного плана";
    }

    @Override
    protected VerticalLayout importPanel() {
        planService = ContextProvider.getBean(PlanService.class);
        return new VerticalLayout();
    }

    @Override
    protected VerticalLayout exportPanel() {
        return new VerticalLayout();
    }

    @Override
    protected String anchorText() {
        return "Экспортировать учебный план";
    }

    @Override
    protected ComponentEventListener<ClickEvent<Button>> exportClick() {
        return null;
    }

    @Override
    protected List<PlanEntity> entitiesToExport() {
        return planService.getRepo().findAll();
    }

    @Override
    public void accept(List<List<String>> rows, Throwable throwable) {
        SubjectRepo subjectRepo = ContextProvider.getBean(SubjectRepo.class);
        List<PlanEntity> planToImport = new ArrayList<>();
        List<String> headerRow = rows.get(0);

        rows.remove(headerRow);

        rows.forEach(row -> {
            String subjectName, dateStartS, dateEndS;

            subjectName = row.get(0).trim();
            SubjectEntity subject = subjectRepo.findByName(subjectName).orElse(null);

            if (subject == null) {
                Notify.error("Дисциплина с указанным именем не найдена! Пропуск строчки");
                return;
            }

            dateStartS = row.get(1).trim();
            dateEndS = row.get(2).trim();
            LocalDate dateStart, dateEnd;
            try{

                dateStart = DateUtils.getLDTFromString(dateStartS).toLocalDate();
                dateEnd = DateUtils.getLDTFromString(dateEndS).toLocalDate();
            }catch (DateTimeParseException ex){
                Notify.error("Введен неправильный формат даты! Пропуск строчки");
                log.error(ex);
                return;
            }

            final int startIndex =  3;
            int weeks =  countWeeks(headerRow,startIndex);

            LinkedHashMap<Integer, Integer> weekMap = new LinkedHashMap<>();
            boolean isBreak = false;

            for (int i = 0; i < weeks; i++) {
                 String hoursPerWeekS =  row.get(i+startIndex).trim();
                hoursPerWeekS = hoursPerWeekS.replaceAll("[a-zA-ZА-Яа-я]", "");

                 try{


                     while (hoursPerWeekS.startsWith("0")){
                         hoursPerWeekS = hoursPerWeekS.replaceFirst("0", "");
                     }
                     double hoursPerWeekD = Double.parseDouble(hoursPerWeekS);

                     Integer hoursPerWeek = (int) hoursPerWeekD ;
                     log.info("hoursPerWeek parsed: {}", hoursPerWeek);
                     weekMap.put(i, hoursPerWeek);
                 }catch (NumberFormatException ex){
                     Notify.error("Неправильно указан формат номера в неделе "+i+". Пропуск строчки");
                     isBreak = true;
                     break;
                 }

            }
            if (isBreak){
                return;
            }

            PlanEntity planEntity = new PlanEntity(dateStart, dateEnd, subject);

            AtomicInteger totalHours = new AtomicInteger(0);

            weekMap.forEach((integer, hoursInWeek)->{
                totalHours.set(totalHours.get() + hoursInWeek);
                log.info("total hours iteration: "+totalHours.get());
            });
            planEntity.setHours(totalHours.get());
            planEntity.setWeeks(weeks);
            planEntity.setWeekMap(new Gson().toJson(weekMap));

            planToImport.add(planEntity);

        });

        planService.saveAll(planToImport);
        Notify.success("Учебный план импортирован");

    }

    private int countWeeks(List<String> headerRow, int startIndex) {

        int weeks = -1;

        Pattern numberPattern = Pattern.compile("[\\D+]");

        for (int i = startIndex; i < headerRow.size(); i++) {
            if (!numberPattern.matcher(headerRow.get(i)).matches()) {
                weeks++;
            }else {
                break;
            }
        }
        log.info("weeks counted: {}", weeks);
        return weeks;
    }
}
