package com.lifedrained.prepjournal.front.views;

import com.lifedrained.prepjournal.front.interfaces.UserControl;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import lombok.Getter;

import java.util.LinkedHashMap;

@Getter
public class UsersControlButtons extends HorizontalLayout {
    private final CustomButton delete,update,create;
    public UsersControlButtons(UserControl userControl){
        super();

        setWidthFull();

        addClassNames(BorderColor.CONTRAST_20,Border.BOTTOM);

        setAlignItems(Alignment.CENTER);

        create = new CustomButton("Добавить аккаунт", new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                userControl.onCreate();
            }
        });

        update = new CustomButton("Изменить свойства выбранных аккаунтов", new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                userControl.onUpdate();
            }
        });

        delete = new CustomButton("Удалить выбранных пользователей", new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                userControl.onDelete();
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

    public void checkButtons (LinkedHashMap<String, LoginEntity> map){
        boolean isSelectedEmpty = map.isEmpty();
        if(!isSelectedEmpty){
            toggleButtons(true);
        }else {
            toggleButtons(false);
        }
    }
}
