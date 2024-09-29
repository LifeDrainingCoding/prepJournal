package com.lifedrained.prepjournal.front.pages;

import com.lifedrained.prepjournal.front.views.TabSheetView;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/admin")
public class AdminControlPanel extends VerticalLayout {
    private LoginRepo loginRepo;
   public AdminControlPanel(LoginRepo loginRepo) {
       this.loginRepo = loginRepo;
       TabSheetView view = new TabSheetView(loginRepo);
       setAlignItems(Alignment.CENTER);
       add(view);
   }
}
