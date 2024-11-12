package com.lifedrained.prepjournal.front.views.widgets;

import com.lifedrained.prepjournal.front.i18n.RussianDate;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


public class CustomDateTimePicker extends DateTimePicker implements HasDateTime {
    public CustomDateTimePicker(){
        setLabel("Выборка даты");

        setDatePickerI18n(new RussianDate());
        setStep(Duration.ofMinutes(5));
        setMin(LocalDateTime.now());
        setMax(LocalDateTime.now().plusMonths(24));
    }

    @Override
    public Date getDate(){
        return Date.from(getValue().atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public Time getTime(){
        return Time.valueOf(getValue().toLocalTime());
    }
}
