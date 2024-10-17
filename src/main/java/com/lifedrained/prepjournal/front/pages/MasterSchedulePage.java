package com.lifedrained.prepjournal.front.pages;

import com.lifedrained.prepjournal.front.views.widgets.UserLabel;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

@Route("/schedules")

public class MasterSchedulePage extends VerticalLayout implements HasUrlParameter<String> {
    private LoginRepo repo;

    public MasterSchedulePage(LoginRepo repo) {
        this.repo = repo;
    }


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
    }
}

