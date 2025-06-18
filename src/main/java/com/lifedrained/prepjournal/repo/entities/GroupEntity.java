package com.lifedrained.prepjournal.repo.entities;

import com.lifedrained.prepjournal.Utils.KeyGen;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "groups", schema = "app")
public class GroupEntity extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "group_name", nullable = false)
    private String name;


    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "master", nullable = false)
    private LoginEntity master;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "speciality", nullable = false)
    private SpecialityEntity speciality;

    @Column(name = "students")
    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY , cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<GlobalVisitor> visitors = new HashSet<>();

    @Column(name = "group_schedules")
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<ScheduleEntity> schedules = new HashSet<>();

    @Column(name = "uid", nullable = false, unique = true)
    private String uid;

    public GroupEntity() {
        uid = KeyGen.generateKey();
    }

    public GroupEntity(String name, LoginEntity master, SpecialityEntity speciality) {
        this.master = master;
        this.name = name;
        this.speciality = speciality;
        uid = KeyGen.generateKey();

    }


    public void setVisitors(Set<GlobalVisitor> visitors){
        this.visitors = visitors;
        visitors.forEach(visitor ->
                visitor.setGroup(this));
    }

    public void setSchedules(Set<ScheduleEntity> schedules) {
        this.schedules = schedules;
        schedules.forEach(
                schedule -> schedule.setGroup(this)
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof GroupEntity group) {
            if (group.uid.equals(uid)) {
                return true;
            }

        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}
