package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.widgets.RowDatePicker;
import com.lifedrained.prepjournal.front.views.widgets.RowWithIntField;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class ChangeGlobalVisitorDialog extends  BaseDialog<Object>{
    private static final Logger log = LogManager.getLogger(ChangeGlobalVisitorDialog.class);
    private final RowWithTxtField name, group, linkedMasterName, speciality, notes;
    private final RowDatePicker birthDateField;
    private int age;


    public ChangeGlobalVisitorDialog(OnConfirmDialogListener<Object> confirmListener, List<Object> fieldValues) {
        super(confirmListener, fieldValues);

        name = new RowWithTxtField((String) fieldValues.get(0));

        Object dateOrString = fieldValues.get(1);

        if(dateOrString instanceof Date date ){
            birthDateField =  new RowDatePicker("Измените дату: ", date );
        }else if (dateOrString instanceof String date) {
            birthDateField = new RowDatePicker(date, new Date());
        }else {
            birthDateField = new RowDatePicker("ОШИБКА ПРИ ПОЛУЧЕНИИ ДАТЫ, СООБЩИТЕ АДМИНИСТРАЦИИ!",new Date());
        }


        linkedMasterName =  new RowWithTxtField((String) fieldValues.get(3) );
        speciality = new RowWithTxtField((String) fieldValues.get(4));
        group =  new RowWithTxtField((String) fieldValues.get(5));


        notes =  new RowWithTxtField((String) fieldValues.get(6));

        getHeader().add(new VerticalLayout(name, birthDateField, linkedMasterName,
                speciality,group, notes));
    }

    @Override
    public List<Object> getDataFromFields() {
        Date birthDate = birthDateField.getDatePicker().getDate();
        age = (int) ChronoUnit.YEARS.between(
                DateUtils.asLocalDate(birthDate),
                LocalDate.now());

        return List.of(name.getFieldText(),birthDate,
                age , linkedMasterName.getFieldText(),
                speciality.getFieldText(),group.getFieldText(),
                notes.getFieldText());
    }

    @Override
    protected void setButtonListeners() {
        ok.addClickListener( buttonClickEvent -> {
            if (isFieldsEmpty(getDataFromFields())){
                return;
            }
            close();
            confirmListener.onConfirm(getDataFromFields());
        });
        deny.addClickListener( buttonClickEvent -> close());
    }

}
