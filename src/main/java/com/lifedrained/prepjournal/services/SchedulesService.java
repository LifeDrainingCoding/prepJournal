package com.lifedrained.prepjournal.services;

import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.SchedulesRepo;
import com.lifedrained.prepjournal.repo.SubjectRepo;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;

import com.lifedrained.prepjournal.repo.entities.SubjectEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.compress.utils.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;


@Service
@AllArgsConstructor
public class SchedulesService {
    private static final Logger log = LogManager.getLogger(SchedulesService.class);
    @Getter
    private SchedulesRepo repo;
    private LoginRepo loginRepo;
    private SubjectRepo subjectRepo;



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
    @Transactional
    public void saveDialogSchedule(ScheduleEntity entity, List<Object> returnData){
        SubjectEntity subjectEntity = subjectRepo.findById(((SubjectEntity) returnData.get(0)).getId()).get();
        LoginEntity loginEntity = loginRepo.findById(((LoginEntity) returnData.get(1)).getId()).get();
        entity.setSubject(subjectEntity);
        entity.setMaster(loginEntity);
        entity.setDate((Date) returnData.get(2));
        entity.setTheme((String) returnData.get(3));
        repo.save(entity);
    }

    @Transactional
    public SubjectEntity getSubjectSchedule(ScheduleEntity entity){
        Hibernate.initialize(entity.getSubject());
        return entity.getSubject();
    }

    @Transactional
    public LoginEntity getLoginSchedule(ScheduleEntity entity){
        Hibernate.initialize(entity.getMaster());
        return entity.getMaster();
    }


}
