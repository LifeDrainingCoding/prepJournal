package com.lifedrained.prepjournal.front.pages.nomenclatures;

import com.lifedrained.prepjournal.Utils.ComponentSecurer;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.consts.Routes;
import com.lifedrained.prepjournal.front.pages.nomenclatures.views.tabs.GroupsTab;
import com.lifedrained.prepjournal.front.pages.nomenclatures.views.tabs.PlanTab;
import com.lifedrained.prepjournal.front.pages.nomenclatures.views.tabs.SpecialityTab;
import com.lifedrained.prepjournal.front.pages.nomenclatures.views.tabs.SubjectsTab;
import com.lifedrained.prepjournal.front.views.widgets.BackBtn;
import com.lifedrained.prepjournal.front.views.widgets.UserLabel;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.lifedrained.prepjournal.services.ServiceUtils;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(Routes.NOMENCLATURES_PAGE)
public class Nomenclatures extends VerticalLayout implements ComponentSecurer {
    private final UserLabel userLabel;
    private final BackBtn backBtn;
    public Nomenclatures() {
        super();
        setSizeFull();
        backBtn =  new BackBtn(ContextProvider.getBean(CurrentSession.class));
        userLabel = new UserLabel(ContextProvider.getApplicationContext().getBean(CurrentSession.class),
                ContextProvider.getApplicationContext().getBean(ServiceUtils.class));
        TabSheet tabSheet = new TabSheet();


        HorizontalLayout horizontalLayout = new HorizontalLayout(new HorizontalLayout(backBtn){{
            setAlignItems(Alignment.START);
            setWidthFull();
        }}, new HorizontalLayout(userLabel){{
            setAlignItems(Alignment.END);
            setAlignSelf(Alignment.END,userLabel);
        }} );
        horizontalLayout.setWidthFull();

        tabSheet.setWidthFull();

        GroupsTab groupsTab = new GroupsTab();
        PlanTab planTab = new PlanTab();
        SpecialityTab specialityTab = new SpecialityTab();
        SubjectsTab subjectsTab = new SubjectsTab();

        checkSecurity(3,planTab,specialityTab);

        tabSheet.add(groupsTab, groupsTab.getContent());
        tabSheet.add(subjectsTab, subjectsTab.getContent());
        tabSheet.add(specialityTab, specialityTab.getContent());
        tabSheet.add(planTab, planTab.getContent());
        add(horizontalLayout,tabSheet);

    }


}
