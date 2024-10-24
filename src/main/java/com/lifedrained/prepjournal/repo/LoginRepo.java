package com.lifedrained.prepjournal.repo;

import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import jakarta.persistence.Table;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Table(name = "USERS", schema = "APP")
public interface LoginRepo extends JpaRepository<LoginEntity, Long> {
      @NonNull
     LoginEntity save(LoginEntity entity);

    Optional<LoginEntity> findByIdOrderById(Long aLong);
    Optional<LoginEntity> findByLogin(String login);
    Optional<LoginEntity> findByName(String name);

}
