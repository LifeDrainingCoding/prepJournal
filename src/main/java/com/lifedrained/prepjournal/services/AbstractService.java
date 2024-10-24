package com.lifedrained.prepjournal.services;

import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
public abstract class AbstractService<T extends JpaRepository<N,Long>, N> {
   protected T repo;


}
