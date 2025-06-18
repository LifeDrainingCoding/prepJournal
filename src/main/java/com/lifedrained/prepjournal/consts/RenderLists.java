package com.lifedrained.prepjournal.consts;

import com.lifedrained.prepjournal.data.StatItem;
import com.lifedrained.prepjournal.front.renders.*;
import com.lifedrained.prepjournal.repo.GroupsRepo;
import com.lifedrained.prepjournal.repo.entities.*;
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
            new PropertyRender<>("subject","Дисциплина занятия", ScheduleRender.subjectName()),
            new PropertyRender<>("master","ФИО педагога", ScheduleRender.masterName()),
            new PropertyRender<>("theme", "Тема занятия"),
            new PropertyRender<>("date","Время проведения занятия",new ScheduleDateRender<>()),
            new PropertyRender<>("isExecuted", "Занятие завершено", new ScheduleBooleanRender())
    );
    List<PropertyRender<GlobalVisitor, ComponentRenderer<Component, GlobalVisitor>>> GLOBAL_VISITORS_RENDER = List.of(
            new PropertyRender<>("id","Номер"),
            new PropertyRender<>("name", "ФИО"),
            new PropertyRender<>("birthDate","Дата рождения", new DateVisitorRender()),
            new PropertyRender<>("age","Возраст"),
            new PropertyRender<>("group", "Группа", GlobalVisitorRender.groupName()),
            new PropertyRender<>("enrollId","Номер приказа"),
            new PropertyRender<>("enrollDate","Дата зачисления", GlobalVisitorRender.enrollDate())
            );
    List<PropertyRender<StatItem, ComponentRenderer<Component, StatItem>>> STATS_RENDER = List.of(
            new PropertyRender<>("masterName","ФИО преподавателя"),
            new PropertyRender<>("visitedHours","Посещенные часы занятий"),
            new PropertyRender<>("diff","Пропущенные часы занятий"),
            new PropertyRender<>("totalHours","Всего часов занятий")
    );
    List<PropertyRender<GlobalVisitor, ComponentRenderer<Component, GlobalVisitor>>> SCHEDULE_VISITORS = List.of(

            new PropertyRender<>("id","Номер"),
            new PropertyRender<>("name", "ФИО"),
            new PropertyRender<>("age","Возраст"),
            new PropertyRender<>("speciality", "Направление"),
            new PropertyRender<>("group","Группа"),
            new PropertyRender<>("notes","Примечания"));

    List<PropertyRender<SpecialityEntity, ComponentRenderer<Component, SpecialityEntity>>> SPECIALITY_RENDER = List.of(
            new PropertyRender<>("id","номер"),
            new PropertyRender<>("name","Название специальности"),
            new PropertyRender<>("code","Код специальности"));

    List<PropertyRender<SubjectEntity, ComponentRenderer<Component, SubjectEntity>>> SUBJECT_RENDER = List.of(
            new PropertyRender<>("id", "номер"),
            new PropertyRender<>("name", "Название дисциплины"),
            new PropertyRender<>("master", "Преподаватель дисциплины",new SubjectRender())
    , new PropertyRender<>("cabinet",
                    "Кабинет"));

    List<PropertyRender<GroupEntity, ComponentRenderer<Component, GroupEntity>>> GROUPS_RENDER = List.of(
            new PropertyRender<>("id", "номер"),
            new PropertyRender<>("name", "Название группы"),
            new PropertyRender<>("master", "Преподаватель" , GroupRender.masterRender()),
            new PropertyRender<>("speciality","Специальность", GroupRender.specialityRender()));

    List<PropertyRender<PlanEntity, ComponentRenderer<Component, PlanEntity>>> PLANS_RENDER = List.of(
            new PropertyRender<>("id", "номер"),
            new PropertyRender<>("subject","Дисциплина",PlanRender.subject()),
            new PropertyRender<>("dateStart","Дата начала исполнения",PlanRender.dateStart()),
            new PropertyRender<>("dateEnd","Дата окончания",PlanRender.dateEnd()),
            new PropertyRender<>("hours","Общая нагрузка по дисциплине"),
            new PropertyRender<>("weeks","Кол-во недель")
    );

}


