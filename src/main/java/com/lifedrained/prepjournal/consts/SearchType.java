package com.lifedrained.prepjournal.consts;

import java.util.List;

public enum SearchType {
    BY_DATE(1), BY_DATE_BETWEEN(2), BY_MASTER_NAME(3),
    BY_DURATION(4), BY_DURATION_BETWEEN(5), ALL(100);

    public final int value;
    public final String string;

        SearchType(int value){
        this.value = value;
        switch (value){
            case 1: string = "По дате:"; break;
            case 2: string = "По диапазону дат:"; break;
            case 3: string = "По Фамилии/Имени/Отчеству/ФИО:"; break;
            case 4: string = "По длительности занятия:"; break;
            case 5: string = "По диапазону длительностей занятий:"; break;
            case 100: string = "Все"; break;
            default: string = null;
        }
    }
    public static List<SearchType> getTypes(){
            return List.of(BY_DATE,BY_DATE_BETWEEN,
                    BY_MASTER_NAME,BY_DURATION,
                    BY_DURATION_BETWEEN);
    }
}
