package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.widgets.RowDatePicker;
import com.lifedrained.prepjournal.front.views.widgets.RowWithIntField;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.List;

public class ChangeGlobalVisitorDialog extends  BaseDialog<Object>{
    private static final Logger log = LogManager.getLogger(ChangeGlobalVisitorDialog.class);
    private final RowWithTxtField name, group, age, linkedMasterName, speciality, notes;
    private RowWithIntField visitedSchedulesN;
    private final RowDatePicker birthDate;


    public ChangeGlobalVisitorDialog(OnConfirmDialogListener<Object> confirmListener, List<Object> fieldValues) {
        super(confirmListener, fieldValues);

        name = new RowWithTxtField((String) fieldValues.get(0));

        Object dateOrString = fieldValues.get(1);

        if(dateOrString instanceof Date date ){
            birthDate =  new RowDatePicker("Измените дату: ", date );
        }else if (dateOrString instanceof String date) {
            birthDate = new RowDatePicker(date, new Date());
        }else {
            birthDate = new RowDatePicker("ОШИБКА ПРИ ПОЛУЧЕНИИ ДАТЫ, СООБЩИТЕ АДМИНИСТРАЦИИ!",new Date());
        }


        age = new RowWithTxtField((String) fieldValues.get(2));
        linkedMasterName =  new RowWithTxtField((String) fieldValues.get(3) );
        speciality = new RowWithTxtField((String) fieldValues.get(4));
        group =  new RowWithTxtField((String) fieldValues.get(5));
        Object numOfVisits = fieldValues.get(6);

        switch (numOfVisits) {
            case null -> {
                visitedSchedulesN = new RowWithIntField("ОШИБКА ПРИ ПОЛУЧЕНИИ КОЛ-ВА ПОСЕЩЕННЫХ ЗАНЯТИЙ, СООБЩИТЕ АДМИНИСТРАЦИИ!");
                log.error("Количество посещенных занятий нулл");
            }
            case Integer visitsNum -> {
                visitedSchedulesN = new RowWithIntField("Кол-во посещенных занятий за учебный год: " + visitsNum);
                visitedSchedulesN.setValue(visitsNum);
            }
            case String visitsName -> {
                visitedSchedulesN = new RowWithIntField(visitsName);
                visitedSchedulesN.setValue(0);
            }
            default -> {
                log.error("кол-во посещенных занятий не число и не строка {}", numOfVisits.toString());
            }
        }
        notes =  new RowWithTxtField((String) fieldValues.get(7));

        getHeader().add(new VerticalLayout(name,birthDate, age , linkedMasterName,
                speciality,group, visitedSchedulesN, notes));
    }

    @Override
    public List<Object> getDataFromFields() {
        return List.of(name.getFieldText(),birthDate.getDatePicker().getDate(),
                age.getFieldText() , linkedMasterName.getFieldText(),
                speciality.getFieldText(),group.getFieldText(), visitedSchedulesN.getValue(),
                notes.getFieldText());
    }

    @Override
    protected void setButtonListeners() {
        ok.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if (isFieldsEmpty(getDataFromFields())){
                    return;
                }
                String age = (String) getDataFromFields().get(2);

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
