package com.lifedrained.prepjournal.front.pages;


import com.lifedrained.prepjournal.consts.Routes;
import com.lifedrained.prepjournal.front.views.AdminTabSheetView;
import com.lifedrained.prepjournal.front.views.widgets.UserLabel;
import com.lifedrained.prepjournal.repo.GroupsRepo;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.lifedrained.prepjournal.services.ServiceUtils;

import com.lifedrained.prepjournal.services.VisitorsService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.LinkedHashMap;


@Route(Routes.ADMIN_PAGE)
public class AdminControlPanel extends VerticalLayout  {
   public AdminControlPanel(LoginRepo repo,ServiceUtils utils,
                            SchedulesService service,
                            GroupsRepo groupsRepo,
                            VisitorsService visitorsService,
                            GlobalVisitorService globalVisitorService) {
       super();
       AdminTabSheetView view = new AdminTabSheetView(repo,service,
               groupsRepo,visitorsService,globalVisitorService,utils);
       setAlignItems(Alignment.CENTER);
       view.setSuffixComponent(new UserLabel(repo, utils));
       add(view);
   }


}
