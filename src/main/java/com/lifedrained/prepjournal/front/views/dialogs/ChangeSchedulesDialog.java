package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.widgets.RowDatePicker;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class ChangeSchedulesDialog extends BaseDialog<Object> {
    private RowWithTxtField scheduleName, masterName, duration, theme , description;
    private RowDatePicker datePicker;

    public ChangeSchedulesDialog (OnConfirmDialogListener<Object> confirmDialogListener,
                                  List<Object> fieldValues){
        super(confirmDialogListener, fieldValues);

        scheduleName =  new RowWithTxtField(((String) fieldValues.get(0))){{
            setLabelWidth("200px");
        }};
        masterName =  new RowWithTxtField(((String) fieldValues.get(1))){{
            setLabelWidth("200px");
        }};
        datePicker = new RowDatePicker("Измените дату если нужно: ");
        if(fieldValues.get(2) instanceof String){
            datePicker.getDateTimePicker().setValue(DateUtils.fromDateToLDT(new Date()));
        }else if(fieldValues.get(2) instanceof Date){
            Date date = (((Date) fieldValues.get(2)));
            datePicker.getDateTimePicker().setValue(DateUtils.fromDateToLDT(date));
        }

        duration = new RowWithTxtField(((String) fieldNames.get(3))){{
            getBody().setPattern("[0-9]*");
            setLabelWidth("200px");
        }};
        theme = new RowWithTxtField(((String) fieldNames.get(4))){{
            setLabelWidth("200px");
        }};

        getHeader().add(new VerticalLayout(scheduleName,masterName,datePicker,duration,theme));
    }

    @Override
    public void setButtonListeners() {
        ok.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
               if(isFieldsEmpty(getDataFromFields())){
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

    @Override
    public List<Object> getDataFromFields() {
        if(datePicker.getDateTimePicker().getValue() == null){
            return new ArrayList<>(){{
                add(scheduleName.getFieldText());
                add(masterName.getFieldText());
                add("null");
                add(duration.getFieldText());
                add(theme.getFieldText());
            }};
        }
        return new ArrayList<>(){{
            add(scheduleName.getFieldText());
            add(masterName.getFieldText());
            add(DateUtils.fromLDTtoDate(datePicker.getDateTimePicker().getValue()));
            add(duration.getFieldText());
            add(theme.getFieldText());
        }};
    }

    @Override
    public boolean isFieldsEmpty(List<Object> data) {

        return data.stream().anyMatch(new Predicate<Object>() {
            @Override
            public boolean test(Object o) {
                if (o instanceof String) {
                    String s = (String) o;
                    if (s.isEmpty()){
                        Notify.error("Заполните все поля!");
                    }
                    if (s.equals("null")){
                        Notify.error("Введите корректную дату!");
                        return true;
                    }
                    return s.isEmpty();
                }
                return false;
            }
        });
    }
}
