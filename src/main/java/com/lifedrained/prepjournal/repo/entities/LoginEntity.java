package com.lifedrained.prepjournal.repo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name="USERS", schema = "APP")
public class LoginEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column (name ="login" , nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "ROLES")
    private String role;

    @Column(name = "name")
    @Nullable
    private String name;


    public LoginEntity(String login, String password, String role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }
    public LoginEntity(String login, String password, String role, String name) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
    }

}
