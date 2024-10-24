package com.lifedrained.prepjournal.repo;

import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Table(name = "schedules", schema = "app")
public interface SchedulesRepo extends JpaRepository<ScheduleEntity, Long> {

    List<ScheduleEntity> findAllByDate(Date date);
    List<ScheduleEntity> findAllByDateBetween(Date start, Date end);
    List<ScheduleEntity> findAllByMasterNameContains(String s);
    List<ScheduleEntity> findAllByDuration(int duration);
    List<ScheduleEntity> findAllByDurationBetween(int durStart, int durEnd);
    Optional<ScheduleEntity> findByUid(String uid);

}
