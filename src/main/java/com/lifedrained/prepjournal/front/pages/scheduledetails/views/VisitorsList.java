package com.lifedrained.prepjournal.front.pages.scheduledetails.views;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.Utils.SerializationUtils;
import com.lifedrained.prepjournal.consts.RenderLists;
import com.lifedrained.prepjournal.data.Visit;
import com.lifedrained.prepjournal.front.interfaces.OnCheckedListener;
import com.lifedrained.prepjournal.front.renders.CheckBoxColumnRender;
import com.lifedrained.prepjournal.front.renders.PropertyRender;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.front.views.widgets.CustomGrid;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class VisitorsList extends VerticalLayout implements OnCheckedListener<GlobalVisitor> {
    private GlobalVisitorService globalVisitorService;
    private CustomButton addBtn, executeSchedule;
    private LinkedHashMap<String, GlobalVisitor> visitedKids;
    public VisitorsList( GlobalVisitorService globalVisitorService, ScheduleEntity entity){
        this.globalVisitorService = globalVisitorService;
        visitedKids = new LinkedHashMap<>();
        RowWithTxtField group =  new RowWithTxtField("Название группы: ");
        add(group);
        addBtn = new CustomButton("Добавить группу", new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                List<GlobalVisitor> visitors = globalVisitorService.getRepo().findAllByGroup(group.getFieldText());
                if(!visitors.isEmpty()){
                    visitors.forEach(new Consumer<GlobalVisitor>() {
                        @Override
                        public void accept(GlobalVisitor globalVisitor) {
                            Visit visit =  new Visit(entity.getUid(),
                                    entity.getMasterName(), null, false );
                            Type type = new TypeToken<List<Visit>>(){}.getType();
                            List<Visit> existingVisits;
                            if(globalVisitor.getJsonVisits()!= null && !globalVisitor.getJsonVisits().isEmpty()){
                                existingVisits = new Gson().fromJson(globalVisitor.getJsonVisits(), type);
                                existingVisits.add(visit);
                            }else {
                                existingVisits = new ArrayList<>();
                                existingVisits.add(visit);
                            }
                            globalVisitor.setJsonVisits(new Gson().toJson(existingVisits));
                            globalVisitorService.getRepo().save(globalVisitor);
                        }
                    });
                    UI.getCurrent().refreshCurrentRoute(true);
                }else{
                    Notify.error("Группа не найдена!");
                }
            }
        });
        List<GlobalVisitor> visitors = globalVisitorService.getRepo().findAll();
        visitors.removeIf(new Predicate<GlobalVisitor>() {
            @Override
            public boolean test(GlobalVisitor globalVisitor) {
                Type type = new TypeToken<List<Visit>>(){}.getType();
                List<Visit> visits = new Gson().fromJson(globalVisitor.getJsonVisits(), type);
                if (visits == null){
                    return true;
                }
                return visits.stream().noneMatch(new Predicate<Visit>() {
                    @Override
                    public boolean test(Visit visit) {
                        return visit.getScheduleUID().equals(entity.getUid());
                    }
                });
            }
        });
        List<PropertyRender<GlobalVisitor, ComponentRenderer<Component,GlobalVisitor>>>
                renders = new ArrayList<>(RenderLists.GLOBAL_VISITORS_RENDER);
        renders.add(new PropertyRender<>("isVisited", "Присутствие на занятии(да/нет)" ,
                new CheckBoxColumnRender<>(this, "")));


        CustomGrid<GlobalVisitor, ?> visitorsGrid = new CustomGrid<>
                (GlobalVisitor.class, renders);
        visitorsGrid.setEnabled(!entity.isExecuted());
        visitorsGrid.setItems(visitors);
        executeSchedule = new CustomButton("Сохранить изменения", (ComponentEventListener<ClickEvent<Button>>) event -> {
            visitedKids.forEach((s, globalVisitor) -> {
                List<Visit> visits =SerializationUtils.toListVisits(globalVisitor.getJsonVisits());
                Visit visit = SerializationUtils.findByUid(visits, entity.getUid());
                visit.setIsVisited(true);
                visits.set(visits.indexOf(visit),visit);
                globalVisitor.setJsonVisits(SerializationUtils.toJsonVisits(visits));

                int numOfVisitsYear = globalVisitor.getVisitedSchedulesYear()+1;
                globalVisitor.setVisitedSchedulesYear(numOfVisitsYear);


            }
            );
            Notify.success("Изменения сохранены!");
        });

        add(group,addBtn,visitorsGrid,executeSchedule);
    }

    @Override
    public void onChecked(String id, GlobalVisitor entity, boolean isChecked, String viewId) {
        if (isChecked){
            visitedKids.put(id,entity);
        }else {
            visitedKids.remove(id);
        }
    }
}
