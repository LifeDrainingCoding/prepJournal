package com.lifedrained.prepjournal.consts;

import com.lifedrained.prepjournal.front.renders.LoginRoleRender;
import com.lifedrained.prepjournal.front.renders.PropertyRender;
import com.lifedrained.prepjournal.front.renders.ScheduleDateRender;
import com.lifedrained.prepjournal.front.renders.ScheduleTimeRender;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.LinkedHashMap;
import java.util.List;

public interface RenderLists {
    List<PropertyRender<LoginEntity,ComponentRenderer<Component, LoginEntity>>>  LOGIN_RENDERS = List.of(
            new PropertyRender<>("id","Номер", null),
            new PropertyRender<>("name","ФИО", null),
            new PropertyRender<>("login","Логин", null),
            new PropertyRender<>("password","Пароль", null),
            new PropertyRender<>("role","", new LoginRoleRender<>()));
    List<PropertyRender<ScheduleEntity, ComponentRenderer<Component,ScheduleEntity>>> SCHEDULES_RENDERS = List.of(
            new PropertyRender<>("id","",null),
            new PropertyRender<>("scheduleName","",null),
            new PropertyRender<>("masterName","",null),
            new PropertyRender<>("date","",new ScheduleDateRender<>()),
            new PropertyRender<>("scheduleTime","",new ScheduleTimeRender<>()),
            new PropertyRender<>("duration","",null)

    );

}
