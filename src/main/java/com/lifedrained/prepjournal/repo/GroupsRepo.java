package com.lifedrained.prepjournal.repo;

import com.lifedrained.prepjournal.repo.entities.GroupEntity;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Table(name = "groups", schema = "app")
public interface GroupsRepo extends JpaRepository<GroupEntity, Long> {
    Optional<GroupEntity> findByName(String group);
    boolean existsByName(String name);

    void deleteAllByUid(String uid);
}
