package com.lifedrained.prepjournal.consts;

import java.util.ArrayList;
import java.util.List;

public interface StringConsts {
     List<String> AccCRUDNames = List.of("Добавить аккаунт",
            "Обновить данные выбранных аккаунтов",
            "Удалить выбранные аккаунты");
     List<String> SchedulesCRUDNames = List.of("Добавить занятие",
            "Обновить данные у выбранных занятий",
            "Удалить выбранные занятия");

    List<String> AccFieldNames = List.of(new String[]{
           "Имя: ",
           "Логин: ",
           "Пароль: ",
           "Права: "
   });
    List<String> SchedulesFieldNames = List.of(new String[]{
           "Hазвание занятия: ",
           "Имя преподавателя: ",
           "Дата проведения занятия: ",
           "Длительность занятия(Минуты): ",
           "Время начала занятия: ",
           "Посетители: "
   });
    interface PropertyNames{
        String SCHEDULE_DATE = "date";
        String SCHEDULE_TIME = "scheduleTime";
    }

}
