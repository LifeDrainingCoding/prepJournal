package com.lifedrained.prepjournal.repo;

import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Table(name = "USERS", schema = "APP")
public interface LoginRepo extends JpaRepository<LoginEntity, Long> {

    Optional<LoginEntity> findByIdOrderById(Long aLong);
    Optional<LoginEntity> findByLogin(String login);
    Optional<LoginEntity> findByUid(String uid);
    List<LoginEntity> findByRole(String role);

    Optional<LoginEntity> findByNameContainsIgnoreCaseAndRole(String name, String role);

    Optional<LoginEntity> findByNameAndRole(String name, String role);
    List<LoginEntity> findAllByRoleOrRole(String role, String role2);
    boolean existsByNameAndRole(String name, String role);


    List<LoginEntity> findAllByRole(String role);
}
