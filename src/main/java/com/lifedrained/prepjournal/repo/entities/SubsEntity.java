package com.lifedrained.prepjournal.repo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(schema = "app", name = "subs")
@NoArgsConstructor
public class SubsEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "user_id")
    public Long userId;
    public SubsEntity(Long userId) {
        this.userId = userId;
    }
}
