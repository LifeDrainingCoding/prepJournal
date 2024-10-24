package com.lifedrained.prepjournal.front.views.dialogs;

import com.google.common.collect.Lists;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.widgets.RowWithComboBox;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;

@Getter
public class ChangeUserDialog extends BaseDialog<String> {
    private final RowWithTxtField name, login, password;
    private final RowWithComboBox role;

    public ChangeUserDialog(OnConfirmDialogListener<String> onConfirmDialogListener, List<String> fieldNames){
        super(onConfirmDialogListener, fieldNames);

        name = new RowWithTxtField(fieldNames.get(0));
        login = new RowWithTxtField(fieldNames.get(1));
        password = new RowWithTxtField(fieldNames.get(2));
        role =  new RowWithComboBox(fieldNames.get(3), Lists.newArrayList(RoleConsts.ADMIN,RoleConsts.USER));


        getHeader().add(new VerticalLayout(name, login, password, role));

    }

    @Override
    public List<String> getDataFromFields(){

        return List.of(  login.getFieldText(),
                password.getFieldText(),
                role.getBoxValue(),
                name.getFieldText());
    }

    @Override
    public void setButtonListeners() {
        ok.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                if (isFieldsEmpty(getDataFromFields())){
                    return;
                }
                close();
                confirmListener.onConfirm(getDataFromFields());

            }
        });
        deny.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                close();
            }
        });
    }

}
