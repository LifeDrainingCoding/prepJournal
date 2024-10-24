package com.lifedrained.prepjournal.front.views;
import com.lifedrained.prepjournal.CurrentSession;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.front.views.widgets.CustomTextEditor;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;


public class ScheduleTabSheetView extends TabSheet implements ComponentEventListener<CompositionUpdateEvent> {
    private final SchedulesService service;
    private ScheduleEntity entity;
    private Button saveDescriptionBtn;
    private final CurrentSession session;
    public ScheduleTabSheetView(SchedulesService service, ScheduleEntity entity, CurrentSession session){
        this.service = service;
        this.entity = entity;
        this.session = session;
        initTabs();
    }
    private void initTabs(){
        Tab scheduleDescription = new Tab("Описание и тема занятия");
        CustomTextEditor theme = new CustomTextEditor("Тема:", entity.getTheme());
        CustomTextEditor description = new CustomTextEditor("Описание:", entity.getDescription());

        saveDescriptionBtn = new Button("Сохранить", new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                entity.setTheme(theme.getValue());
                entity.setDescription(description.getValue());
                service.repo.save(entity);
                Notify.success("Успешно сохранено");
                toggleButton();
            }
        });

        VerticalLayout layout = new VerticalLayout(){{
            setAlignItems(Alignment.STRETCH);
            add(theme,description);
            add(saveDescriptionBtn);
        }};
        add(scheduleDescription,layout);
        Tab otherScheduleParams = new Tab("Другие параметры занятия");


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
