package com.lifedrained.prepjournal.front.pages.schedule_details.views;

import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.front.views.widgets.CustomDateTimePicker;
import com.lifedrained.prepjournal.front.views.widgets.RowWithIntField;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;

import java.time.LocalDateTime;
import java.util.Date;

import static com.lifedrained.prepjournal.consts.StringConsts.SchedulesFieldNames;

public class OtherScheduleParams extends VerticalLayout {
    private SchedulesService service;
    private ScheduleEntity entity;
    private CurrentSession session;

    private RowWithTxtField masterName = null;
    private final CustomDateTimePicker dateTimePicker;
    private final RowWithIntField scheduleDuration;
    private final CustomButton saveChanges;
    public OtherScheduleParams(SchedulesService service, ScheduleEntity entity, CurrentSession session){
        this.entity = entity;
        this.service = service;
        this.session = session;
        setAlignItems(Alignment.CENTER);

        if(session.getEntity().getRole().equals(RoleConsts.ADMIN.value)){
            masterName = new RowWithTxtField((String) SchedulesFieldNames.get(1));
            masterName.getBody().setValue(entity.getMasterName());
            add(masterName);
        }
        String duration = (String) SchedulesFieldNames.get(3);
        Date date = entity.getDate();

        scheduleDuration = new RowWithIntField(duration);
        scheduleDuration.setValue(entity.getDuration());
        scheduleDuration.getBody().addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<IntegerField, Integer>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<IntegerField, Integer> event) {
                enableBtn();
            }
        });

        dateTimePicker =  new CustomDateTimePicker();
        dateTimePicker.setValue(DateUtils.asLocalDateTime(date));
        dateTimePicker.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<DateTimePicker, LocalDateTime>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<DateTimePicker, LocalDateTime> event) {
                enableBtn();
            }
        });

        saveChanges = new CustomButton("Сохранить изменения", new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                entity.setDuration(scheduleDuration.getValue());
                entity.setDate(dateTimePicker.getDate());
                if(masterName != null){
                    entity.setMasterName(masterName.getFieldText());
                }
                service.repo.save(entity);
                Notify.success("Успешно сохранено");
                disableBtn();
            }
        }){{
            setAlignSelf(Alignment.CENTER);
        }};
        add(scheduleDuration,dateTimePicker, saveChanges);
    }
    private void enableBtn(){
        saveChanges.setEnabled(true);
    }
    private void  disableBtn(){
        saveChanges.setEnabled(false);
    }

}
