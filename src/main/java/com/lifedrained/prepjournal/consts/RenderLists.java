package com.lifedrained.prepjournal.consts;

import com.lifedrained.prepjournal.front.renders.LoginRoleRender;
import com.lifedrained.prepjournal.front.renders.PropertyRender;
import com.lifedrained.prepjournal.front.renders.ScheduleDateRender;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.List;

public interface RenderLists {
    List<PropertyRender<LoginEntity,ComponentRenderer<Component, LoginEntity>>>  LOGIN_RENDERS = List.of(
            new PropertyRender<>("id","Номер"),
            new PropertyRender<>("name","ФИО"),
            new PropertyRender<>("login","Логин"),
            new PropertyRender<>("password","Пароль"),
            new PropertyRender<>("role","Права", new LoginRoleRender<>()));
    List<PropertyRender<ScheduleEntity, ComponentRenderer<Component,ScheduleEntity>>> SCHEDULES_RENDERS = List.of(
            new PropertyRender<>("id","Номер занятия"),
            new PropertyRender<>("scheduleName","Название занятия"),
            new PropertyRender<>("masterName","ФИО педагога"),
            new PropertyRender<>("theme", "Тема занятия"),
            new PropertyRender<>("date","Время проведения занятия",new ScheduleDateRender<>()),
            new PropertyRender<>("duration","Длительность занятия(минуты)")
    );
    List<PropertyRender<GlobalVisitor, ComponentRenderer<Component, GlobalVisitor>>> GLOBAL_VISITORS_RENDER = List.of(
            new PropertyRender<>("id","Номер"),
            new PropertyRender<>("name", "ФИО"),
            new PropertyRender<>("group","Группы"),
            new PropertyRender<>("age","Возраст"));

}
