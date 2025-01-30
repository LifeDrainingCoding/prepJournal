package com.lifedrained.prepjournal.front.pages.statistics;


import com.lifedrained.prepjournal.Utils.DateUtils;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.consts.Routes;
import com.lifedrained.prepjournal.data.StatItem;
import com.lifedrained.prepjournal.front.pages.statistics.views.SortStats;
import com.lifedrained.prepjournal.front.pages.statistics.views.StatisticsGrid;
import com.lifedrained.prepjournal.front.views.widgets.UserLabel;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.lifedrained.prepjournal.services.LoginService;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.lifedrained.prepjournal.services.ServiceUtils;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Route(Routes.STATISTICS_PAGE)
public class Statistics extends VerticalLayout implements HasUrlParameter<String>,
        Function<SortTime,List<StatItem>> {
    private final SchedulesService schedulesService;
    private final LoginService loginService;
    private final GlobalVisitorService visitorService;
    private final CurrentSession session;
    private final ServiceUtils utils;
    private UserLabel label;
    private SortStats stats;
    private String masterName;

    public Statistics(SchedulesService schedulesService, LoginService loginService, GlobalVisitorService visitorService,
                      CurrentSession session, ServiceUtils utils) {
        this.schedulesService = schedulesService;
        this.loginService = loginService;
        this.visitorService = visitorService;
        this.session = session;
        this.utils = utils;
        setAlignItems(Alignment.CENTER);
        setWidthFull();

    }


    private void init(){
        StatisticsGrid grid =  new StatisticsGrid(this);
        stats = new SortStats(grid);
        label =  new UserLabel(session,utils );
        add(label,stats,grid);
    }


    private boolean userCheck(){
        if (!session.getRole().equals(RoleConsts.ADMIN.value) && !masterName.equals(session.getEntity().getName()) ){
            return false;
        }
        return true;
    }
    private boolean isAdmin(){
        return session.getRole().equals(RoleConsts.ADMIN.value);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
      masterName = s;

      if (userCheck()){

          init();
      }else {
          Notify.error("У вас нет доступа к этой странице." , 60, Notification.Position.TOP_CENTER);
      }
    }


    /**
     находит все записи в бд по занятиям(проверяет наличие занятий у каждого препода),
     потом формирует статистику для конкретного преподователя в определенный промежуток времени {@link SortTime},
     а потом всю собранную статистику складывает в {@link List<SortTime>}
     */
    @Override
    public List<StatItem> apply(SortTime sortTime) {
        List<StatItem> items = new ArrayList<>();

        if (isAdmin()) {

            List<LoginEntity> users = loginService.getRepo().findAll();
            users.removeIf(loginEntity -> !schedulesService.getRepo().existsByMasterName(loginEntity.getName()));
            if (sortTime == null) {

                users.forEach(loginEntity -> items.add(new StatItem(loginEntity.getName(),
                        schedulesService.repo.findAllByMasterName(loginEntity.getName()))));
            } else {

                System.out.println(DateUtils.asDate(sortTime.getStartDate()));

                /**
                 * ВАЖНО: ЕСЛИ ТИП ДАННЫХ TIMESTAMP, ТО ИСКАТЬ В JPA СУГУБО ПО LocalDateTime, ИБО ЕСЛИ ИСКАТЬ ПО LocalDate
                 * ТО ИНТЕПРЕТИРУЕТСЯ КАК ТИП ДАННЫХ DATE, А НЕ TIMESTAMP. ВПОСЛЕДСТВИИ JPA РЕПОЗИТОРИЙ ВЕРНЕТ ПУСТУЮ КОЛЛЕКЦИЮ.
                 * БЕЗ ВСЯКИЙ ИСКЛЮЧЕНИЙ И ЛОГОВ.
                 * ЭТО ПРОСТО НАХУЙ ВЫЕБЛО МНЕ НЕРВЫ, Я ОТДЕБАЖИЛ ВЕСЬКОД СВЯЗАННЫМ С ScheduleEntity НАХУЙ
                 */
                users.forEach(loginEntity -> {
                    items.add(new StatItem(loginEntity.getName(),
                            schedulesService.repo
                                    .findAllByDateBetweenAndMasterName(DateUtils.asDate(sortTime.getStartDate()),
                                            DateUtils.asDate(LocalDateTime.now()),
                                            loginEntity.getName())));
                });


            }
        }else {
            if (sortTime == null) {

                items.add(new StatItem(masterName, schedulesService.repo.findAllByMasterName(masterName)));

            }else {

                items.add(new StatItem(masterName,schedulesService.repo
                        .findAllByDateBetweenAndMasterName(DateUtils.asDate(sortTime.getStartDate()),
                                DateUtils.asDate(LocalDateTime.now()),
                                masterName)));

            }
        }
        return items;
    }

}
