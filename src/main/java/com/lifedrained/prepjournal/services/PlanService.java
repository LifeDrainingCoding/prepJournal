package com.lifedrained.prepjournal.services;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.repo.PlanRepo;
import com.lifedrained.prepjournal.repo.SubjectRepo;
import com.lifedrained.prepjournal.repo.entities.PlanEntity;
import com.lifedrained.prepjournal.repo.entities.SubjectEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlanService {
    private static final Logger log = LogManager.getLogger(PlanService.class);
    private SubjectRepo subjectRepo;
    @Getter
    private PlanRepo repo;


    @Transactional
    public PlanEntity saveDetached(PlanEntity entity, SubjectEntity subject) {
        log.error("subject id "+subject.getId());
        subject = subjectRepo.findById(subject.getId()).get();

        entity.setSubject(subject);
        entity.getSubject().setPlan(entity);
        try {

            return repo.save(entity);
        }catch (DataIntegrityViolationException ex){
            Notify.error("Элемент учебного плана "+ subject.getName()+" уже существует. Пропуск строчки");
            return null;
        }
    }

    @Transactional
    public void saveAll(List<PlanEntity> entities) {
        entities.forEach(entity -> saveDetached(entity, entity.getSubject()));
    }

    @Transactional
    public void deleteAll(Iterable<PlanEntity> entities) {
        entities.forEach(this::delete);


    }

    @Transactional
    public void delete(PlanEntity entity) {
        entity.getSubject().setPlan(null);
        repo.delete(entity);
    }
}
