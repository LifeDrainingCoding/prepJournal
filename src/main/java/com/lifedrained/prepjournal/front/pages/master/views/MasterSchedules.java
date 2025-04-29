package com.lifedrained.prepjournal.front.pages.master.views;

import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.JSUtils;
import com.lifedrained.prepjournal.Utils.OnCheckedEntityHandler;
import com.lifedrained.prepjournal.consts.*;
import com.lifedrained.prepjournal.front.interfaces.CRUDControl;
import com.lifedrained.prepjournal.front.interfaces.OnCheckedListener;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.ControlButtons;
import com.lifedrained.prepjournal.front.views.dialogs.ChangeSchedulesDialog;
import com.lifedrained.prepjournal.front.views.widgets.CustomGrid;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.*;

public class MasterSchedules extends TabSheet implements OnCheckedListener<ScheduleEntity> , CRUDControl {
    private final CurrentSession session;
    private final SchedulesService service;
    private final ControlButtons<ScheduleEntity> scheduleBar;
    private final LoginRepo loginRepo;

    private final LinkedHashMap<String, ScheduleEntity> selectedSchedules;

    public MasterSchedules(CurrentSession session, SchedulesService service, LoginRepo loginRepo) {
        super();

        this.loginRepo = loginRepo;
        this.session = session;
        this.service = service;
        selectedSchedules =  new LinkedHashMap<>();

        setSizeFull();
        scheduleBar = new ControlButtons<>(this, StringConsts.SchedulesCRUDNames){{
            setId(Ids.SCHEDULES_BAR);
        }};
        initTabs();

    }

    private void initTabs(){
        Tab mySchedules = new Tab("Мои занятия");
        VerticalLayout layoutSchedules = new VerticalLayout(){{
            setAlignItems(Alignment.STRETCH);
            add(scheduleBar);
        }};
        List<ScheduleEntity> schedules = service.repo.findAllByMasterName(session.getEntity().getName());
        CustomGrid<ScheduleEntity , ComponentRenderer<Component, ScheduleEntity>> grid =
        new CustomGrid<>(ScheduleEntity.class, RenderLists.SCHEDULES_RENDERS,this, Ids.SCHEDULES_BAR);
        grid.setItems(schedules);
        grid.addItemDoubleClickListener(new ComponentEventListener<ItemDoubleClickEvent<ScheduleEntity>>() {
            @Override
            public void onComponentEvent(ItemDoubleClickEvent<ScheduleEntity> event) {
                JSUtils.openNewTab(Routes.SCHEDULE_DETAILS+"/"+event.getItem().getUid());
            }
        });
        layoutSchedules.add(grid);
        add(mySchedules, layoutSchedules);
    }

    @Override
    public void onChecked(String id, ScheduleEntity entity, boolean isChecked, String viewId) {
        new OnCheckedEntityHandler<>(id, entity,isChecked, selectedSchedules,scheduleBar );
    }

    @Override
    public void onDelete(String id) {
        service.getRepo().deleteAll(selectedSchedules.values());
        selectedSchedules.clear();
        refresh();
    }

    @Override
    public void onUpdate(String id) {
        Iterator<ScheduleEntity> iterator = selectedSchedules.values().iterator();
        final boolean[] switcher = {true};
        while (iterator.hasNext()){
            ScheduleEntity entity = iterator.next();
            ChangeSchedulesDialog schedulesDialog = new ChangeSchedulesDialog(new OnConfirmDialogListener<Object>() {
                @Override
                public void onConfirm(List<Object> returnData) {
                    entity.setScheduleName((String)returnData.get(0));
                    entity.setMasterName( ((LoginEntity) returnData.get(1)).getName());
                    entity.setDate((Date) returnData.get(2));
                    entity.setDuration((int) returnData.get(3));
                    entity.setTheme((String) returnData.get(4));
                    entity.setMasterUid(session.getUid());
                    service.getRepo().save(entity);
                    switcher[0] = turnOffSwitcher(switcher[0]);
                }
            },List.of(entity.getScheduleName(), List.of(session.getEntity()),
                    DateUtils.getStringFromDateTime(entity.getDate()),
                    String.valueOf(entity.getDuration()),
                    entity.getTheme()), session);
            schedulesDialog.open();
        }
    }

    @Override
    public void onCreate(String id) {
        ChangeSchedulesDialog scheludesDialog =  new ChangeSchedulesDialog(new OnConfirmDialogListener<Object>() {
            @Override
            public void onConfirm(List<Object> data) {
                ScheduleEntity entity = new ScheduleEntity();
                entity.setScheduleName((String) data.get(0));
                entity.setMasterName(((LoginEntity) data.get(1)).getName());
                entity.setDate((Date) data.get(2));
                entity.setDuration(Integer.parseInt((String) data.get(3)));
                entity.setTheme((String) data.get(4));
                entity.setMasterUid(session.getUid());
                service.getRepo().save(entity);
                refresh();
            }
        }, StrConsts.getScheduleFieldValues(List.of(session.getEntity())), session);
        scheludesDialog.open();
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
