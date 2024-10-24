package com.lifedrained.prepjournal.front.renders;

import com.lifedrained.prepjournal.Utils.NameProcessor;
import com.lifedrained.prepjournal.Utils.TimeUtils;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

public class LoginRoleRender<T extends BaseEntity> extends ComponentRenderer<Component, T> {
    public LoginRoleRender() {
        super(new SerializableFunction<T, Component>() {
            @Override
            public Component apply(T t) {
                return new Text(NameProcessor.convertRoleToReadable(((LoginEntity)t).getRole()));
            }
        });
    }
}
