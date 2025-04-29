package com.lifedrained.prepjournal.consts;

import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;

import java.util.List;

public class StrConsts {



    public static List<Object> getScheduleFieldValues(List<LoginEntity>  masters){
        List<Object> SchedulesFieldNames = List.of(
                "Hазвание занятия: ",
                masters,
                "Дата проведения занятия: ",
                "Длительность занятия(Минуты): ",
                "Тема занятия: ",
                " "
        );
        return SchedulesFieldNames;
    }
}
