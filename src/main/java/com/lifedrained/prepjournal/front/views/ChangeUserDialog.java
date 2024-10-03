package com.lifedrained.prepjournal.front.views;

import com.google.common.collect.Lists;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.front.views.widgets.RowWithComboBox;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

@Getter
public class ChangeUserDialog extends Dialog  {
    private RowWithTxtField name, login, password;
    private RowWithComboBox role;

    public ChangeUserDialog(OnConfirmDialogListener onConfirmDialogListener,String[] data){
        super();
        CustomButton ok, deny;
        name = new RowWithTxtField(data[0]);
        login = new RowWithTxtField(data[1]);
        password = new RowWithTxtField(data[2]);
        role =  new RowWithComboBox(data[3], Lists.newArrayList(RoleConsts.ADMIN,RoleConsts.USER));

        ok = new CustomButton("Сохранить", new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                if (Arrays.stream(getDataFromFields()).anyMatch(new Predicate<String>() {
                    @Override
                    public boolean test(String string) {
                        return string.isEmpty();
                    }
                })){
                    new Notification("Не все поля заполнены!",
                            (int)Duration.ofSeconds(5).toMillis())
                    {{
                        open();
                    }};

                    return;
                }
                close();
                onConfirmDialogListener.onConfirm(getDataFromFields());

            }
        });
        deny = new CustomButton("Отмена", new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                close();
            }
        });
        deny.setTheme(ButtonVariant.LUMO_ERROR);



        getHeader().add(new VerticalLayout(name, login, password, role));
        getFooter().add(deny,ok);
    }
    public String[] getDataFromFields(){
        return new String[] {
                login.getFieldText(),
                password.getFieldText(),
                role.getBoxValue(),
                name.getFieldText()
        };
    }
}
