package com.lifedrained.prepjournal.repo;

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
@Table( name="USERS")
public class LoginEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column (name ="login")
    private String login;

    @Column(name = "password")
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
