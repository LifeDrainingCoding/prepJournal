package com.lifedrained.prepjournal.front.lists.renders;

import com.lifedrained.prepjournal.Utils.KeyGen;
import com.lifedrained.prepjournal.Utils.NameProcessor;
import com.lifedrained.prepjournal.front.interfaces.OnCheckBoxPickedListener;
import com.lifedrained.prepjournal.front.views.widgets.CustomLabel;
import com.lifedrained.prepjournal.repo.LoginEntity;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;


import static com.vaadin.flow.theme.lumo.LumoUtility.*;


public class UsersRender extends ComponentRenderer<Component, LoginEntity> {
    public UsersRender(OnCheckBoxPickedListener listener) {
        super(new SerializableFunction<LoginEntity, Component>() {
            @Override
            public Component apply(LoginEntity entity) {

                CustomLabel id,login,name,password,role;
                Checkbox selected ;


                id  = new CustomLabel(String.valueOf(entity.getId()));
                id.setMaxWidth(40, Unit.PIXELS);
                id.setWidth(40,Unit.PIXELS);

                name = new CustomLabel(entity.getName());

                login = new CustomLabel(entity.getLogin());

                password = new CustomLabel(entity.getPassword());

                role = new CustomLabel(NameProcessor.convertRoleToReadable(entity.getRole()));





                HorizontalLayout layout = new HorizontalLayout();

                layout.addClassNames(
                        Margin.Vertical.NONE,
                        Padding.Vertical.XSMALL);

                layout.setMargin(true);
                layout.setSpacing(true);
                layout.setPadding(true);
                if(entity.getId()!=-1L){
                    selected = new Checkbox();
                    selected.setId(KeyGen.generateKey());
                    selected.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>>() {
                        @Override
                        public void valueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> event) {
                            listener.onChecked(String.valueOf(selected.getId()),entity, event.getValue());
                        }
                    });
                    selected.addClassNames(AlignSelf.CENTER, AlignContent.CENTER);
                    layout.add(id,name,login,password,role,selected);
                }else {
                    layout.add(id,name,login,password,role);
                }



                return layout;
            }
        });

    }

}
