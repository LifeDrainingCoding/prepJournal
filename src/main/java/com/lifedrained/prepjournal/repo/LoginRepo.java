package com.lifedrained.prepjournal.repo;

import jakarta.persistence.Table;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Table(name = "USERS")
public interface LoginRepo extends JpaRepository<LoginEntity, Long> {
      @NonNull
     LoginEntity save(LoginEntity entity);

    @Override
    Optional<LoginEntity> findById(Long aLong);
    Optional<LoginEntity> findByLogin(String login);
    @Override
    List<LoginEntity> findAll();
}
