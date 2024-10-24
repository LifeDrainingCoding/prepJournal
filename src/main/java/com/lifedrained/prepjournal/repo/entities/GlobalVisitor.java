package com.lifedrained.prepjournal.repo.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "global_visitors", schema = "app")
@NoArgsConstructor
public class GlobalVisitor extends BaseEntity implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "groups")
    private String group;
    @Column(name = "age")
    private int age;
    @Column(name = "visits")
    private String jsonVisits;

    public GlobalVisitor(String name, String group, int age) {
        this.name = name;
        this.group = group;
        this.age = age;
    }
}
