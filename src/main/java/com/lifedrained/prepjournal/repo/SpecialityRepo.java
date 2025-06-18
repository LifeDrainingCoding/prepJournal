package com.lifedrained.prepjournal.repo;

import com.lifedrained.prepjournal.repo.entities.SpecialityEntity;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Table(name = "specialities", schema = "app")
public interface SpecialityRepo extends JpaRepository<SpecialityEntity,Long> {

    boolean existsByName(String name);
    Optional<SpecialityEntity> findByName(String name);
}
