package com.lifedrained.prepjournal.repo.entities;

import com.lifedrained.prepjournal.Utils.KeyGen;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table( name="users", schema = "app")
public class LoginEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column (name ="login" , nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "ROLE", nullable = false)
    private String role;

    @Column(name = "name" , nullable = false)
    private String name;

    @OneToMany(mappedBy = "master",fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<ScheduleEntity> schedules = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "master")
    private GroupEntity group;

    @OneToMany(mappedBy = "master",fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<SubjectEntity> subjects =  new HashSet<>();

    @Column(name = "uid", nullable = false, unique = true)
    private String uid;

    public LoginEntity() {
        uid = KeyGen.generateKey();
    }

    public LoginEntity(String login, String password, String role) {
        this.login = login;
        this.password = password;
        this.role = role;
        uid = KeyGen.generateKey();
    }
    public LoginEntity(String login, String password, String role, String name) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
        uid = KeyGen.generateKey();
    }

    public void update(){
        subjects.forEach(subject -> {
            subject.setMaster(this);
        });
        schedules.forEach(schedule -> {
            schedule.setMaster(this);
        });
    }

    @Override
    public boolean equals(Object obj) {
        if(super.equals(obj)) return true;
        if (obj instanceof LoginEntity loginEntity) {
            return uid.equals(loginEntity.uid);
        }
        return false;

    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }
}
