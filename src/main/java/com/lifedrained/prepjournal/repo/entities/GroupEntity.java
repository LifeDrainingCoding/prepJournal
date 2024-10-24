package com.lifedrained.prepjournal.repo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "groups", schema = "app")
@NoArgsConstructor
public class GroupEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "groups", nullable = false)
    private String group;
    public GroupEntity(String group){
        this.group = group;
    }
    public String get(){
        return group;
    }
    public void set(String group){
        this.group = group;
    }
}
