package com.lifedrained.prepjournal.comps;

import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.lifedrained.prepjournal.services.SchedulesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

@Component
public class ScheduleTask {
    public static final long pollRate = 60000;
    private static final Logger log = LogManager.getLogger(ScheduleTask.class);
    private final SchedulesService service;
    private final GlobalVisitorService globalVisitorService;

    public ScheduleTask(SchedulesService service, GlobalVisitorService globalVisitorService) {
        this.service = service;
        this.globalVisitorService = globalVisitorService;
    }

    @Scheduled(fixedRate = pollRate)
    public void schedule(){
        List<ScheduleEntity> scheduleEntities = service.repo.findAll();
        scheduleEntities.forEach(new Consumer<ScheduleEntity>() {
            @Override
            public void accept(ScheduleEntity entity) {
                if(!entity.isExecuted()){
                    long scheduleDate, now;
                    scheduleDate = entity.getDate().getTime() + Duration.ofMinutes(entity.getDuration()).toMillis();
                    now = System.currentTimeMillis();
                    if(scheduleDate <= now){
                        entity.setExecuted(true);
                        service.repo.save(entity);
                    }
                }
            }
        });
    }
    @Scheduled(cron = "0 0 0 1 1 ?")
    public void clearNumVisits() {
        globalVisitorService.getRepo().findAll().forEach(new Consumer<>() {
            @Override
            public void accept(GlobalVisitor globalVisitor) {

            }
        });
        //todo допилить выполнение
    }
}
