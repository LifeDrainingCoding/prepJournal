package com.lifedrained.prepjournal.front.views;

import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class OtherScheduleParams extends VerticalLayout {
    private SchedulesService service;
    private ScheduleEntity entity;
    public OtherScheduleParams(SchedulesService service, ScheduleEntity entity){
        this.entity = entity;
        this.service = service;
        setAlignItems(Alignment.CENTER);
    }

}
