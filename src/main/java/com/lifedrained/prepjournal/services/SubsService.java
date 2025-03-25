package com.lifedrained.prepjournal.services;

import com.lifedrained.prepjournal.repo.SubsRepo;
import com.lifedrained.prepjournal.repo.entities.SubsEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class SubsService {
    private static final Logger log = LogManager.getLogger(SubsService.class);
    private SubsRepo repo;


    public List<SubsEntity> uploadSubs(LinkedHashSet<SubsEntity> subs){
        log.info("subs uploaded");
        return repo.saveAll(subs);
    }

    public LinkedHashSet<SubsEntity> getSubs(){
        log.info("get subs");
        return new LinkedHashSet<>(repo.findAll());
    }

    public SubsEntity uploadSub(SubsEntity sub){
       return repo.save(sub);
    }
    public void deleteSub(long userId){
        repo.deleteByUserId(userId);
    }
}
