package com.lifedrained.prepjournal.front.pages.ie;

import com.lifedrained.prepjournal.Utils.ComponentSecurer;
import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.consts.Routes;
import com.lifedrained.prepjournal.front.pages.ie.views.tabs.*;
import com.lifedrained.prepjournal.front.views.widgets.BackBtn;
import com.lifedrained.prepjournal.front.views.widgets.UserLabel;
import com.lifedrained.prepjournal.repo.entities.SpecialityEntity;
import com.lifedrained.prepjournal.services.ServiceUtils;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(Routes.IE_PAGE)
public class IE extends VerticalLayout implements ComponentSecurer {
    public IE(CurrentSession session, ServiceUtils serviceUtils) {

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();

        BackBtn backBtn =  new BackBtn(session);

        UserLabel userLabel =  new UserLabel(session,serviceUtils);


        horizontalLayout.add(new HorizontalLayout(backBtn){{
            setWidthFull();
            backBtn.addClassName(LumoUtility.AlignSelf.START);
            setAlignItems(Alignment.START);
            addClassName(LumoUtility.AlignItems.START);
        }}, new HorizontalLayout(userLabel){{
            setAlignItems(Alignment.END);
            userLabel.addClassName(LumoUtility.AlignSelf.END);
            addClassName(LumoUtility.AlignItems.END);
        }});
        horizontalLayout.setWidthFull();

        add(horizontalLayout);

        TabSheet tabSheet = new TabSheet();
        tabSheet.setWidthFull();

        LoginIETab loginIETab = new LoginIETab();
        SubjectIETab subjectIETab = new SubjectIETab();
        SpecialityIETab specialityIETab = new SpecialityIETab();
        GroupsIETab groupsIETab = new GroupsIETab();
        PlanIETab planIETab = new PlanIETab();
        VisitorIETab visitorIETab = new VisitorIETab();
        checkSecurity(3 , subjectIETab,specialityIETab,planIETab);


        tabSheet.add(loginIETab, loginIETab.getContent());
        tabSheet.add(subjectIETab, subjectIETab.getContent());
        tabSheet.add(specialityIETab, specialityIETab.getContent());
        tabSheet.add(groupsIETab, groupsIETab.getContent());
        tabSheet.add(planIETab, planIETab.getContent());
        tabSheet.add(visitorIETab, visitorIETab.getContent());

        add(tabSheet );


    }
}
