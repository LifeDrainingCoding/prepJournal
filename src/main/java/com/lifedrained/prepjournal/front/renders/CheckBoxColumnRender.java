package com.lifedrained.prepjournal.front.renders;

import com.google.gson.Gson;
import com.lifedrained.prepjournal.Utils.KeyGen;
import com.lifedrained.prepjournal.Utils.SerializationUtils;
import com.lifedrained.prepjournal.data.Visit;
import com.lifedrained.prepjournal.front.interfaces.OnCheckedListener;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CheckBoxColumnRender<T> extends ComponentRenderer<Component,T> {
    public CheckBoxColumnRender(OnCheckedListener<T> listener, String reutrnViewId) {
        super(new SerializableFunction<T, Component>() {
            @Override
            public Component apply(T t) {
                Checkbox checkbox = new Checkbox();
                checkbox.setId(KeyGen.generateKey());
                checkbox.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>>() {
                    @Override
                    public void valueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> event) {
                        listener.onChecked(event.getSource().getId().get(),t, event.getValue(),reutrnViewId);
                    }
                });
                return checkbox;
            }
        });
    }
    public CheckBoxColumnRender(OnCheckedListener<T> listener, Object uid) {
        super(new SerializableFunction<T, Component>() {
            @Override
            public Component apply(T t) {
                Checkbox checkbox = new Checkbox();
                if (t instanceof GlobalVisitor globalVisitor){
                    Type type = SerializationUtils.getJsonType();
                    List<Visit> visits =  new ArrayList<>();
                    if (globalVisitor.getJsonVisits() != null && !globalVisitor.getJsonVisits().isEmpty()){
                        visits = new Gson().fromJson(globalVisitor.getJsonVisits(), type);
                    }
                    Visit visit = SerializationUtils.findByUid(visits, ((String) uid));
                    if (visit != null){
                        checkbox.setValue(visit.getIsVisited());
                    }

                }
                checkbox.setId(KeyGen.generateKey());
                checkbox.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>>() {
                    @Override
                    public void valueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> event) {
                        listener.onChecked(event.getSource().getId().get(),t, event.getValue(),(String) uid);
                    }
                });
                return checkbox;
            }
        });
    }
    public CheckBoxColumnRender(OnCheckedListener<T> listener, String reutrnViewId, String text) {
        super(new SerializableFunction<T, Component>() {
            @Override
            public Component apply(T t) {
                Checkbox checkbox = new Checkbox(text);
                checkbox.setId(KeyGen.generateKey());
                checkbox.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>>() {
                    @Override
                    public void valueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> event) {
                        listener.onChecked(event.getSource().getId().get(),t, event.getValue(),reutrnViewId);
                    }
                });
                return checkbox;
            }
        });
    }
}
