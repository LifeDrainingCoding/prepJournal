package com.lifedrained.prepjournal.front.renders;

import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.repo.entities.PlanEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

public class PlanRender extends ComponentRenderer<Component, PlanEntity> {

    private PlanRender(SerializableFunction<PlanEntity, Component> componentFunction) {
        super(componentFunction);
    }

    public static PlanRender dateStart(){
        return new PlanRender(entity->
                new Span(DateUtils.getStringFromDate(entity.getDateStart())));
    }
    public static PlanRender dateEnd(){
        return new PlanRender(entity->
                new Span(DateUtils.getStringFromDate(entity.getDateEnd())));
    }
    public static PlanRender subject(){
        return new PlanRender(entity->
                new Span(entity.getSubject().getName()));
    }
}
