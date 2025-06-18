package com.lifedrained.prepjournal.repo.entities;

import com.lifedrained.prepjournal.Utils.KeyGen;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.Objects;


@Entity
@Getter
@Setter
@Table(schema = "app", name = "schedules")
public class ScheduleEntity extends BaseEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;


    @JoinColumn(name = "subject", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private SubjectEntity subject;


    @Column(name = "academ_hours", nullable = false)
    private final byte hours = 2;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "schedule_begin_time", nullable = false, columnDefinition = "TIMESTAMP")
    private Date date;

    @Column(name = "description")
    private String description;

    @Column(name = "schedule_theme")
    private String theme;

    @Column(name = "is_executed", nullable = false)
    private boolean isExecuted = false;

    @Column(name = "visitors", columnDefinition = "LONG VARCHAR")
    private String jsonVisitors;

    @Column(name= "schedule_UID", nullable = false, unique = true)
    private String uid;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "group_ref")
    private GroupEntity group;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "master", nullable = false)
    private LoginEntity master;





    public ScheduleEntity(SubjectEntity subject, Date date , LoginEntity master, GroupEntity group, String theme) {
        setSubject(subject);
        setGroup(group);
        this.date = date;
        this.master = master;
        this.theme = theme;

        uid = KeyGen.generateKey();
    }
    public ScheduleEntity(){
        uid = KeyGen.generateKey();
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
        group.getSchedules().remove(this);
        group.getSchedules().add(this);
    }


    public void setSubject(SubjectEntity subject) {
        this.subject = subject;
        subject.getSchedules().remove(this);
        subject.getSchedules().add(this);
    }

   

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof ScheduleEntity schedule) {
            return schedule.uid.equals(uid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }
}
