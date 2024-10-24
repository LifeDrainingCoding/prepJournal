package com.lifedrained.prepjournal.services;

import com.lifedrained.prepjournal.repo.GlobalVisitorRepo;
import com.lifedrained.prepjournal.repo.VisitorsRepo;
import com.lifedrained.prepjournal.repo.entities.VisitorEntity;
import org.springframework.stereotype.Service;

@Service
public class VisitorsService{
    VisitorsRepo repo;
    public VisitorsService(VisitorsRepo repo) {
        this.repo  = repo;
    }
}
