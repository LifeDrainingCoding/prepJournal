package com.lifedrained.prepjournal.front.pages;


import com.lifedrained.prepjournal.front.views.AdminTabSheetView;
import com.lifedrained.prepjournal.front.views.widgets.UserLabel;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.lifedrained.prepjournal.services.ServiceUtils;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.LinkedHashMap;


@Route("/admin")
public class AdminControlPanel extends VerticalLayout  {

    LinkedHashMap<String, ScheduleEntity> selectedSchedules;

   public AdminControlPanel(LoginRepo loginRepo, ServiceUtils utils, SchedulesService schedulesService) {
       super();
       selectedSchedules =  new LinkedHashMap<>();
       AdminTabSheetView view = new AdminTabSheetView(loginRepo,schedulesService);
       setAlignItems(Alignment.CENTER);
       view.setSuffixComponent(new UserLabel(loginRepo, utils));
       add(view);
   }


}
