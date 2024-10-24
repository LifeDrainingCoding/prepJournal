package com.lifedrained.prepjournal.repo;

import com.lifedrained.prepjournal.repo.entities.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GroupsRepo extends JpaRepository<GroupEntity, Long> {
    Optional<GroupEntity> findByGroup(String group);
    boolean existsByGroup(String string);
}
