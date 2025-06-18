package com.lifedrained.prepjournal.services;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.repo.GroupsRepo;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.SpecialityRepo;
import com.lifedrained.prepjournal.repo.entities.GroupEntity;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.entities.SpecialityEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class GroupService {
    private static final Logger log = LogManager.getLogger(GroupService.class);
    private LoginRepo loginRepo;
    private SpecialityRepo specialityRepo;
    @Getter
    private GroupsRepo repo;
    private GlobalVisitorService globalVisitorService;
    private SchedulesService schedulesService;


    @Transactional
    public GroupEntity saveDialogGroup(GroupEntity entity, LoginEntity loginEntity, SpecialityEntity speciality, String groupName) {
        loginEntity = loginRepo.findById(loginEntity.getId()).get();
        speciality = specialityRepo.findById(speciality.getId()).get();

        if (entity == null) {
            entity = new GroupEntity(groupName,
                    loginEntity, speciality);

            entity.getMaster().setGroup(entity);
            entity.getSpeciality().getGroup().add(entity);
        }else {
            entity.setName(groupName);
            entity.setMaster(loginEntity);
            entity.getMaster().setGroup(entity);
            entity.setSpeciality(speciality);
            entity.getSpeciality().getGroup().remove(entity);
            entity.getSpeciality().getGroup().add(entity);
        }
        return repo.save(entity);
    }

    @Transactional
    public GroupEntity createImportGroup(String groupName, String masterName, String specialityName) {
        LoginEntity master = loginRepo.findByNameContainsIgnoreCaseAndRole(masterName, RoleConsts.USER_TIER1.value)
                .orElse(null);

        if (master == null) {
            Notify.error("Преподавателя с ФИО "+masterName+" не существует в базе! Строчка будет пропущена");
            return null;
        }

        SpecialityEntity speciality =  specialityRepo.findByName(specialityName).orElse(null);

        if (speciality == null) {
            Notify.error("Специальности с наименованием "+specialityName+" не существует! Строчка будет пропущена");
            return null;
        }
        GroupEntity group = null;


            group = new GroupEntity(groupName, master, speciality);
            speciality.getGroup().add(group);

        return group;
    }

    @Transactional
    public void deleteAll(Iterable<GroupEntity> entities) {
       entities.forEach(entity -> {
           entity =  repo.findById(entity.getId()).get();
           entity.getMaster().setGroup(null);
           globalVisitorService.getRepo().deleteAll(entity.getVisitors());
           schedulesService.getRepo().deleteAll(entity.getSchedules());


           SpecialityEntity speciality = specialityRepo.findById(entity.getSpeciality().getId()).get();
           speciality.getGroup().remove(entity);
           repo.delete(entity);
       });

    }
}
