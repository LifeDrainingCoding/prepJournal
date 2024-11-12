package com.lifedrained.prepjournal.front.views.widgets;

import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.front.i18n.RussianDate;
import com.vaadin.flow.component.datepicker.DatePicker;

import java.sql.Time;
import java.util.Date;

public class CustomDatePicker extends DatePicker implements HasDateTime {
    public CustomDatePicker() {
        setLabel("Выборка даты");
        setI18n(new RussianDate());
    }

    @Override
    public Date getDate() {
        return DateUtils.asDate(getValue());
    }

    @Override
    public Time getTime() {
        return HasDateTime.super.getTime();
    }
}
