package com.lifedrained.prepjournal.front.views.dialogs;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.Utils.ValidationUtils;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.front.views.filters.EntityFilters;
import com.lifedrained.prepjournal.front.views.widgets.RowDatePicker;
import com.lifedrained.prepjournal.front.views.widgets.RowWithComboBox;
import com.lifedrained.prepjournal.front.views.widgets.RowWithIntField;
import com.lifedrained.prepjournal.repo.SubjectRepo;
import com.lifedrained.prepjournal.repo.entities.PlanEntity;
import com.lifedrained.prepjournal.repo.entities.SubjectEntity;
import com.lifedrained.prepjournal.services.PlanService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashMap;

public class ChangePlanDialog extends AbstractDialog<PlanEntity> {
    private LinkedHashMap<Integer, Integer> loadout ;
    private RowWithComboBox<SubjectEntity> subject;
    private RowWithIntField totalHours;
    private Details weekRoot;
    private RowDatePicker dateStart, dateEnd;
    public ChangePlanDialog(OnConfirmListener<PlanEntity> confirmListener, PlanEntity planEntity) {

        super(confirmListener, planEntity);
        Scroller scroller = new Scroller();
        totalHours = new RowWithIntField("Общая нагрузка(часы)");
        loadout = new LinkedHashMap<>();
        weekRoot = new Details();
        dateStart = new RowDatePicker("Дата начала дисциплины ", new Date());
        dateEnd = new RowDatePicker("Дата конца дисциплины", new Date());

        SubjectRepo repo = ContextProvider.getBean(SubjectRepo.class);
        subject =   new RowWithComboBox<>("Выберите дисциплину", repo.findAll(),
                SubjectEntity::getName, EntityFilters.SUBJECT.get());

        if (entity == null){

            entity = new PlanEntity();
            LocalDate dateS = dateStart.getDatePicker().getValue(),
                    dateE = dateEnd.getDatePicker().getValue();
            if (dateS != null && dateE != null) {
                entity.setDateStart(dateE);
                entity.setDateEnd(dateS);
                entity.setWeeks(DateUtils.countWeeksBetween(dateS, dateE));
                entity.setHours(0);


                weekRoot.addOpenedChangeListener(event -> {
                    if (isEven()){
                       entity.setWeekMap( fillLoadout());
                    }else {
                        weekRoot.setOpened(false);
                    }
                });
            }else {
                Notify.error("Не выбраны даты! ");
            }


        }else {
           getLoadout();
        }

        setListeners();

        VerticalLayout root = new VerticalLayout();
        root.add(subject, dateStart, dateEnd, totalHours,weekRoot);
        scroller.setContent(root);

        headerLayout.add(scroller);
    }
    private void setListeners(){
        totalHours.getBody().addValueChangeListener(event -> {
            entity.setHours(totalHours.getValue());
        });
        dateStart.getDatePicker().addValueChangeListener(event -> {
            entity.setDateStart(event.getValue());
        });
        dateEnd.getDatePicker().addValueChangeListener(event -> {
            entity.setDateEnd(event.getValue());
        });
    }

    private String fillLoadout() {
        LocalDate dateS = dateStart.getDatePicker().getValue(),
                dateE = dateEnd.getDatePicker().getValue();
        entity.setWeeks(DateUtils.countWeeksBetween(dateS, dateE));
        int hoursPerWeek = entity.getHours() / entity.getWeeks();
        int modHours = entity.getHours() % entity.getWeeks();


        for (int i = 0; i < entity.getWeeks(); i++) {
            loadout.put(i+1, hoursPerWeek);
        }

        for (int i = 0; i < entity.getWeeks(); i++) {
            if (modHours >0){
                loadout.merge(i+1, 2, Integer::sum);
                modHours -= 2;
                continue;
            }
            break;
        }
        weekRoot.removeAll();
        loadout.forEach( (key, value) -> {
            Details details = new Details("Неделя "+key);
            details.add(new Span(String.valueOf(value)));
            weekRoot.add(details);
        });
        Gson gson = new Gson();

        return gson.toJson(loadout);

    }

    private void getLoadout() {
        Type type = new TypeToken<LinkedHashMap<Integer, Integer>>() {}.getType();
        loadout = new Gson().fromJson(entity.getWeekMap(), type);
        totalHours.setValue(entity.getHours());
        loadout.forEach( (key, value) -> {
            Details details = new Details("Неделя "+key);
            details.add(new Span(String.valueOf(value)));
            weekRoot.add(details);
        });
    }

    @Override
    protected ComponentEventListener<ClickEvent<Button>> onOk() {
        return event -> {
            if (entity == null){
                entity = new PlanEntity();
            }

            LocalDate dateS = dateStart.getDatePicker().getValue(),
                    dateE = dateEnd.getDatePicker().getValue();

            entity.setDateStart(dateS);
            entity.setDateEnd(dateE);
            entity.setWeeks(DateUtils.countWeeksBetween(dateS, dateE));
            entity.setHours(totalHours.getValue());
            entity.setWeekMap(fillLoadout());
            PlanService service = ContextProvider.getBean(PlanService.class);
            service.saveDetached(entity, subject.getCBoxValue());
            if (ValidationUtils.hasAnyNull(entity)){
                Notify.error("Не все поля заполнены! ");
                return;
            }
            confirmListener.onConfirm(entity);
            close();
        };
    }

    private boolean isEven(){
       if (totalHours.getValue() != 0 && totalHours.getValue() % 2 == 0){

           return true;

       }
        Notify.error("Общее кол-во часов должно быть четным!");
       return false;
    }



}
