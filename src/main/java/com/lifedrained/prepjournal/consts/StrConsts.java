package com.lifedrained.prepjournal.consts;

import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;

import java.util.List;

public class StrConsts {



    public static List<Object> getScheduleFieldValues(List<LoginEntity>  masters){
        List<Object> SchedulesFieldNames = List.of(
                "Преподаватель: ",
                masters,
                "Тема занятия: ",
                "Длительность занятия(академические часы): ",
                "Тема занятия: ",
                " "
        );
        return SchedulesFieldNames;
    }
}
