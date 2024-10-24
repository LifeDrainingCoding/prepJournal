package com.lifedrained.prepjournal.consts;

import java.util.ArrayList;
import java.util.List;

public interface StringConsts {
    String DELIM = "#$";
     List<String> AccCRUDNames = List.of("Добавить аккаунт",
            "Обновить данные выбранных аккаунтов",
            "Удалить выбранные аккаунты");
     List<String> SchedulesCRUDNames = List.of("Добавить занятие",
            "Обновить данные у выбранных занятий",
            "Удалить выбранные занятия");
    List<String> VisitorCRUDNames = List.of("Добавить посетителя",
            "Обновить выбранных посетителей",
            "Удалить выбранных посетителей");
    List<String> VisitorFieldNames = List.of("ФИО: ", "Группы: ", "Возраст: ");

    List<String> AccFieldNames = List.of(new String[]{
           "ФИО: ",
           "Логин: ",
           "Пароль: ",
           "Права: "
   });
    List<Object> SchedulesFieldNames = List.of(
           "Hазвание занятия: ",
           "Имя преподавателя: ",
           "Дата проведения занятия: ",
           "Длительность занятия(Минуты): ",
           "Тема занятия: ",
           " "
   );


}
