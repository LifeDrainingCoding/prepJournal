package com.lifedrained.prepjournal.front.views.widgets;

import com.lifedrained.prepjournal.front.interfaces.OnCheckedListener;
import com.lifedrained.prepjournal.front.renders.CheckBoxColumnRender;
import com.lifedrained.prepjournal.front.renders.PropertyRender;
import com.lifedrained.prepjournal.front.renders.ScheduleDateRender;
import com.lifedrained.prepjournal.front.renders.ScheduleTimeRender;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;
import java.util.function.Consumer;
public class CustomGrid<T extends BaseEntity,RENDER extends ComponentRenderer<Component,T>> extends Grid<T> {
    public CustomGrid(Class<T> clazz, List<PropertyRender<T,RENDER>> list, OnCheckedListener<T> listener, String returnViewId){
        super(clazz , false );
        addColumn(new CheckBoxColumnRender<T>(listener, returnViewId)).setAutoWidth(true).setClassName(LumoUtility.Flex.AUTO);
        list.forEach(new Consumer<PropertyRender<T,RENDER>>() {
            @Override
            public void accept(PropertyRender<T,RENDER> propertyRender) {
                if(propertyRender.getRender() != null){
                    addColumn((propertyRender.getRender())).setHeader(propertyRender.getColumnName())
                            .setAutoWidth(true);
                }else{
                    addColumn(propertyRender.getPropetryName()).setHeader(propertyRender.getColumnName())
                            .setAutoWidth(true);
                }
            }
        });

        setRowsDraggable(true);
        addClassName(LumoUtility.Flex.AUTO);

    }
}
