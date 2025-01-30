package com.lifedrained.prepjournal.front.pages.statistics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public enum SortTime {
    ALL_TIME("за все время"),
    LAST_DAY("за последний день"),LAST_WEEK("за последнюю неделю"),
    LAST_MONTH("за последний месяц"), LAST_YEAR("за последний год") ;
    public final String translation;
    public static final List<SortTime> LIST = List.of(SortTime.values());
    SortTime(String translation){
        this.translation = translation;
    }


    public LocalDateTime getStartDate(){
        LocalDateTime currentDate = LocalDateTime.now();
        List<LocalDateTime> dates = List.of(LocalDateTime.of(1970, Month.JANUARY,1,0,0,0),
                currentDate.minusDays(1), currentDate.minusWeeks(1),
                currentDate.minusMonths(1), currentDate.minusYears(1));
        return dates.get(LIST.indexOf(this));
    }

}
