package com.lifedrained.prepjournal.repo.entities;


import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.KeyGen;
import groovyjarjarpicocli.CommandLine;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


@Getter
@Setter
@Entity
@Table(name = "global_visitors", schema = "app")
public class GlobalVisitor extends BaseEntity implements Serializable {

    public final static int PARAMS_LENGTH = 9;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date", nullable = false, columnDefinition = "DATE")
    private Date birthDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "enroll_date")
    private Date enrollDate;

    @Column(name = "age",nullable = false)
    private int age;

    @Nullable
    @Column(name = "course", nullable = false)
    private byte course;

    @Column(name = "local_group_id")
    private String localId;

    @Column(name= "uid", nullable = false, unique = true)
    private String uid;


    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_ref", nullable = false)
    private GroupEntity group;

    @NotNull
    @Column(name = "passport", nullable = false)
    private String passport;

    @NotNull
    @Column(name = "enroll_id", nullable = false)
    private long enrollId;

    @NotNull
    @Column(name = "inn_id", nullable = false)
    private long innId ;

    @NotNull
    @Column(name = "snils_id", nullable = false)
    private String snils;


    @Column(name = "visits", columnDefinition = "LONG VARCHAR")
    private String jsonVisits;

    public GlobalVisitor( String name, Date birthDate, int age, byte course, GroupEntity group, Date enrollDate, long enrollId,
                         long innId, String passport, String snils) {
        this.age = age;
        this.birthDate = birthDate;
        this.course = course;
        this.enrollDate = enrollDate;
        this.enrollId = enrollId;
        setGroup(group);
        this.innId = innId;
        this.name = name;
        this.passport = passport;
        this.snils = snils;
        uid = KeyGen.generateKey();
    }

    public GlobalVisitor(String name, Date birthDate, int age, GroupEntity group, byte course, long enrollId) {
        this.name = name;
        this.course = course;
        this.enrollId = enrollId;
        this.birthDate = birthDate;
        this.age = age;
        setGroup(group);
        uid = KeyGen.generateKey();
    }
    public GlobalVisitor(){
        uid = KeyGen.generateKey();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GlobalVisitor visitor) {
            if (this.getPassport().equals(visitor.getPassport())) {
                return true;
            }
            if (this.getUid().equals(visitor.getUid()) ||
            this.name.equals(visitor.getName())) {
                return true;
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(passport);
    }
}
