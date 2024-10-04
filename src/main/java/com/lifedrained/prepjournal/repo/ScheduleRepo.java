package com.lifedrained.prepjournal.repo;

import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepo extends JpaRepository<ScheduleEntity, Long> {

}
