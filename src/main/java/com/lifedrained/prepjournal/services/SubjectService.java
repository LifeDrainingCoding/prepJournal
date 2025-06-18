package com.lifedrained.prepjournal.services;

import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.SubjectRepo;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.entities.PlanEntity;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.repo.entities.SubjectEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SubjectService {

    @Getter
    private SubjectRepo repo;
    private LoginRepo loginRepo;
    private SchedulesService schedulesService;
    private PlanService planService;

    @Transactional
    public SubjectEntity saveDialog(SubjectEntity subjectEntity, LoginEntity loginEntity, String name, int cabinet) {
        loginEntity = loginRepo.findById(loginEntity.getId()).get();

        subjectEntity.setMaster(loginEntity);
        subjectEntity.setName(name);
        subjectEntity.setCabinet(cabinet);

        Hibernate.initialize(loginEntity.getSubjects());

        loginEntity.getSubjects().remove(subjectEntity);
        loginEntity.getSubjects().add(subjectEntity);

        return subjectEntity;
    }
    @Transactional
    public void saveAll(List<SubjectEntity> subjectEntities) {
        for (SubjectEntity subjectEntity : subjectEntities) {
            LoginEntity loginEntity = loginRepo.findById(subjectEntity.getMaster().getId()).get();
            subjectEntity.setMaster(loginEntity);
        }
         repo.saveAll(subjectEntities);
    }

    @Transactional
    public void deleteCollapse(SubjectEntity subjectEntity) {
        subjectEntity = repo.findById(subjectEntity.getId()).get();
        if (subjectEntity.getPlan() != null) {
            PlanEntity planEntity = planService.getRepo().findById(subjectEntity.getPlan().getId()).get();
            planService.delete(planEntity);
        }

        List<ScheduleEntity> scheduleEntities =schedulesService.getRepo().findAllBySubject(subjectEntity) ;
        schedulesService.getRepo().deleteAll(scheduleEntities);

        repo.delete(subjectEntity);


    }
    @Transactional
    public void deleteAllCollapse(Iterable<SubjectEntity> entities) {
        entities.forEach(this::deleteCollapse);

    }

    @Transactional
    public void deleteAll(Iterable<SubjectEntity> entities) {
        List<Long> ids = repo.findAll().stream().map(SubjectEntity::getId).toList();
        entities = repo.findAllById(ids);

        entities.forEach(entity -> {
            if (entity.getPlan() != null) {
                planService.delete(entity.getPlan());
            }
            repo.delete(entity);
        });
    }
}
