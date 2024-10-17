package com.lifedrained.prepjournal.front.renders;

import com.lifedrained.prepjournal.Utils.TimeUtils;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

public class ScheduleDateRender<T extends BaseEntity> extends ComponentRenderer<Component, T> {
    public ScheduleDateRender() {
        super(new SerializableFunction<T, Component>() {
            @Override
            public Component apply(T t) {
                return new Text(TimeUtils.convertToString(((ScheduleEntity)t).getScheduleTime()));
            }
        });
    }
}
