package com.lifedrained.prepjournal.repo.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "global_visitors", schema = "app")
@NoArgsConstructor
public class GlobalVisitor extends BaseEntity implements Serializable {

    public final static int PARAMS_LENGTH = 7;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date", nullable = false, columnDefinition = "DATE")
    private Date birthDate;

    @Column(name = "age",nullable = false)
    private int age;

    @Column(name = "linked_master_name", nullable = false)
    private String linkedMasterName;

    @Column(name = "speciality", nullable = false)
    private String speciality;

    @Column(name = "groups", nullable = false)
    private String group;

    @Column(name = "visited_schedules_year", nullable = false)
    private int visitedSchedulesYear;

    @Column(name = "notes")
    private String notes;

    @Column(name = "visits", columnDefinition = "LONG VARCHAR")
    private String jsonVisits;

    public GlobalVisitor(String name, Date birthDate, int age,
                         String linkedMasterName, String speciality, String group,
                         int visitedSchedulesYear, String notes) {
        this.name = name;
        this.birthDate = birthDate;
        this.age = age;
        this.linkedMasterName = linkedMasterName;
        this.speciality = speciality;
        this.group = group;
        this.visitedSchedulesYear = visitedSchedulesYear;
        this.notes = notes;
    }


}
