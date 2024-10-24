package com.lifedrained.prepjournal.Utils;

import com.lifedrained.prepjournal.events.EventType;
import com.lifedrained.prepjournal.consts.Ids;
import com.lifedrained.prepjournal.consts.StringConsts;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.dialogs.BaseDialog;
import com.lifedrained.prepjournal.front.views.dialogs.ChangeGlobalVisitorDialog;
import com.lifedrained.prepjournal.front.views.dialogs.ChangeSchedulesDialog;
import com.lifedrained.prepjournal.front.views.dialogs.ChangeUserDialog;
import com.lifedrained.prepjournal.repo.GroupsRepo;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.*;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.vaadin.flow.component.UI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.Nullable;

import java.util.*;

public class ProcessorBarEvent {
    private static final Logger log = LogManager.getLogger(ProcessorBarEvent.class);
    private LoginRepo loginRepo;
    private SchedulesService schedulesService;
    private GlobalVisitorService globalVisitorService;
    private GroupsRepo groupsRepo;
    private List<Iterator<? extends BaseEntity>> iterators;
    private List<HashMap<String,? extends BaseEntity>> maps;

    public ProcessorBarEvent(List<Object> eventServices,  @Nullable List<Iterator<? extends BaseEntity>> iterators,
                             List<HashMap<String,? extends BaseEntity>> maps) {
        this.loginRepo = (LoginRepo) eventServices.get(0);
        this.schedulesService = (SchedulesService)eventServices.get(1) ;
        this.globalVisitorService = (GlobalVisitorService) eventServices.get(2) ;
        this.groupsRepo = (GroupsRepo) eventServices.get(3);
        this.iterators = iterators;
        this.maps = maps;
    }

