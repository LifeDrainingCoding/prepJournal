package com.lifedrained.prepjournal.repo;

import com.lifedrained.prepjournal.repo.entities.PlanEntity;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Table(name = "study_plans", schema = "app")
public interface PlanRepo extends JpaRepository<PlanEntity,Long> {
}
