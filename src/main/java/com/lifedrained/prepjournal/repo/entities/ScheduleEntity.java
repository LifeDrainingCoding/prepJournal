package com.lifedrained.prepjournal.repo.entities;

import com.lifedrained.prepjournal.Utils.KeyGen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Entity
@Getter
@Setter
@Table(schema = "app", name = "schedules")
@AllArgsConstructor
public class ScheduleEntity extends BaseEntity  {
    private static final Logger log = LogManager.getLogger(ScheduleEntity.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "schedule_name")
    private String scheduleName;

    @Column(name = "MASTER_NAME")
    private String masterName;

    @Column(name = "duration_mins", nullable = false)
    private int duration;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "schedule_begin_time", nullable = false, columnDefinition = "TIMESTAMP")
    private Date date;

    @Column(name = "description")
    private String description;

    @Column(name = "schedule_theme")
    private String theme;

    @Column(name = "is_executed")
    private boolean isExecuted = false;

    @Column(name = "visitors")
    private String jsonVisitors;

    @Column(name= "schedule_UID", nullable = false, unique = true)
    private String uid;



    public ScheduleEntity(String scheduleName, String masterName,Date date ,int durationMins){
        this.scheduleName = scheduleName;
        this.date = date;
        this.masterName = masterName;
        duration = durationMins;
        uid = KeyGen.generateKey();
    }
    public ScheduleEntity(){
        uid = KeyGen.generateKey();
    }

}
