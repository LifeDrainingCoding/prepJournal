package com.lifedrained.prepjournal.consts;

import com.lifedrained.prepjournal.data.StatItem;
import com.lifedrained.prepjournal.front.renders.*;
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
            new PropertyRender<>("duration","Длительность занятия(минуты)"),
            new PropertyRender<>("isExecuted", "Занятие завершено", new ScheduleBooleanRender())
    );
    List<PropertyRender<GlobalVisitor, ComponentRenderer<Component, GlobalVisitor>>> GLOBAL_VISITORS_RENDER = List.of(
            new PropertyRender<>("id","Номер"),
            new PropertyRender<>("name", "ФИО"),
            new PropertyRender<>("birthDate","Дата рождения: ", new DateVisitorRender()),
            new PropertyRender<>("age","Возраст"),
            new PropertyRender<>("linkedMasterName", "ФИО Педагога"),
            new PropertyRender<>("speciality", "Направление"),
            new PropertyRender<>("group","Группа"),
            new PropertyRender<>("visitedSchedulesYear",
                    "Кол-во посещенных занятий с начала обучения в текущем учебном году"),
            new PropertyRender<>("notes","Примечания")
            );
    List<PropertyRender<StatItem, ComponentRenderer<Component, StatItem>>> STATS_RENDER = List.of(
            new PropertyRender<>("masterName","ФИО преподавателя"),
            new PropertyRender<>("visitedHours","Посещенные часы занятий"),
            new PropertyRender<>("diff","Пропущенные часы занятий"),
            new PropertyRender<>("totalHours","Всего часов занятий")
    );

}
