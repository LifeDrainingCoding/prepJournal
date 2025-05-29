package com.lifedrained.prepjournal.front.pages.admin;


import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.consts.Routes;
import com.lifedrained.prepjournal.front.pages.admin.views.AdminTabSheetView;
import com.lifedrained.prepjournal.front.views.widgets.UserLabel;
import com.lifedrained.prepjournal.repo.GroupsRepo;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.lifedrained.prepjournal.services.ServiceUtils;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route(Routes.ADMIN_PAGE)
public class AdminControlPanel extends VerticalLayout  {
   public AdminControlPanel(LoginRepo repo, ServiceUtils utils,
                            SchedulesService service,
                            GroupsRepo groupsRepo,
                            GlobalVisitorService globalVisitorService, CurrentSession session) {
       super();
       AdminTabSheetView view = new AdminTabSheetView(repo,service,
               groupsRepo,globalVisitorService,session);
       setAlignItems(Alignment.CENTER);
       setWidthFull();
       view.setSuffixComponent(new UserLabel(session, utils));
       add(view);
   }


}
