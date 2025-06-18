package com.lifedrained.prepjournal.front.renders;

import com.lifedrained.prepjournal.repo.entities.GroupEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

public class GroupRender extends ComponentRenderer<Component, GroupEntity> {
    private GroupRender(SerializableFunction<GroupEntity, Component> componentFunction) {
        super(componentFunction);
    }

    public static GroupRender masterRender() {
        return new GroupRender(entity -> {
            return new Span(entity.getMaster().getName());
        });
    }

    public static GroupRender specialityRender() {
        return new GroupRender(entity -> new Span(entity.getSpeciality().getName()));
    }
}
