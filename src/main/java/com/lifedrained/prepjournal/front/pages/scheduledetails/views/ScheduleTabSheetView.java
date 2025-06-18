package com.lifedrained.prepjournal.front.pages.scheduledetails.views;
import com.lifedrained.prepjournal.Utils.ComponentSecurer;
import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.front.views.widgets.CustomTextEditor;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.theme.lumo.LumoUtility;



public class ScheduleTabSheetView extends TabSheet implements ComponentEventListener<CompositionUpdateEvent> ,
        ComponentSecurer {
    private final SchedulesService service;
    private ScheduleEntity entity;
    private final CurrentSession session;
    private final GlobalVisitorService globalVisitorService;
    private final LoginRepo loginRepo;

    private Button saveDescriptionBtn;
    public ScheduleTabSheetView(SchedulesService service, ScheduleEntity entity, CurrentSession session,
                                GlobalVisitorService globalVisitorService
                                , LoginRepo loginRepo){
        this.loginRepo = loginRepo;
        this.globalVisitorService = globalVisitorService;
        this.service = service;
        this.entity = entity;
        this.session = session;

        setWidthFull();
        setHeightFull();

        initTabs();
    }
    private void initTabs(){


        Tab scheduleDescription = new Tab("Описание и тема занятия");
        ScheduleNameView scheduleName = new ScheduleNameView(entity.getSubject().getName(), "350px", session);
        CustomTextEditor theme = new CustomTextEditor("Тема:", entity.getTheme());
        CustomTextEditor description = new CustomTextEditor("Описание:", entity.getDescription());
        checkSecurity(2, scheduleName);

        saveDescriptionBtn = new Button("Сохранить",  event -> {
            entity.setTheme(theme.getValue());
            entity.setDescription(description.getValue());
            service.getRepo().save(entity);
            Notify.success("Успешно сохранено");
            toggleButton();
        });

        saveDescriptionBtn.setClassName(LumoUtility.AlignSelf.CENTER);

        VerticalLayout layout = new VerticalLayout(){{
            setAlignItems(Alignment.STRETCH);
            add(scheduleName,theme,description);
            add(saveDescriptionBtn);
        }};

        add(scheduleDescription,layout);

        Tab visitorsTab = new Tab("Посетители занятия");
        VisitorsList list = new VisitorsList(globalVisitorService, entity,service);
        add(visitorsTab, list);


        Tab otherParamsTab = new Tab("Другие параметры занятия");
        OtherScheduleParams otherScheduleParams = new OtherScheduleParams(service,entity,session, loginRepo);
        add(otherParamsTab,otherScheduleParams);


    }
    private void toggleButton(){
        saveDescriptionBtn.setEnabled(!saveDescriptionBtn.isEnabled());
    }

    @Override
    public void onComponentEvent(CompositionUpdateEvent event) {
        if(!saveDescriptionBtn.isEnabled()){
            saveDescriptionBtn.setEnabled(true);
        }
    }
}
