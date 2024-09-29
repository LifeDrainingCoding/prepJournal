package com.lifedrained.prepjournal.front.lists.renders;

import com.lifedrained.prepjournal.repo.LoginEntity;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;

public class UsersRender extends ComponentRenderer<Component, LoginEntity> {
    public UsersRender() {
        super(new SerializableFunction<LoginEntity, Component>() {
            @Override
            public Component apply(LoginEntity entity) {

                HorizontalLayout layout = new HorizontalLayout();

                layout.setMargin(true);
                layout.setSpacing(true);
                layout.setPadding(true);

                return null;
            }
        });

    }
}
