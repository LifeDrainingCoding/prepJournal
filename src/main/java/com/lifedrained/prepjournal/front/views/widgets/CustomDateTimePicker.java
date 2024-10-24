package com.lifedrained.prepjournal.front.views.widgets;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CustomDateTimePicker extends DateTimePicker {
    public CustomDateTimePicker(){
        setLabel("Выборка даты");
        DatePicker.DatePickerI18n russianDate = new DatePicker.DatePickerI18n();
        russianDate.setDateFormat("dd:MM:yyyy");
        russianDate.setMonthNames(List.of("Январь", "Февраль","Март",
                "Апрель","Май","Июнь","Июль","Август","Сентябрь",
                "Октябрь","Ноябрь","Декабрь") );
        russianDate.setWeekdays(List.of("Воскресенье","Понедельник","Вторник","Среда",
                "Четверг","Пятница","Суббота"));
        russianDate.setWeekdaysShort(List.of("Вс","Пн","Вт","Ср","Чт",
                "Пт","Сб"));
        russianDate.setToday("Сегодня");
        russianDate.setCancel("Отмена");
        setDatePickerI18n(russianDate);
        setStep(Duration.ofMinutes(5));
        setMin(LocalDateTime.now());
        setMax(LocalDateTime.now().plusMonths(24));
    }
    public Date getDate(){
        return Date.from(getValue().atZone(ZoneId.systemDefault()).toInstant());
    }
    public Time getTime(){
        return Time.valueOf(getValue().toLocalTime());
    }
}
