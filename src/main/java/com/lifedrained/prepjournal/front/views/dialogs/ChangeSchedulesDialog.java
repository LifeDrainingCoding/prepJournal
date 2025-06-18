package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.front.views.filters.EntityFilters;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.widgets.RowDateTimePicker;
import com.lifedrained.prepjournal.front.views.widgets.RowWithComboBox;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.SubjectRepo;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.entities.SubjectEntity;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class ChangeSchedulesDialog extends BaseDialog<Object> {
    private static final Logger log = LogManager.getLogger(ChangeSchedulesDialog.class);
    private RowWithTxtField    theme, master ;
    private RowWithComboBox<SubjectEntity> subject;

    private RowDateTimePicker datePicker;

    private final SubjectRepo subjectRepo;
    private final LoginRepo loginRepo;

    private final CurrentSession session;

    public ChangeSchedulesDialog (OnConfirmDialogListener<Object> confirmDialogListener,
                                  List<Object> fieldValues, CurrentSession session){
        super(confirmDialogListener, fieldValues);

        subjectRepo = ContextProvider.getBean(SubjectRepo.class);
        loginRepo = ContextProvider.getBean(LoginRepo.class);


        this.session = session;

        subject = new RowWithComboBox<>("Выберите дисциплину", subjectRepo.findAll(),
                SubjectEntity::getName, EntityFilters.SUBJECT.get());
        subject.getBody().addValueChangeListener(e -> {
            master.getBody().setValue(e.getValue().getMaster().getName());
            log.info("changed master: {}", master.getBody().getValue());
        });





        master =  new RowWithTxtField(((String) fieldValues.get(0)));
        master.getBody().setReadOnly(true);
        if (subject.getCBoxValue() != null){
            master.getBody().setValue(subject.getCBoxValue().getMaster().getName());
        }



        datePicker = new RowDateTimePicker("Измените дату если нужно: ");
        if(fieldValues.get(1) instanceof String){
            datePicker.getDateTimePicker().setValue(DateUtils.asLocalDateTime(new Date()));
        }else if(fieldValues.get(1) instanceof Date date){
            datePicker.getDateTimePicker().setValue(DateUtils.asLocalDateTime(date));
        }


        theme = new RowWithTxtField(((String) ChangeSchedulesDialog.this.fieldValues.get(2))){{
            setLabelWidth("200px");
        }};
            getHeader().add(new VerticalLayout(subject,master,datePicker,theme));

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
                add(subject.getCBoxValue());
                add(subject.getCBoxValue().getMaster());
                add("null");
                add(theme.getFieldText());
            }};
        }
        return new ArrayList<>(){{
            add(subject.getCBoxValue());
            add(subject.getCBoxValue().getMaster());
            add(DateUtils.asDate(datePicker.getDateTimePicker().getValue()));
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
