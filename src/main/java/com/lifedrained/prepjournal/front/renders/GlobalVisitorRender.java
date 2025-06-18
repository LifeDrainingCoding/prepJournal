package com.lifedrained.prepjournal.front.renders;

import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

public class GlobalVisitorRender extends ComponentRenderer<Component, GlobalVisitor> {
    private GlobalVisitorRender(SerializableFunction<GlobalVisitor, Component> componentFunction) {
        super(componentFunction);
    }
    public static GlobalVisitorRender groupName(){
        return new GlobalVisitorRender(entity-> new Span(entity.getGroup().getName()));
    }

    public static GlobalVisitorRender enrollDate(){
        return new GlobalVisitorRender(entity-> new Span(
                DateUtils.getStringFromDate(entity.getEnrollDate())
        ));
    }
}
