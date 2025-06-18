package com.lifedrained.prepjournal.repo;

import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.lifedrained.prepjournal.repo.entities.GroupEntity;
import jakarta.persistence.Table;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Table(name = "global_visitors", schema = "app")

public interface GlobalVisitorRepo extends JpaRepository<GlobalVisitor, Long> {
    List<GlobalVisitor> findAllByGroup( GroupEntity group);
    List<GlobalVisitor> findAllByGroupNot( GroupEntity group);

    @NotNull List<GlobalVisitor> findAllById(@NotNull Iterable<Long> ids);

    boolean existsByGroup(GroupEntity group);
}
