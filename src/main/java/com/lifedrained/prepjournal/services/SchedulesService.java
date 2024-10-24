package com.lifedrained.prepjournal.services;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lifedrained.prepjournal.Utils.NameProcessor;
import com.lifedrained.prepjournal.repo.SchedulesRepo;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.repo.entities.VisitorEntity;
import lombok.Getter;
import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Predicate;

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

    public List<ScheduleEntity> findAllByGroups(List<String> groups){
        List<ScheduleEntity> all =getRepo().findAll();
        all.removeIf(new Predicate<ScheduleEntity>() {
            @Override
            public boolean test(ScheduleEntity entity) {
                List<VisitorEntity> visitorEntities = getVisitorsForSchedule(entity);
                return visitorEntities.stream().noneMatch(new Predicate<VisitorEntity>() {
                    @Override
                    public boolean test(VisitorEntity visitorEntity) {

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
    public List<VisitorEntity> getVisitorsForSchedule(ScheduleEntity entity){
        Type listType = new TypeToken<List<VisitorEntity>>(){}.getType();
        return new Gson().fromJson(entity.getJsonVisitors(),listType);
    }
}
