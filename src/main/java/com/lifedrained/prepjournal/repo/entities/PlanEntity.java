package com.lifedrained.prepjournal.repo.entities;

import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.KeyGen;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.atmosphere.config.service.Get;
import org.springframework.integration.support.locks.RenewableLockRegistry;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "study_plans", schema = "app")
@Getter
@Setter
public class PlanEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "uid", nullable = false, unique = true)
    private String uid;

    @Column(name = "plan_name", nullable = false)
    private String name;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "subject", nullable = false)
    private SubjectEntity subject;

    @NotNull
    @Column(name = "date_start", nullable = false)
    private LocalDate dateStart;

    @NotNull
    @Column(name = "date_end", nullable = false)
    private LocalDate dateEnd;

    @NotNull
    @Column(name = "week_map", nullable = false, columnDefinition ="LONG VARCHAR" )
    private String weekMap;

    @NotNull
    @Column(name = "subject_hours", nullable = false)
    private int hours;

    @NotNull
    @Column(name = "weeks", nullable = false)
    private Integer weeks;


    public PlanEntity(LocalDate dateStart, LocalDate dateEnd, SubjectEntity subject) {
        this.dateEnd = dateEnd;
        this.dateStart = dateStart;
        this.subject = subject;
        weeks = DateUtils.countWeeksBetween(dateStart, dateEnd);
        uid = KeyGen.generateKey();
        pollName();
    }

    public PlanEntity() {
        uid = KeyGen.generateKey();
    }

    public void setSubject(SubjectEntity subject) {
        this.subject = subject;
        pollName();
    }

    public void setWeeks(Integer weeks) {
        this.weeks = weeks;
        pollName();
    }


    private void pollName(){
        if (subject != null && weeks != null) {
            name = String.format("Plan_%d_%s", weeks, subject.getName() );
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof PlanEntity plan) {
            return plan.uid.equals(uid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }

}
