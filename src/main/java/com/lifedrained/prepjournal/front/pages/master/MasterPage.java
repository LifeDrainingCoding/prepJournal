package com.lifedrained.prepjournal.front.pages.master;

import com.lifedrained.prepjournal.Utils.JSUtils;
import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.consts.Routes;
import com.lifedrained.prepjournal.front.pages.master.views.MasterSchedules;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.front.views.widgets.UserLabel;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.services.SchedulesService;
import com.lifedrained.prepjournal.services.ServiceUtils;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

@Route(Routes.MASTER_PAGE)
public class MasterPage extends VerticalLayout  {
    private CurrentSession session;
    private ServiceUtils utils;
    private SchedulesService service;
    private LoginRepo loginRepo;
    public MasterPage(CurrentSession session, ServiceUtils utils, SchedulesService service, LoginRepo loginRepo) {
        this.session = session;
        this.utils = utils;
        this.service = service;
        this.loginRepo = loginRepo;
        init();

    }
    private void init(){
        setSizeFull();
        setAlignItems(Alignment.STRETCH);

        add(new UserLabel(session, utils){{
            addComponentAsFirst(new CustomButton("Просмотреть статистику по занятиям",
                    event -> {
                        JSUtils.openNewTab(Routes.STATISTICS_PAGE+"/"+session.getEntity().getName());
            }));

        }});
        add(new MasterSchedules(session,service,loginRepo));
    }
}

