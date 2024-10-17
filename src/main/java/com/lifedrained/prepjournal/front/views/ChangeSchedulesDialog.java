package com.lifedrained.prepjournal.front.views;

import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ChangeSchedulesDialog extends BaseDialog<List<String>> {
    private ArrayList<RowWithTxtField> list;
    public ChangeSchedulesDialog (OnConfirmDialogListener<List<String>> confirmDialogListener,
                                  List<String> fieldNames){
        super(confirmDialogListener);
        list = new ArrayList<>();
        fieldNames.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                RowWithTxtField rowWithTxtField =  new RowWithTxtField(s);
                list.add(rowWithTxtField);
            }
        });

        list.forEach(new Consumer<RowWithTxtField>() {
            @Override
            public void accept(RowWithTxtField rowWithTxtField) {
                rowWithTxtField.setLabelWidth("200px");
                add(rowWithTxtField);
            }
        });
    }

    @Override
    public List<String> getDataFromFields() {
        ArrayList<String> arrayList = new ArrayList<>();
        list.forEach(new Consumer<RowWithTxtField>() {
            @Override
            public void accept(RowWithTxtField rowWithTxtField) {
                arrayList.add(rowWithTxtField.getFieldText());
            }
        });
        return arrayList;
    }

    @Override
    public void setButtonListeners() {
        ok.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if (isFieldsEmpty()){

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

    @Override
    protected boolean isFieldsEmpty() {
        return getDataFromFields().stream().anyMatch(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.isEmpty();
            }
        });
    }
}
