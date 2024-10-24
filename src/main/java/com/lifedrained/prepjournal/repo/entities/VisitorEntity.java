package com.lifedrained.prepjournal.repo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "visitors", schema = "app")
@NoArgsConstructor
public class VisitorEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "json_global_enity", nullable = false)
    private String jsonGlobalEntity;
    @Column(name = "linked_schedule_uid", nullable = false)
    private String scheduleUID;

    public VisitorEntity(String jsonGlobalEntity, String scheduleUID){
        this.jsonGlobalEntity = jsonGlobalEntity;
        this.scheduleUID = scheduleUID;
    }

}
