package com.lifedrained.prepjournal.services;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lifedrained.prepjournal.Utils.NameProcessor;
import com.lifedrained.prepjournal.repo.SchedulesRepo;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.repo.entities.ScheduleVisitorEntity;
import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import static com.lifedrained.prepjournal.Utils.TimeUtils.convertToInt;

@Service
public class SchedulesService {
    private static final Logger log = LogManager.getLogger(SchedulesService.class);
    private final SchedulesRepo schedulesRepo;
    public SchedulesService (SchedulesRepo schedulesRepo){

        this.schedulesRepo = schedulesRepo;

    }

    public SchedulesRepo getRepo() {
        return schedulesRepo;
    }

    public List<ScheduleEntity> findDatesBetween(String start , String end){

        Date dateStart = NameProcessor.getDateFromString(start);
        Date dateEnd =  NameProcessor.getDateFromString(end);

        return schedulesRepo.findAllByDateBetween(dateStart ,dateEnd);
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
    public List<ScheduleEntity> findTimeBetween(String hrsStart , String minsStart, String hrsEnd, String minsEnd){

        int startTime, endTime;
        startTime = convertToInt(hrsStart,minsStart);
        endTime = convertToInt(hrsEnd, minsEnd);

        return schedulesRepo.findAllByScheduleTimeBetween(startTime, endTime);
    }
    public List<ScheduleEntity> findAllByGroups(List<String> groups){
        List<ScheduleEntity> all =getRepo().findAll();
        all.removeIf(new Predicate<ScheduleEntity>() {
            @Override
            public boolean test(ScheduleEntity entity) {
                List<ScheduleVisitorEntity> visitorEntities = getVisitorsForSchedule(entity);
                return visitorEntities.stream().noneMatch(new Predicate<ScheduleVisitorEntity>() {
                    @Override
                    public boolean test(ScheduleVisitorEntity visitorEntity) {

                        return groups.stream().anyMatch(new Predicate<String>() {
                            @Override
                            public boolean test(String s) {
                                Type type = new TypeToken<GlobalVisitor>(){}.getType();
                                GlobalVisitor globalVisitor = new Gson().fromJson(visitorEntity.getJsonGlobalEntity(),type);
                                return globalVisitor.getGroup().contains(s);
                            }
                        });
                    }
                });
            }
        });
        return all;
    }
    public List<ScheduleVisitorEntity> getVisitorsForSchedule(ScheduleEntity entity){
        Type listType = new TypeToken<List<ScheduleVisitorEntity>>(){}.getType();
        return new Gson().fromJson(entity.getJsonVisitors(),listType);
    }
}
