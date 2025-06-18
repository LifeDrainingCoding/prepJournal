package com.lifedrained.prepjournal.front.renders;

import com.lifedrained.prepjournal.Utils.SerializationUtils;
import com.lifedrained.prepjournal.data.Visit;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.function.Consumer;

public class MarkRender extends ComponentRenderer<Component,GlobalVisitor> {
    public MarkRender(String uid, Consumer<Visit> onMarkChange) {
        super(entity ->{
           Visit visit = SerializationUtils.findByUid(entity.getJsonVisits(),uid);

            IntegerField mark = new IntegerField();

            mark.setMax(5);
            mark.setMin(2);
            if(visit != null &&  visit.getMark()!=null){
                mark.setValue((int)visit.getMark());
                mark.addValueChangeListener( event -> {
                    visit.setMark(event.getValue().byteValue());
                    onMarkChange.accept(visit);
                });

            }else if(visit != null) {
                mark.addValueChangeListener( event -> {
                    visit.setMark(event.getValue().byteValue());
                    onMarkChange.accept(visit);
                });
            }



            return mark;
        });
    }
}
