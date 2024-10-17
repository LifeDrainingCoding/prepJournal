package com.lifedrained.prepjournal.front.views;

import com.lifedrained.prepjournal.front.interfaces.CRUDControl;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;

@Getter
public class ControlButtons<T extends BaseEntity> extends HorizontalLayout {
    private final CustomButton delete,update,create;
    public ControlButtons(CRUDControl crudControl, List<String> names){
        super();

        setWidthFull();

        addClassNames(BorderColor.CONTRAST_20,Border.BOTTOM);

        setAlignItems(Alignment.CENTER);

        create = new CustomButton(names.get(0), new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                crudControl.onCreate(getId().get());
            }
        });

        update = new CustomButton(names.get(1), new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                crudControl.onUpdate(getId().get());
            }
        });

        delete = new CustomButton(names.get(2), new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                crudControl.onDelete(getId().get());
            }
        });

        delete.setTheme(ButtonVariant.LUMO_ERROR);

        update.setEnabled(false);
        delete.setEnabled(false);

        add(create,update,delete);
    }
    private void toggleButtons(boolean switcher){
        delete.setEnabled(switcher);
        update.setEnabled(switcher);
    }

    public void checkButtons (LinkedHashMap<String, T> map){
        boolean isSelectedEmpty = map.isEmpty();
        if(!isSelectedEmpty){
            toggleButtons(true);
        }else {
            toggleButtons(false);
        }
    }
}
