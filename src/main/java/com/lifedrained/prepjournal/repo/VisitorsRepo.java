package com.lifedrained.prepjournal.repo;

import com.lifedrained.prepjournal.repo.entities.VisitorEntity;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Table(name = "visitors", schema = "app")
public interface VisitorsRepo extends JpaRepository<VisitorEntity,Long> {
    List<VisitorEntity> findAllByScheduleUID(String scheduleUID);
}
