package com.lifedrained.prepjournal.front.i18n;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.util.List;

public class RussianDate extends DatePicker.DatePickerI18n {
    public RussianDate() {

        setDateFormat("dd:MM:yyyy");

        setMonthNames(List.of("Январь", "Февраль","Март",
                "Апрель","Май","Июнь","Июль","Август","Сентябрь",
                "Октябрь","Ноябрь","Декабрь") );

        setWeekdays(List.of("Воскресенье","Понедельник","Вторник","Среда",
                "Четверг","Пятница","Суббота"));

        setWeekdaysShort(List.of("Вс","Пн","Вт","Ср","Чт",
                "Пт","Сб"));

        setToday("Сегодня");

        setCancel("Отмена");

    }
}