    public void processEvent(EventType type, String barId){
        BaseDialog<String> dialog =null;
        final boolean[] switcher = {true};
        switch (type){
            case CREATE -> {
                switch (barId){
                    case Ids.ACCOUNT_BAR -> dialog = new ChangeUserDialog(new OnConfirmDialogListener<String>() {
                        @Override
                        public void onConfirm(List<String> data) {
                            LoginEntity entity = new LoginEntity();
                            entity.setLogin(data.get(0));
                            entity.setPassword(data.get(1));
                            entity.setRole(data.get(2));
                            entity.setName(data.get(3));
                            loginRepo.save(entity);
                            refresh();
                        }
                    }, StringConsts.AccFieldNames);
                    case Ids.SCHEDULES_BAR -> {
                        ChangeSchedulesDialog scheludesDialog =  new ChangeSchedulesDialog(new OnConfirmDialogListener<Object>() {
                        @Override
                        public void onConfirm(List<Object> data) {
                            ScheduleEntity entity = new ScheduleEntity();
                            entity.setScheduleName((String) data.get(0));
                            entity.setMasterName((String) data.get(1));
                            entity.setDate((Date) data.get(2));
                            entity.setDuration(Integer.parseInt((String) data.get(3)));
                            entity.setTheme((String) data.get(4));
                            schedulesService.getRepo().save(entity);
                            refresh();
                        }
                    },  StringConsts.SchedulesFieldNames);
                        scheludesDialog.open();
                    }
                    case Ids.VISITORS_BAR -> {
                        dialog = new ChangeGlobalVisitorDialog(new OnConfirmDialogListener<String>() {
                            @Override
                            public void onConfirm(List<String> returnData) {
                                String group = returnData.get(1);
                                GlobalVisitor visitor = new GlobalVisitor(returnData.get(0),
                                        group,
                                        Integer.parseInt(returnData.get(2)));
                                globalVisitorService.getRepo().save(visitor);
                                if (!groupsRepo.existsByGroup(group)){
                                    GroupEntity groupEntity = new GroupEntity(group);
                                    groupsRepo.save(groupEntity);
                                }
                                refresh();
                            }
                        }, StringConsts.VisitorFieldNames);
                    }
                    default ->  log.error("wrong id was passed: {}", barId);

                }
                if (dialog!=null) dialog.open();
            }
            case UPDATE -> {
                switch (barId){
                    case Ids.ACCOUNT_BAR -> {
                        Iterator<LoginEntity> iterator = (Iterator<LoginEntity>) iterators.get(1);
                        while (iterator.hasNext()){

                            LoginEntity entity =  iterator.next();

                             dialog = new ChangeUserDialog(new OnConfirmDialogListener<String>() {
                                @Override
                                public void onConfirm(List<String> data) {
                                    entity.setLogin(data.get(0));
                                    entity.setPassword(data.get(1));
                                    entity.setRole(data.get(2));
                                    entity.setName(data.get(3));
                                    loginRepo.save(entity);
                                    switcher[0] = turnOffSwitcher(switcher[0]);

                                }
                            }, List.of(entity.getName(), entity.getLogin(),entity.getPassword(), entity.getRole()));
                            dialog.open();

                        }
                    }
                    case Ids.SCHEDULES_BAR ->{
                        Iterator<ScheduleEntity> iterator = (Iterator<ScheduleEntity>) iterators.get(0);
                        while (iterator.hasNext()){
                            ScheduleEntity entity = iterator.next();
                            //todo на случай если нужно будет все группы взять
//                            Type visitorsType = new TypeToken<List<VisitorEntity>>(){}.getType();
//                            List<VisitorEntity> visitors = new Gson().fromJson(entity.getJsonVisitors(), visitorsType);
//                            List<String> groups = new ArrayList<>();
//                            boolean isFound = false;
//                            while(!isFound){
//                                visitors.forEach(new Consumer<VisitorEntity>() {
//                                    @Override
//                                    public void accept(VisitorEntity visitorEntity) {
//                                        if (groups.isEmpty()){
//                                            Type globalVisitorType = new TypeToken<GlobalVisitor>()
//                                            GlobalVisitor =
//                                            groups.add()
//                                        }
//                                    }
//                                });
//                            }
                            ChangeSchedulesDialog  schedulesDialog = new ChangeSchedulesDialog(new OnConfirmDialogListener<Object>() {
                                @Override
                                public void onConfirm(List<Object> returnData) {
                                    entity.setScheduleName((String)returnData.get(0));
                                    entity.setMasterName((String) returnData.get(1));
                                    entity.setDate((Date) returnData.get(2));
                                    entity.setDuration((int) returnData.get(3));
                                    entity.setTheme((String) returnData.get(4));
                                    schedulesService.getRepo().save(entity);
                                    switcher[0] = turnOffSwitcher(switcher[0]);
                                }
                            },List.of(entity.getScheduleName(), entity.getMasterName(),
                                    DateUtils.getStringFromDate(entity.getDate()),
                                    String.valueOf(entity.getDuration()),
                                  entity.getTheme()));
                          schedulesDialog.open();
                        }
                    }
                    case Ids.VISITORS_BAR -> {
                        Iterator<GlobalVisitor> iterator = (Iterator<GlobalVisitor>) iterators.get(2);
                        while (iterator.hasNext()){
                            GlobalVisitor globalVisitor = iterator.next();
                            dialog =  new ChangeGlobalVisitorDialog(new OnConfirmDialogListener<String>() {
                                @Override
                                public void onConfirm(List<String> returnData) {
                                    globalVisitor.setName(returnData.get(0));
                                    globalVisitor.setGroup(returnData.get(1));
                                    globalVisitor.setAge(Integer.parseInt(returnData.get(2)));
                                    globalVisitorService.getRepo().save(globalVisitor);
                                    switcher[0] = turnOffSwitcher(switcher[0]);
                                }
                            }, StringConsts.VisitorFieldNames);
                            dialog.open();
                        }

                    }
                    default ->log.error("Unknown barId: {}",barId);
                }
            }
            case DELETE -> {
                switch (barId){
                    case Ids.ACCOUNT_BAR -> {
                       HashMap<String,LoginEntity> selectedEntities = (HashMap<String, LoginEntity>) maps.get(0);
                       loginRepo.deleteAll(selectedEntities.values());
                       selectedEntities.clear();
                       refresh();
                    }
                    case Ids.SCHEDULES_BAR ->{
                        HashMap<String,ScheduleEntity> selectedEntities = (HashMap<String, ScheduleEntity>) maps.get(1);
                        schedulesService.getRepo().deleteAll(selectedEntities.values());
                        selectedEntities.clear();
                        refresh();
                    }
                    case Ids.VISITORS_BAR -> {
                        HashMap<String, GlobalVisitor> selectedEntities = (HashMap<String, GlobalVisitor>) maps.get(2);
                        globalVisitorService.getRepo().deleteAll(selectedEntities.values());
                        selectedEntities.clear();
                        refresh();
                    }
                    default ->log.error("Unknown barId: {}",barId);
                }


            }
            default -> {
                log.error("Unknown event type");
            }
        }
    }
    private void refresh(){
        UI.getCurrent().refreshCurrentRoute(true);
    }
    private boolean turnOffSwitcher(boolean value){
        if(value){
            refresh();
        }
        return false;
    }
}
