package com.lifedrained.prepjournal.repo;

import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import jakarta.persistence.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Table(name = "global_visitors", schema = "app")
public interface GlobalVisitorRepo extends JpaRepository<GlobalVisitor, Long> {
    List<GlobalVisitor> findAllByGroup(String group);
}
