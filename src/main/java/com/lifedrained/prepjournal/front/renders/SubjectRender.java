package com.lifedrained.prepjournal.front.renders;

import com.lifedrained.prepjournal.repo.entities.SubjectEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class SubjectRender extends ComponentRenderer<Component, SubjectEntity> {
    public SubjectRender() {
        super(entity ->{
            return new Span(entity.getMaster().getName());
        });
    }
}
