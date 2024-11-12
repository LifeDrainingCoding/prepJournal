package com.lifedrained.prepjournal.front.renders;

import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

public class DateVisitorRender extends ComponentRenderer<Component, GlobalVisitor> {
    public DateVisitorRender() {
        super(new SerializableFunction<GlobalVisitor, Component>() {
            @Override
            public Component apply(GlobalVisitor globalVisitor) {

                return new Span(DateUtils.getStringFromDate(globalVisitor.getBirthDate()));
            }
        });
    }
}
