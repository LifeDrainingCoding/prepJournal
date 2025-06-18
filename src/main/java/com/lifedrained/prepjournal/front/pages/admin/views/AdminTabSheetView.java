package com.lifedrained.prepjournal.front.pages.admin.views;

import com.lifedrained.prepjournal.Utils.*;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.consts.*;
import com.lifedrained.prepjournal.data.searchengine.SearchTypes;
import com.lifedrained.prepjournal.events.EventType;

import com.lifedrained.prepjournal.events.SearchEvent;
import com.lifedrained.prepjournal.front.interfaces.CRUDControl;
import com.lifedrained.prepjournal.data.searchengine.OnSearchEventListener;
import com.lifedrained.prepjournal.front.pages.ie.views.VisitorExporter;
import com.lifedrained.prepjournal.front.pages.ie.views.VisitorImporter;
import com.lifedrained.prepjournal.front.views.ControlButtons;
import com.lifedrained.prepjournal.front.views.SearchView;
import com.lifedrained.prepjournal.front.views.widgets.CustomGrid;
import com.lifedrained.prepjournal.repo.GroupsRepo;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.lifedrained.prepjournal.consts.Ids.tabIds.*;

public class AdminTabSheetView extends TabSheet implements
        ComponentEventListener<TabSheet.SelectedChangeEvent>, CRUDControl, OnSearchEventListener<GlobalVisitor>
            , ComponentSecurer{

    private static final Logger log = LogManager.getLogger(AdminTabSheetView.class);
    private final ControlButtons<LoginEntity> accountBar;
    private final ControlButtons<ScheduleEntity> schedulesBar;
    private final ControlButtons<GlobalVisitor> visitorBar;

    private VerticalLayout usersContent, schedulesContent, visitorsContent;
    private CustomGrid<ScheduleEntity,?> schedulesGrid;
    private CustomGrid<GlobalVisitor,?>  visitorGrid;

    private final LinkedHashMap<String, LoginEntity> selectedLogins;
    private final LinkedHashMap<String, ScheduleEntity> selectedSchedules;
    private final LinkedHashMap<String,GlobalVisitor> selectedVisitors;

    private final SchedulesService schedulesService;
    private final LoginRepo repo;
    private final GroupsRepo groupsRepo;
    private final GlobalVisitorService globalVisitorService;
    private final CurrentSession session;

    private final List<Object> eventServices;
    private final List<HashMap<String,? extends BaseEntity>> maps;

    public AdminTabSheetView(LoginRepo repo, SchedulesService service,
                             GroupsRepo groupsRepo,
                             GlobalVisitorService globalVisitorService, CurrentSession session){
        super();

        this.session = session;
        schedulesService = service;
        this.repo = repo;
        this.groupsRepo = groupsRepo;
        this.globalVisitorService = globalVisitorService;
        eventServices = List.of(repo,schedulesService,globalVisitorService,groupsRepo);

        setWidthFull();
        setHeightFull();
        schedulesBar = new ControlButtons<>(this, StringConsts.SchedulesCRUDNames){{
            setId(Ids.SCHEDULES_BAR);
        }};
        accountBar = new ControlButtons<>(this, StringConsts.AccCRUDNames){{
            setId(Ids.ACCOUNT_BAR);
        }};
        visitorBar = new ControlButtons<>(this,StringConsts.VisitorCRUDNames){{
            setId(Ids.VISITORS_BAR);
        }};

        selectedLogins = new LinkedHashMap<>();
        selectedSchedules = new LinkedHashMap<>();
        selectedVisitors = new LinkedHashMap<>();
        maps = new ArrayList<>();
        maps.add(selectedLogins);
        maps.add(selectedSchedules);
        maps.add(selectedVisitors);

        initTabs();

    }
    private void initTabs(){

        Tab schedulesTab = new Tab("Список занятий");
        schedulesContent = new VerticalLayout();
        schedulesContent.add(new H1("Управление занятиями"));
        schedulesContent.setAlignItems(FlexComponent.Alignment.STRETCH);
        schedulesTab.setId(SCHEDULES_CONTENT);

        schedulesGrid = new CustomGrid<>(ScheduleEntity.class,
                RenderLists.SCHEDULES_RENDERS,
                (id, entity, isChecked, viewId) ->
                new OnCheckedEntityHandler<>(id, entity, isChecked, selectedSchedules, schedulesBar),
                Ids.SCHEDULES_BAR);

        List<ScheduleEntity> scheduleEntities = schedulesService.getRepo().findAll();
        schedulesGrid.setItems(scheduleEntities);
        schedulesContent.add(schedulesGrid);

        schedulesGrid.addItemDoubleClickListener( event -> {
            JSUtils.openNewTab(Routes.SCHEDULE_DETAILS+"/"+event.getItem().getUid());
            Notify.info("Здесь должна открываться страница с UID: "+event.getItem().getUid());
        });

        schedulesContent.addComponentAtIndex(1,schedulesBar);
        add(schedulesTab, schedulesContent);

        Tab usersTab = new Tab("Управление пользователями");
        usersContent = new VerticalLayout();
        usersContent.add(new H1("Список пользователей"));
        usersTab.setId(USERS_CONTENT);
        usersContent.setAlignItems(FlexComponent.Alignment.STRETCH);




        List<LoginEntity> entities = repo.findAll();
        CustomGrid<LoginEntity,?> entitiesGrid = new CustomGrid<>(LoginEntity.class, RenderLists.LOGIN_RENDERS,
                (id, entity, isChecked, viewId) ->
                        new OnCheckedEntityHandler<>(id, entity, isChecked, selectedLogins, accountBar),
                Ids.ACCOUNT_BAR);

        if (!session.getRole().equals(RoleConsts.ADMIN.value)){
            entities = entities.stream().filter(loginEntity->{
                RoleConsts role = RoleConsts.valueOf(loginEntity.getRole());
                RoleConsts sessionRole = RoleConsts.valueOf(session.getRole());
                if (sessionRole.ordinal()> role.ordinal()){
                    return true;
                }
                return false;
            }).toList();
        }
        entitiesGrid.setItems(entities);

        GridContextMenu<LoginEntity> entitiesMenu = entitiesGrid.addContextMenu();
        entitiesMenu.addItem("Просмотреть статистику", event ->{
           JSUtils.openNewTab(Routes.STATISTICS_PAGE+"/admin");
        });

        usersContent.add(entitiesGrid);

        add(usersTab,usersContent);

        Tab visitorTab = new Tab("Управление обучающимися");
        visitorsContent = new VerticalLayout(new H1("Список обучающихся"));

        visitorGrid =  new CustomGrid<>(GlobalVisitor.class,
                RenderLists.GLOBAL_VISITORS_RENDER,
                (id, entity, isChecked, viewId) ->
                new OnCheckedEntityHandler<>(id, entity, isChecked, selectedVisitors, visitorBar),
                Ids.VISITORS_BAR);
        visitorGrid.setItems(globalVisitorService.getRepo().findAll());

        visitorTab.setId(VISITORS_CONTENT);

        ArrayList<GlobalVisitor> globalVisitors = (ArrayList<GlobalVisitor>) globalVisitorService.getRepo().findAll();
        visitorsContent.add(visitorGrid);

        visitorsContent.addComponentAtIndex(1, new SearchView<>(this,
                SearchTypes.VISITOR_TYPE.values(), globalVisitors));
        add(visitorTab,visitorsContent );


        Tab nomencTab = new Tab("Номенклатуры");
        nomencTab.setId(RedirTabs.NOMENCLATURE.value);
        add(nomencTab,new Div());

        Tab ie = new Tab("Импорт/Экспорт данных");
        ie.setId(RedirTabs.IE.value);
        add(ie,new Div());

        addSelectedChangeListener(this);
    }

    @Override
    public void onComponentEvent(AdminTabSheetView.SelectedChangeEvent event) {
        event.getSelectedTab().getId().ifPresentOrElse(string -> {

            if(string.equals(USERS_CONTENT)){
                usersContent.addComponentAtIndex(1, accountBar);
            }else{
                selectedLogins.clear();
                usersContent.remove(accountBar);
            }

            if (string.equals(SCHEDULES_CONTENT)){
                schedulesContent.addComponentAtIndex(1,schedulesBar);
            }else{
                selectedSchedules.clear();
                schedulesContent.remove(schedulesBar);
            }

            if (string.equals(VISITORS_CONTENT)){
                visitorsContent.addComponentAtIndex(1, visitorBar);
            } else {
                selectedVisitors.clear();
                visitorsContent.remove(visitorBar);
            }

            List<RedirTabs> redirTabs = RedirTabs.all();
            redirTabs.forEach(redirTab -> {
                if (redirTab.value.equals(string)){
                    JSUtils.openNewTab(redirTab.route);
                }
            });




        }, () -> Notification.show("Tab has no id"));

    }

    @Override
    public void onDelete(String id) {
       new ProcessorBarEvent(eventServices,List.of(selectedSchedules.values().iterator(),
               selectedLogins.values().iterator(),
               selectedVisitors.values().iterator()),maps).processEvent(EventType.DELETE,id,session);
    }

    @Override
    public void onUpdate(String id) {
        new ProcessorBarEvent(eventServices,List.of(selectedSchedules.values().iterator(),
                selectedLogins.values().iterator(),
                selectedVisitors.values().iterator()),maps)
                .processEvent(EventType.UPDATE,id,session);

    }

    @Override
    public void onCreate(String id) {
        new ProcessorBarEvent(eventServices, List.of(selectedSchedules.values().iterator(),
                selectedLogins.values().iterator(),
                selectedVisitors.values().iterator()),maps).processEvent(EventType.CREATE, id,session);
    }

    @Override
    public void onSearchEvent(SearchEvent<GlobalVisitor> event) {
        visitorGrid.setItems(event.getSearchResult());
    }
}
