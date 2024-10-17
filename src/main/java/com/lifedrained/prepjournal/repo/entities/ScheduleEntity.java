package com.lifedrained.prepjournal.repo.entities;

import com.lifedrained.prepjournal.Utils.KeyGen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;


@Entity
@Getter
@Setter
@Table(schema = "app", name = "schedules")
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEntity extends BaseEntity  {
    private static final Logger log = LogManager.getLogger(ScheduleEntity.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public Long id;

    @Column(name = "schedule_name")
    private String scheduleName;

    @Column(name = "MASTER_NAME")
    private String masterName;

    @Column(name ="DATE", nullable = false)
    private Date date;

    @Column(name = "duration_mins", nullable = false)
    private int duration;

    @Column(name = "schedule_begin_time", nullable = false)
    private int scheduleTime;

    @Column(name = "visitors")
    private String jsonVisitors;

    @Column(name= "schedule_UID", nullable = false)
    private String uid;


    public ScheduleEntity(String scheduleName, String masterName,Date date ,int durationMins, int scheduleTime){
        this.scheduleName = scheduleName;
        this.date = date;
        this.masterName = masterName;
        duration = durationMins;
        uid = KeyGen.generateKey();
        this.scheduleTime = scheduleTime;
    }

}
