package com.lifedrained.prepjournal.front.pages.scheduledetails.views;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.Utils.SerializationUtils;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.consts.RenderLists;
import com.lifedrained.prepjournal.data.Visit;
import com.lifedrained.prepjournal.front.interfaces.OnCheckedListener;
import com.lifedrained.prepjournal.front.renders.CheckBoxColumnRender;
import com.lifedrained.prepjournal.front.renders.MarkRender;
import com.lifedrained.prepjournal.front.renders.PropertyRender;
import com.lifedrained.prepjournal.front.views.filters.EntityFilters;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.front.views.widgets.CustomGrid;
import com.lifedrained.prepjournal.front.views.widgets.RowWithComboBox;
import com.lifedrained.prepjournal.repo.GroupsRepo;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.lifedrained.prepjournal.repo.entities.GroupEntity;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class VisitorsList extends VerticalLayout implements OnCheckedListener<GlobalVisitor>, Consumer<Visit> {
    private static final Logger log = LogManager.getLogger(VisitorsList.class);
    private GlobalVisitorService globalVisitorService;
    private CustomButton addBtn, saveBtn;
    private LinkedHashMap<String, GlobalVisitor> visitedKids;
    private HashSet<Visit> markedVisits;
    private List<GlobalVisitor> visitors;
    public VisitorsList(GlobalVisitorService globalVisitorService, ScheduleEntity entity,
                        SchedulesService schedulesService){
        this.globalVisitorService = globalVisitorService;
        visitedKids = new LinkedHashMap<>();
        markedVisits = new HashSet<>();
        GroupsRepo repo = ContextProvider.getApplicationContext().getBean(GroupsRepo.class);


        RowWithComboBox<GroupEntity> group =  new RowWithComboBox<>("Название группы: ",
                repo.findAll(),
                GroupEntity::getName,EntityFilters.GROUP.get());
        add(group);

        addBtn = new CustomButton("Изменить группу", (ComponentEventListener<ClickEvent<Button>>) event -> {
            List<GlobalVisitor> visitors = globalVisitorService.getRepo().findAllByGroup(group.getCBoxValue());
            List<GlobalVisitor> notVisitors = globalVisitorService.getRepo().findAllByGroupNot(group.getCBoxValue());



            if(!visitors.isEmpty()){
                visitors.forEach(globalVisitor -> {
                    Visit visit = new Visit(entity.getUid(),
                            entity.getMaster().getName(), null, false, globalVisitor.getId(),null);
                    Type type = new TypeToken<List<Visit>>() {}.getType();
                    List<Visit> visitorVisits;
                    if (globalVisitor.getJsonVisits() != null && !globalVisitor.getJsonVisits().isEmpty()) {
                        visitorVisits = new Gson().fromJson(globalVisitor.getJsonVisits(), type);

                    } else {
                        visitorVisits = new ArrayList<>();
                    }
                    visitorVisits.add(visit);
                    globalVisitor.setJsonVisits(new Gson().toJson(visitorVisits));
                    globalVisitorService.getRepo().save(globalVisitor);
                    List<Visit> scheduleVisits = new ArrayList<>();;
                    if (entity.getJsonVisitors() != null && !entity.getJsonVisitors().isEmpty()) {
                        scheduleVisits = new Gson().fromJson(entity.getJsonVisitors(), type);
                    }

                    scheduleVisits.add(visit);
                    entity.setJsonVisitors(new Gson().toJson(scheduleVisits));
                });
                schedulesService.getRepo().save(entity);
                UI.getCurrent().refreshCurrentRoute(true);
            }else{
                Notify.error("Группа не найдена!");
            }

            //todo требует доработки

//            if (!notVisitors.isEmpty()){
//                notVisitors.forEach(globalVisitor -> {
//                   List<Visit> visits = SerializationUtils.toListVisits(globalVisitor.getJsonVisits());
//                   List<Visit> scheduleVisits = SerializationUtils.toListVisits(entity.getJsonVisitors());
//                    for (int i = 0; i < scheduleVisits.size(); i++) {
//                        Visit scheduleVisit = scheduleVisits.get(i);
//                        for (int j = 0; j <visits.size(); j++) {
//                            Visit visit = visits.get(j);
//                            if (scheduleVisit.getScheduleUID().equals(visit.getScheduleUID())) {
//
//                            }
//                        }
//                    }
//                });
//            }
        });

        visitors = globalVisitorService.getRepo().findAll();
        visitors.removeIf(globalVisitor -> {
            Type type = new TypeToken<List<Visit>>(){}.getType();
            List<Visit> visits = new Gson().fromJson(globalVisitor.getJsonVisits(), type);
            if (visits == null){
                return true;
            }
            return visits.stream().noneMatch(visit -> visit.getScheduleUID().equals(entity.getUid()));
        });

        List<PropertyRender<GlobalVisitor, ComponentRenderer<Component,GlobalVisitor>>>
                renders = new ArrayList<>(RenderLists.GLOBAL_VISITORS_RENDER);
        renders.add(1,new PropertyRender<>("isVisited", "Присутствие на занятии(да/нет)" ,
                new CheckBoxColumnRender<>(this, ((Object) entity.getUid()))));
        renders.addLast(new PropertyRender<>("jsonVisits", "Оценка за занятие", new MarkRender(entity.getUid(), this)));


        CustomGrid<GlobalVisitor, ?> visitorsGrid = new CustomGrid<>
                (GlobalVisitor.class, renders);
        visitorsGrid.setEnabled(!entity.isExecuted());
        visitorsGrid.setItems(visitors);

        saveBtn = new CustomButton("Сохранить изменения", event -> {
            visitedKids.forEach((s, globalVisitor) -> {

                List<Visit> visits =SerializationUtils.toListVisits(globalVisitor.getJsonVisits());
                Visit visit = SerializationUtils.findByUid(visits, entity.getUid());
                        assert visit != null;
                        visit.setIsVisited(true);
                visits.set(visits.indexOf(visit),visit);
                globalVisitor.setJsonVisits(SerializationUtils.toJsonVisits(visits));

                Type type = new TypeToken<List<Visit>>() {}.getType();
                List<Visit> scheduleVisits = new ArrayList<>();
                if (entity.getJsonVisitors() != null && !entity.getJsonVisitors().isEmpty()) {
                    scheduleVisits = new Gson().fromJson(entity.getJsonVisitors(), type);
                }

                scheduleVisits.forEach(visit1 -> {
                    if (visit1.getVisitorId() == visit.getVisitorId()){
                        System.out.println("equals");
                        visit1.setIsVisited(true);
                    }
                });
                entity.setJsonVisitors(new Gson().toJson(scheduleVisits));

                globalVisitorService.getRepo().save(globalVisitor);
                schedulesService.getRepo().save(entity);


            }
            );
             globalVisitorService.getRepo().findAll();

            final List<GlobalVisitor> markedVisitors  =
                    globalVisitorService.getRepo().findAllById(markedVisits.stream()
                            .map(Visit::getVisitorId)
                            .collect(Collectors.toList()));

            markedVisits.forEach(visit -> {
                markedVisitors.forEach(globalVisitor -> {
                   List<Visit> visits =SerializationUtils.toListVisits(globalVisitor.getJsonVisits());

                   if (visits == null){
                       visits = new ArrayList<>();
                   }

                   if (visits.stream().noneMatch(visit1 -> visit1
                           .getScheduleUID().equals(visit.getScheduleUID()))){
                       visits.add(visit);
                   }
                    visits.forEach(visit1 -> {
                        if ( visit1.getScheduleUID().equals(visit.getScheduleUID()) ) {

                            visit1.setMark(visit.getMark());
                        }
                    });


                   globalVisitor.setJsonVisits(SerializationUtils.toJsonVisits(visits));
                });

            });
            globalVisitorService.getRepo().saveAll(markedVisitors);

            Notify.success("Изменения сохранены!");
        });

        add(group,addBtn,visitorsGrid, saveBtn);
    }

    @Override
    public void onChecked(String id, GlobalVisitor entity, boolean isChecked, String viewId) {
        if (isChecked){
            visitedKids.put(id,entity);
        }else {
            visitedKids.remove(id);
        }
    }


    @Override
    public void accept(Visit visit) {
        markedVisits.add(visit);
        log.info("marked: {}", visit.getMark());
    }
}
