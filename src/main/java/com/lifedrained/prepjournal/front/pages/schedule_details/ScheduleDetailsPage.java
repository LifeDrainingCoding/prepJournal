package com.lifedrained.prepjournal.front.pages.schedule_details;

import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.consts.Routes;
import com.lifedrained.prepjournal.front.pages.schedule_details.views.BackBtn;
import com.lifedrained.prepjournal.front.pages.schedule_details.views.ScheduleTabSheetView;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Optional;
import java.util.function.Consumer;

@Route(Routes.SCHEDULE_DETAILS)
public class ScheduleDetailsPage extends VerticalLayout implements HasUrlParameter<String> {
    private ScheduleEntity schedule;
    private SchedulesService service;
    private CurrentSession session;
    private final GlobalVisitorService globalVisitorService;
    public ScheduleDetailsPage(CurrentSession session, SchedulesService service,
                               GlobalVisitorService globalVisitorService){
       this.service = service;
       this.session = session;
       this.globalVisitorService = globalVisitorService;
    }

    private void init(){
        add(new BackBtn(session));
        String masterName = schedule.getMasterName();
        if(!masterName.equals(session.getEntity().getName())) {
            if (!session.getEntity().getRole().equals(RoleConsts.ADMIN.value)) {
                Notify.error("У вас нет прав для просмотра контента этой страницы. Покиньте страницу.", 15, Notification.Position.MIDDLE);
                return;
            }
        }
        setAlignItems(Alignment.CENTER);
        ScheduleTabSheetView scheduleTabSheetView = new ScheduleTabSheetView(service,schedule, session,
                globalVisitorService);
        Span span = new Span("UID занятия: "+schedule.getUid()){{
            setWidthFull();
        }};
        span.addClassNames(LumoUtility.Border.BOTTOM, LumoUtility.Border.TOP);
        add(scheduleTabSheetView);
        add(span);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        Optional<ScheduleEntity>  scheduleEntity = service.getRepo().findByUid(parameter);
        scheduleEntity.ifPresentOrElse(new Consumer<ScheduleEntity>() {
            @Override
            public void accept(ScheduleEntity entity) {
                schedule = entity;
                init();
            }
        }, new Runnable() {
            @Override
            public void run() {
                Notify.error("Не найдено занятие по UID: "+parameter);
            }
        });
    }
}
