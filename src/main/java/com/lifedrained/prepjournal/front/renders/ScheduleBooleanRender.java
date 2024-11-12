package com.lifedrained.prepjournal.front.renders;

import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

public class ScheduleBooleanRender extends ComponentRenderer<Component, ScheduleEntity> {
    public ScheduleBooleanRender() {
        super((SerializableFunction<ScheduleEntity, Component>) entity ->{
            String text = "Нет";
            if(entity.isExecuted()){
                text = "Да";
            }
               return new Span(text);
        });
    }
}
