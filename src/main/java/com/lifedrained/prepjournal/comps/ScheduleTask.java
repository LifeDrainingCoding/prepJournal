package com.lifedrained.prepjournal.comps;

import com.lifedrained.prepjournal.repo.GroupsRepo;
import com.lifedrained.prepjournal.repo.SchedulesRepo;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.lifedrained.prepjournal.services.SchedulesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class ScheduleTask {
    public static final long ONE_MINUTE = 60000, TWO_MINUTE = ONE_MINUTE * 2;
    private static final Logger log = LogManager.getLogger(ScheduleTask.class);
    private final GlobalVisitorService globalVisitorService;
    private final GroupsRepo groupsRepo;
    private final SchedulesRepo schedulesRepo;

    public ScheduleTask( GlobalVisitorService globalVisitorService,
                        GroupsRepo groupsRepo) {
        this.groupsRepo = groupsRepo;
        schedulesRepo = ContextProvider.getBean(SchedulesRepo.class);
        this.globalVisitorService = globalVisitorService;
    }

    @Scheduled(fixedRate = ONE_MINUTE)
    public void schedule(){
        List<ScheduleEntity> scheduleEntities = schedulesRepo.findAll();
        scheduleEntities.forEach(entity -> {
            if(!entity.isExecuted()){
                long scheduleDate, now;
                scheduleDate = entity.getDate().getTime() + Duration.ofMinutes(entity.getHours()*45).toMillis();
                now = System.currentTimeMillis();
                if(scheduleDate <= now){
                    entity.setExecuted(true);
                    schedulesRepo.save(entity);
                }
            }
        });
    }
    @Scheduled(fixedRate = TWO_MINUTE)
    private void checkGroupsForExistence() {
        groupsRepo.findAll().forEach(group -> {
            boolean isExist = globalVisitorService.getRepo().existsByGroup(group);
            if(!isExist){
                groupsRepo.delete(group);
            }
        });
    }
}
