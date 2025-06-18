package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.filters.EntityFilters;
import com.lifedrained.prepjournal.front.views.widgets.RowDatePicker;
import com.lifedrained.prepjournal.front.views.widgets.RowWithComboBox;
import com.lifedrained.prepjournal.front.views.widgets.RowWithIntField;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.lifedrained.prepjournal.repo.GroupsRepo;
import com.lifedrained.prepjournal.repo.entities.GroupEntity;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class ChangeGlobalVisitorDialog extends  BaseDialog<Object>{
    private static final Logger log = LogManager.getLogger(ChangeGlobalVisitorDialog.class);
    private final RowWithTxtField name, snils, passport;
    private final RowWithComboBox<GroupEntity> group;
    private final RowWithIntField course,enrollId,innId;
    private final RowDatePicker birthDateField, enrollDateField;
    private int age;

    private final GroupsRepo groupsRepo;

    public ChangeGlobalVisitorDialog(OnConfirmDialogListener<Object> confirmListener,
                                     List<Object> fieldValues) {
        super(confirmListener, fieldValues);

        groupsRepo = ContextProvider.getBean(GroupsRepo.class);

        name = new RowWithTxtField((String) fieldValues.get(0));

        Object dateOrString = fieldValues.get(1);

        if(dateOrString instanceof Date date ){
            birthDateField =  new RowDatePicker("Измените дату: ", date );
        }else if (dateOrString instanceof String date) {
            birthDateField = new RowDatePicker(date, new Date());
        }else {
            birthDateField = new RowDatePicker(
                    "ОШИБКА ПРИ ПОЛУЧЕНИИ ДАТЫ, СООБЩИТЕ АДМИНИСТРАЦИИ!",new Date());
        }

        if(fieldValues.get(2) instanceof Date date ){
            enrollDateField =  new RowDatePicker("Измените дату зачисления : ", date );
        }else {
            enrollDateField = new RowDatePicker("Измените дату зачисления :", new Date());
        }

        course = new RowWithIntField((String) fieldValues.get(3));
        course.getBody().setMin(1);
        course.getBody().setMax(5);


        group =  new RowWithComboBox<>((String) fieldValues.get(4), groupsRepo.findAll(),
                GroupEntity::getName, EntityFilters.GROUP.get());

        passport = new RowWithTxtField((String) fieldValues.get(5));

        enrollId = new RowWithIntField((String) fieldValues.get(6));

        innId = new RowWithIntField((String) fieldValues.get(7));

        snils = new RowWithTxtField((String) fieldValues.get(8));

        HorizontalLayout horizontalLayout = new HorizontalLayout(ok, deny);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.END);

        VerticalLayout layout = new VerticalLayout();
        layout.setAlignSelf(FlexComponent.Alignment.END);
        layout.setAlignItems(FlexComponent.Alignment.END);
        layout.add(name, birthDateField, enrollDateField, course
                , group, passport, enrollId, innId, snils, horizontalLayout);
        add(new Scroller(layout));

    }

    @Override
    public List<Object> getDataFromFields() {
        Date birthDate = birthDateField.getDatePicker().getDate(),
                enrollDate = enrollDateField.getDatePicker().getDate();
        age = (int) ChronoUnit.YEARS.between(
                DateUtils.asLocalDate(birthDate),
                LocalDate.now());

        return List.of(name.getFieldText(),birthDate,enrollDate
                ,age , course.getValue(),group.getCBoxValue(),
                passport.getFieldText(), enrollId.getValue(),innId.getValue(),snils.getFieldText()
                );
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
