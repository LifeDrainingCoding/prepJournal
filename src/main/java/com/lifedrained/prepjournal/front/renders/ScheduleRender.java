package com.lifedrained.prepjournal.front.renders;

import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

public class ScheduleRender extends ComponentRenderer<Component, ScheduleEntity> {

    private ScheduleRender(SerializableFunction<ScheduleEntity, Component> componentFunction) {
        super(componentFunction);
    }
    public static ScheduleRender subjectName(){

        SchedulesService schedulesService = ContextProvider.getBean(SchedulesService.class);
        return  new ScheduleRender(entity ->
                new Span(schedulesService.getSubjectSchedule(entity).getName()));
    }

    public static ScheduleRender masterName() {
        SchedulesService schedulesService = ContextProvider.getBean(SchedulesService.class);
        return  new ScheduleRender(entity -> new Span( schedulesService.getLoginSchedule(entity).getName()));
    }
}
