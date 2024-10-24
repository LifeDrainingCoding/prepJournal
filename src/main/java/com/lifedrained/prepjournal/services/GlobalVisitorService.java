package com.lifedrained.prepjournal.services;

import com.lifedrained.prepjournal.repo.GlobalVisitorRepo;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class GlobalVisitorService {
    GlobalVisitorRepo repo;
    public GlobalVisitorService(GlobalVisitorRepo repo){
        this.repo = repo;
    }

}
