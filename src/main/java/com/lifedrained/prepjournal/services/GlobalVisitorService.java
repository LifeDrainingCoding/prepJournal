package com.lifedrained.prepjournal.services;

import com.lifedrained.prepjournal.repo.GlobalVisitorRepo;
import com.lifedrained.prepjournal.repo.GroupsRepo;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.lifedrained.prepjournal.repo.entities.GroupEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Getter
public class GlobalVisitorService {
    public GlobalVisitorRepo repo;
    private GroupsRepo groupsRepo;

    public GlobalVisitorService(GroupsRepo groupsRepo, GlobalVisitorRepo repo) {
        this.groupsRepo = groupsRepo;
        this.repo = repo;
    }

    @Transactional

    public void saveDialog(GlobalVisitor visitor, List<Object> returnData){
        GroupEntity groupEntity =  groupsRepo.findById(((GroupEntity) returnData.get(5)).getId()).get();

        visitor.setName(((String) returnData.get(0)));
        visitor.setBirthDate((Date) returnData.get(1));
        visitor.setEnrollDate((Date) returnData.get(2));
        visitor.setAge((Integer) returnData.get(3));
        visitor.setCourse(((Integer) returnData.get(4)).byteValue());
        visitor.setGroup(groupEntity);
        visitor.setPassport(((String) returnData.get(6)));
        visitor.setEnrollId(((Long) returnData.get(7)));
        visitor.setInnId(((Long) returnData.get(8)));
        visitor.setSnils((String) returnData.get(9));

        repo.save(visitor);
    }

    @Transactional
    public void saveDetached(GlobalVisitor visitor){
        GroupEntity group = groupsRepo.findById(visitor.getGroup().getId()).get();

        visitor.setGroup(group);
        repo.save(visitor);
    }

    @Transactional
    public void saveAll(Iterable<GlobalVisitor> visitors){
        visitors.forEach(this::saveDetached);
    }
}
