package com.lifedrained.prepjournal.front.views;

import com.lifedrained.prepjournal.repo.LoginRepo;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;


public class TabSheetView extends TabSheet {
    public TabSheetView(LoginRepo repo){
        super();
        setWidthFull();
        Tab usersTab = new Tab("Управление пользователями");
        VerticalLayout usersContent = new VerticalLayout();
        usersContent.setAlignItems(FlexComponent.Alignment.CENTER);
        usersContent.add(new H1("Список пользователей"));

        add(usersTab,usersContent);

        Tab tab2 = new Tab("Вкладка 2");
        VerticalLayout content2 = new VerticalLayout();
        content2.add(new H1("Содержимое вкладки 2"));
        add(tab2, content2);
    }
}
