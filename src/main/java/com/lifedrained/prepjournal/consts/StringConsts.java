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

    List<String> GeneralCRUDNames = List.of("Добавить", "Обновить", "Удалить");
    List<Object> VisitorFieldNames = List.of("ФИО: ", "Дата рождения: ",
            "Дата зачисления: ", "Курс: ","Группа: ", "Паспорт: ",
            "Номер приказа: ", "ИНН: ", "снилс: "
             );


    List<String> AccFieldNames = List.of(new String[]{
           "ФИО: ",
           "Логин: ",
           "Пароль: ",
           "Права: "
   });


}
