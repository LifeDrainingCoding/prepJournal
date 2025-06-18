package com.lifedrained.prepjournal.front.pages.scheduledetails.views;

import com.lifedrained.prepjournal.Utils.ComponentSecurer;
import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.front.views.filters.EntityFilters;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.front.views.widgets.*;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Date;

import static com.lifedrained.prepjournal.consts.StrConsts.*;

public class OtherScheduleParams extends VerticalLayout implements ComponentSecurer {
    private SchedulesService service;
    private ScheduleEntity entity;
    private CurrentSession session;

    private RowWithComboBox<LoginEntity> masterName = null;
    private final CustomDateTimePicker dateTimePicker;
    private final RowWithIntField scheduleDuration;
    private final CustomButton saveChanges;
    public OtherScheduleParams(SchedulesService service, ScheduleEntity entity, CurrentSession session, LoginRepo loginRepo){
        this.entity = entity;
        this.service = service;
        this.session = session;
        setAlignItems(Alignment.CENTER);

        if(session.getEntity().getRole().equals(RoleConsts.ADMIN.value)){
            masterName = new RowWithComboBox<>("ФИО Преподавателя",loginRepo.findAllByRole(RoleConsts.USER_TIER1.value), LoginEntity::getName, EntityFilters.LOGIN.get());
            masterName.getBody().setValue(loginRepo.findByUid(entity.getMaster().getUid()).get());
            add(masterName);
        }
        String duration = (String) getScheduleFieldValues(loginRepo.findAll()).get(3);
        Date date = entity.getDate();

        scheduleDuration = new RowWithIntField(duration);
        scheduleDuration.setValue(entity.getHours());
        scheduleDuration.setEnabled(false);

        dateTimePicker =  new CustomDateTimePicker();
        dateTimePicker.setValue(DateUtils.asLocalDateTime(date));
        dateTimePicker.addValueChangeListener(event -> enableBtn());

        saveChanges = new CustomButton("Сохранить изменения",  event -> {
            entity.setDate(dateTimePicker.getDate());
            if(masterName != null){
                entity.setMaster(masterName.getCBoxValue());
            }
            service.getRepo().save(entity);
            Notify.success("Успешно сохранено");
            disableBtn();
        }){{
            setAlignSelf(Alignment.CENTER);
        }};

        checkSecurity(2,scheduleDuration, dateTimePicker, saveChanges);
        add(scheduleDuration,dateTimePicker, saveChanges);
    }
    private void enableBtn(){
        saveChanges.setEnabled(true);
    }
    private void  disableBtn(){
        saveChanges.setEnabled(false);
    }

}
