package com.lifedrained.prepjournal.services;

import com.google.common.collect.Streams;
import com.lifedrained.prepjournal.repo.SpecialityRepo;
import com.lifedrained.prepjournal.repo.entities.SpecialityEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SpecialityService {
    @Getter
    private SpecialityRepo repo;
    private GroupService groupService;




    @Transactional
    public void deleteAllCollapse(Iterable<SpecialityEntity> entities) {
        List<Long> ids = Streams.stream(entities)
                .map(SpecialityEntity::getId).toList();

        entities = repo.findAllById(ids);

        entities.forEach(entity -> {
            groupService.deleteAll(entity.getGroup());
            repo.delete(entity);
        });

    }
}
