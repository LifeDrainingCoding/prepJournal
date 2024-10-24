package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ChangeGlobalVisitorDialog extends  BaseDialog<String>{
    private static final Logger log = LogManager.getLogger(ChangeGlobalVisitorDialog.class);
    private RowWithTxtField name, group, age;
    public ChangeGlobalVisitorDialog(OnConfirmDialogListener<String> confirmListener, List<String> fieldNames) {
        super(confirmListener, fieldNames);

        name = new RowWithTxtField(fieldNames.get(0));
        group =  new RowWithTxtField(fieldNames.get(1));
        age = new RowWithTxtField(fieldNames.get(2));
        getHeader().add(new VerticalLayout(name,group,age));
    }

    @Override
    public List<String> getDataFromFields() {
        return List.of(name.getFieldText(), group.getFieldText(), age.getFieldText());
    }

    @Override
    protected void setButtonListeners() {
        ok.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if (isFieldsEmpty(getDataFromFields())){
                    return;
                }
                String age = getDataFromFields().get(2);
                try{
                    Integer.parseInt(age);
                }catch (NumberFormatException ex){
                    log.error("Error during parsing Global visitor age: "+age, ex);
                    Notify.error("Некорректно введен возраст!");
                    return;
                }
                close();
                confirmListener.onConfirm(getDataFromFields());
            }
        });
        deny.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                close();
            }
        });
    }

}
