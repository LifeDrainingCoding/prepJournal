package com.lifedrained.prepjournal.front.views.dialogs;

import com.google.common.collect.Lists;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.widgets.RowWithRoleComboBox;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChangeUserDialog extends BaseDialog<String> {
    private final RowWithTxtField name, login, password;
    private final RowWithRoleComboBox role;

    public ChangeUserDialog(OnConfirmDialogListener<String> onConfirmDialogListener, List<String> fieldNames){
        super(onConfirmDialogListener, fieldNames);

        name = new RowWithTxtField(fieldNames.get(0));
        login = new RowWithTxtField(fieldNames.get(1));
        password = new RowWithTxtField(fieldNames.get(2));

        ArrayList<RoleConsts> roles = Lists.newArrayList(RoleConsts.values());
        CurrentSession session = ContextProvider.getBean(CurrentSession.class);

        if(session.getRoleConst() != RoleConsts.ADMIN){
            roles.removeIf(roleConst -> roleConst.ordinal()>session.getRoleConst().ordinal());
            roles.removeIf(roleConst -> roleConst.ordinal()==session.getRoleConst().ordinal());
        }
        role =  new RowWithRoleComboBox(fieldNames.get(3),roles );


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
        ok.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            if (isFieldsEmpty(getDataFromFields())){
                return;
            }
            close();
            confirmListener.onConfirm(getDataFromFields());

        });
        deny.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> close());
    }

}
