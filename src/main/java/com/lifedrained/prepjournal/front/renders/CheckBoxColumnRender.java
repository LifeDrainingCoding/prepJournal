package com.lifedrained.prepjournal.front.renders;

import com.lifedrained.prepjournal.Utils.KeyGen;
import com.lifedrained.prepjournal.front.interfaces.OnCheckedListener;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

public class CheckBoxColumnRender<T extends BaseEntity> extends ComponentRenderer<Component,T> {
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
}
