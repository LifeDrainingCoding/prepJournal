package com.lifedrained.prepjournal.data;

import com.lifedrained.prepjournal.Utils.SerializationUtils;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import lombok.Getter;

import java.util.List;

public class StatItem {
    @Getter
    private String masterName;
    @Getter
    private int totalHours = 0, visitedHours = 0, diff;
    @Getter
    private List<ScheduleEntity> schedules;

    public StatItem( String masterName, List<ScheduleEntity> schedules) {
        this.masterName = masterName;
        this.schedules = schedules;
        schedules.forEach(schedule -> {
            List<Visit> visits = SerializationUtils.toListVisits(schedule.getJsonVisitors());
            int duration = schedule.getHours();

            totalHours = totalHours + duration*visits.size();
            System.out.println(visits.size());
            visits.forEach(visit -> {
                if (visit.getIsVisited()){
                    visitedHours = visitedHours + duration;
                }
            });
        });
        diff = totalHours - visitedHours;

    }

}
