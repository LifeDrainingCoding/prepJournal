package com.lifedrained.prepjournal.repo;

import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.entities.SubjectEntity;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Table(name = "subjects", schema = "app")
public interface SubjectRepo extends JpaRepository<SubjectEntity, Long> {
    List<SubjectEntity> findByMaster(LoginEntity entity);

    Optional<SubjectEntity> findByName(String name);
}
