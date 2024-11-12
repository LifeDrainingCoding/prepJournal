package com.lifedrained.prepjournal.services;

import com.lifedrained.prepjournal.repo.SchedulesRepo;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;

import lombok.Getter;
import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@Getter
public class SchedulesService {
    private static final Logger log = LogManager.getLogger(SchedulesService.class);
    public SchedulesRepo repo;
    public SchedulesService (SchedulesRepo schedulesRepo){

        this.repo = schedulesRepo;

    }



    public List<ScheduleEntity> findTimeBetween(String start, String end){
        String[] partsStart = start.split(":");
        String[] partsEnd = end.split(":");

        if(partsStart.length != 2 || partsEnd.length != 2){
            log.error("Entered time was incorrect expected time format for example: 12:30");
            return Lists.newArrayList();
        }

        String rawTimeStart = partsStart[0]+partsStart[1];
        String rawTimeEnd = partsEnd[0]+partsEnd[1];

        return null;

    }


}
