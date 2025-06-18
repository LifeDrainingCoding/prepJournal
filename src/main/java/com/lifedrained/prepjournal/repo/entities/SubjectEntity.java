package com.lifedrained.prepjournal.repo.entities;

import com.lifedrained.prepjournal.Utils.KeyGen;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.lang.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(schema = "app", name = "subjects")
@Getter
@Setter
public class SubjectEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "uid", nullable = false, unique = true)
    private String uid;

    @NotNull
    @Column(name = "subject_name", nullable = false)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH} )
    @JoinColumn(name = "master", nullable = false)
    private LoginEntity master;

    @Nullable
    @OneToOne(mappedBy = "subject" , cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private PlanEntity plan;

    @OneToMany(mappedBy = "subject" , fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<ScheduleEntity> schedules = new HashSet<>();

    @NotNull
    @Column(name = "cabinet", nullable = false)
    private int cabinet;


    public SubjectEntity(String name, LoginEntity master, int cabinet) {
        this.name = name;
        this.master = master;
        this.cabinet = cabinet;
        uid = KeyGen.generateKey();
    }

    public SubjectEntity() {
        uid = KeyGen.generateKey();
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof SubjectEntity subject) {
            return subject.uid.equals(uid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }
}
