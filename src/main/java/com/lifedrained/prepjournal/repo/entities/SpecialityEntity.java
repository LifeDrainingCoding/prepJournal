package com.lifedrained.prepjournal.repo.entities;

import com.lifedrained.prepjournal.Utils.KeyGen;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "specialities", schema = "app")
@Getter
@Setter
public class SpecialityEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;


    @Column(name = "uid", nullable = false, unique = true)
    private String uid;

    @NotNull
    @Column(name = "speciality_name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "spec_code", nullable = false, unique = true)
    private String code;



    @OneToMany(mappedBy = "speciality",cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, }, fetch = FetchType.LAZY)
    private Set<GroupEntity> group = new HashSet<>();

    public SpecialityEntity(String name, String code) {
        this.name = name;
        this.code = code;
        uid = KeyGen.generateKey();
    }

    public SpecialityEntity() {
        uid = KeyGen.generateKey();
    }


}
