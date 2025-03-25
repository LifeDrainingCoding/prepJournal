package com.lifedrained.prepjournal.repo;

import com.lifedrained.prepjournal.repo.entities.SubsEntity;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Table(name = "subs", schema = "app")
public interface SubsRepo extends JpaRepository<SubsEntity, Long> {
    void deleteByUserId(Long userId);
}
