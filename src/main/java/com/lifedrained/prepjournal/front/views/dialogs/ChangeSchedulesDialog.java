package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.front.views.filters.EntityFilters;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.widgets.RowDateTimePicker;
import com.lifedrained.prepjournal.front.views.widgets.RowWithComboBox;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class ChangeSchedulesDialog extends BaseDialog<Object> {
    private RowWithTxtField scheduleName,  duration, theme ;
    private RowWithComboBox<LoginEntity> masterName;
    private RowDateTimePicker datePicker;

    private final CurrentSession session;

    public ChangeSchedulesDialog (OnConfirmDialogListener<Object> confirmDialogListener,
                                  List<Object> fieldValues, CurrentSession session){
        super(confirmDialogListener, fieldValues);

        this.session = session;

        scheduleName =  new RowWithTxtField(((String) fieldValues.get(0))){{
            setLabelWidth("200px");
        }};
        masterName =  new RowWithComboBox<>("Выберите преподавателя", ((List<LoginEntity>) fieldValues.get(1)),
                LoginEntity::getName, EntityFilters.LOGIN.get()){{
            setLabelWidth("200px");
        }};
        datePicker = new RowDateTimePicker("Измените дату если нужно: ");
        if(fieldValues.get(2) instanceof String){
            datePicker.getDateTimePicker().setValue(DateUtils.asLocalDateTime(new Date()));
        }else if(fieldValues.get(2) instanceof Date){
            Date date = (((Date) fieldValues.get(2)));
            datePicker.getDateTimePicker().setValue(DateUtils.asLocalDateTime(date));
        }

        duration = new RowWithTxtField(((String) ChangeSchedulesDialog.this.fieldValues.get(3))){{
            getBody().setPattern("[0-9]*");
            setLabelWidth("200px");
        }};
        theme = new RowWithTxtField(((String) ChangeSchedulesDialog.this.fieldValues.get(4))){{
            setLabelWidth("200px");
        }};
        if (session.getRole().equals(RoleConsts.ADMIN.value)){
            getHeader().add(new VerticalLayout(scheduleName,masterName,datePicker,duration,theme));
        }else {
            getHeader().add(new VerticalLayout(scheduleName,datePicker,duration,theme));
        }
    }

    @Override
    public void setButtonListeners() {
        ok.addClickListener((ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
           if(isFieldsEmpty(getDataFromFields())){
               return;
           }
            close();
            confirmListener.onConfirm(getDataFromFields());
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
                if (session.getRole().equals(RoleConsts.ADMIN.value)){
                    add(masterName.getCBoxValue());
                }else {
                    add(session.getEntity());
                }
                add("null");
                add(duration.getFieldText());
                add(theme.getFieldText());
            }};
        }
        return new ArrayList<>(){{
            add(scheduleName.getFieldText());
            if (session.getRole().equals(RoleConsts.ADMIN.value)){
                add(masterName.getCBoxValue());
            }else {
                add(session.getEntity());
            }
            add(DateUtils.asDate(datePicker.getDateTimePicker().getValue()));
            add(duration.getFieldText());
            add(theme.getFieldText());
        }};
    }

    @Override
    public boolean isFieldsEmpty(List<Object> data) {

        return data.stream().anyMatch(new Predicate<Object>() {
            @Override
            public boolean test(Object o) {
                if (o instanceof String s) {
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
