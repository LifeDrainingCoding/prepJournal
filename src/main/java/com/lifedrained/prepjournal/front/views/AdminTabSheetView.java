package com.lifedrained.prepjournal.front.views;

import com.lifedrained.prepjournal.Utils.NameProcessor;
import com.lifedrained.prepjournal.consts.Ids;

import com.lifedrained.prepjournal.consts.RenderLists;
import com.lifedrained.prepjournal.consts.StringConsts;
import com.lifedrained.prepjournal.front.interfaces.CRUDControl;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.interfaces.OnSearchEventListener;
import com.lifedrained.prepjournal.front.interfaces.OnCheckedListener;
import com.lifedrained.prepjournal.front.views.widgets.CustomGrid;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

import static com.lifedrained.prepjournal.consts.Ids.tabIds.*;

public class AdminTabSheetView extends TabSheet implements OnSearchEventListener,
        ComponentEventListener<TabSheet.SelectedChangeEvent>, CRUDControl {

    private static final Logger log = LogManager.getLogger(AdminTabSheetView.class);
    private final ControlButtons<LoginEntity> accountBar;
    private final ControlButtons<ScheduleEntity> schedulesBar;
    private final SchedulesService schedulesService;
    private final VerticalLayout usersContent;
    private final VerticalLayout schedulesContent;
    private final LinkedHashMap<String, LoginEntity> selectedLogins;
    private final LinkedHashMap<String, ScheduleEntity> selectedSchedules;
    private final LoginRepo repo;
    private final CustomGrid<ScheduleEntity,?> schedulesGrid;


    public AdminTabSheetView(LoginRepo repo, SchedulesService service){
        super();
        setWidthFull();
        schedulesService = service;
        this.repo = repo;

        schedulesBar = new ControlButtons<>(this, StringConsts.SchedulesCRUDNames){{
            setId(Ids.SCHEDULES_BAR);
        }};
        accountBar = new ControlButtons<>(this, StringConsts.AccCRUDNames){{
            setId(Ids.ACCOUNT_BAR);
        }};

        selectedLogins = new LinkedHashMap<>();
        selectedSchedules = new LinkedHashMap<>();

        Tab schedulesTab = new Tab("Список занятий");
        schedulesContent = new VerticalLayout();
        schedulesContent.add(new H1("Управление занятиями"));
        schedulesContent.setAlignItems(FlexComponent.Alignment.STRETCH);
        schedulesTab.setId(SCHEDULES_CONTENT);

        schedulesGrid = new CustomGrid<>(ScheduleEntity.class,
                RenderLists.SCHEDULES_RENDERS, new OnCheckedListener<ScheduleEntity>() {
            @Override
            public void onChecked(String id, ScheduleEntity entity, boolean isChecked, String viewId) {
                if(isChecked){
                    selectedSchedules.put(id,entity);
                }else {
                    selectedSchedules.remove(id);
                }
                schedulesBar.checkButtons(selectedSchedules);
            }
        }, Ids.SCHEDULES_BAR);
        List<ScheduleEntity> scheduleEntities = service.getRepo().findAll();
        schedulesGrid.setItems(scheduleEntities);
        schedulesContent.add(schedulesGrid);
        add(schedulesTab, schedulesContent);

        Tab usersTab = new Tab("Управление пользователями");
        usersContent = new VerticalLayout();
        usersContent.add(new H1("Список пользователей"));
        usersTab.setId(USERS_CONTENT);
        usersContent.setAlignItems(FlexComponent.Alignment.STRETCH);

        List<LoginEntity> entities = repo.findAll();
        CustomGrid<LoginEntity,?> entitiesGrid = new CustomGrid<>(LoginEntity.class, RenderLists.LOGIN_RENDERS,
                new OnCheckedListener<LoginEntity>() {
                    @Override
                    public void onChecked(String id, LoginEntity entity, boolean isChecked, String viewId) {
                        if(isChecked){
                            selectedLogins.put(id,entity);
                        }else {
                            selectedLogins.remove(id);
                        }
                        accountBar.checkButtons(selectedLogins);
                    }
                }, Ids.ACCOUNT_BAR);
        entitiesGrid.setItems(entities);
        usersContent.add(entitiesGrid);
        add(usersTab,usersContent);


        addSelectedChangeListener(this);
    }

    @Override
    public void onSearchEvent(List<ScheduleEntity> entities) {
        schedulesGrid.setItems(entities);
    }

    @Override
    public void onComponentEvent(AdminTabSheetView.SelectedChangeEvent event) {
        event.getSelectedTab().getId().ifPresentOrElse(new Consumer<String>() {
            @Override
            public void accept(String string) {

                if(string.equals(USERS_CONTENT)){
                    usersContent.addComponentAtIndex(1, accountBar);
                }else {
                    selectedLogins.clear();
                    usersContent.remove(accountBar);
                }
                if (string.equals(SCHEDULES_CONTENT)){
                    schedulesContent.addComponentAtIndex(1,schedulesBar);
                }else {
                    selectedSchedules.clear();
                    schedulesContent.remove(schedulesBar);
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                Notification.show("Tab has no id");
            }
        });

    }



    @Override
    public void onDelete(String id) {

        if(selectedLogins.isEmpty()){
            Notification notification = new Notification("Нельзя удалить ничего. " +
                    "\n Выберите пользователей для удаления.",
                    (int) Duration.ofSeconds(10).toMillis(),
                    Notification.Position.TOP_CENTER);
            notification.open();
            return;
        }

        repo.deleteAll(selectedLogins.values());
        UI.getCurrent().refreshCurrentRoute(true);


    }

    @Override
    public void onUpdate(String id) {
        Iterator<?> iterator;
        final boolean[] switcher = {true};
        iterator =selectedLogins.values().iterator();
        while (iterator.hasNext()){

            LoginEntity entity = (LoginEntity) iterator.next();


            ChangeUserDialog dialog = new ChangeUserDialog(new OnConfirmDialogListener<List<String>>() {
                @Override
                public void onConfirm(List<String> data) {
                    entity.setLogin(data.get(0));
                    entity.setPassword(data.get(1));
                    entity.setRole(data.get(2));
                    entity.setName(data.get(3));
                    repo.save(entity);
                    if(switcher[0]){
                        getUI().ifPresent(new Consumer<UI>() {
                            @Override
                            public void accept(UI ui) {
                                ui.refreshCurrentRoute(true);
                            }
                        });
                        switcher[0] = false;
                    }

                }
            }, List.of(entity.getName(), entity.getLogin(),entity.getPassword(), entity.getRole()));
            dialog.open();

        }
    }

    @Override
    public void onCreate(String id) {
        BaseDialog<List<String>> dialog = null;
        switch (id){
            case Ids.ACCOUNT_BAR:
                 dialog = new ChangeUserDialog(new OnConfirmDialogListener<List<String>>() {
                    @Override
                    public void onConfirm(List<String> data) {
                        LoginEntity entity = new LoginEntity();
                        entity.setLogin(data.get(0));
                        entity.setPassword(data.get(1));
                        entity.setRole(data.get(2));
                        entity.setName(data.get(3));
                        repo.save(entity);
                        UI.getCurrent().refreshCurrentRoute(true);
                    }
                }, StringConsts.AccFieldNames);

                 break;

            case Ids.SCHEDULES_BAR:
                dialog =  new ChangeSchedulesDialog(new OnConfirmDialogListener<List<String>>() {
                    @Override
                    public void onConfirm(List<String> data) {
                        ScheduleEntity entity = new ScheduleEntity();
                        entity.setScheduleName(data.get(0));
                        entity.setMasterName(data.get(1));
                        entity.setDate(NameProcessor.getDateFromString(data.get(2)));
                        entity.setDuration(Integer.parseInt(data.get(3)));
                        entity.setScheduleTime(Integer.parseInt(data.get(4)));
                        entity.setJsonVisitors(data.get(5));
                        schedulesService.getRepo().save(entity);
                    }
                }, StringConsts.SchedulesFieldNames);
                break;
            default:
                log.error("wrong id was placed: {}", id);

        }
        if(dialog!=null){
            dialog.open();
        }

    }
}
