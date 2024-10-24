package com.lifedrained.prepjournal.repo.entities;

import com.lifedrained.prepjournal.Utils.KeyGen;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name="USERS", schema = "APP")
public class LoginEntity extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column (name ="login" , nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "name")
    private String name;

    @Column(name = "UID", nullable = false)
    private String uid;


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

}
