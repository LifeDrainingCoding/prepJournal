package com.lifedrained.prepjournal.front.views;

import com.lifedrained.prepjournal.front.lists.UserVirtualList;
import com.lifedrained.prepjournal.front.interfaces.OnCheckBoxPickedListener;
import com.lifedrained.prepjournal.front.lists.renders.UsersRender;
import com.lifedrained.prepjournal.repo.LoginEntity;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;

import java.util.List;


public class TabSheetView extends TabSheet {
    public TabSheetView(LoginRepo repo, ComponentEventListener<SelectedChangeEvent> eventListener,
                        OnCheckBoxPickedListener listener){
        super();
        setWidthFull();

        Tab tab2 = new Tab("Вкладка 2");
        tab2.setId("tab2");
        VerticalLayout content2 = new VerticalLayout();
        content2.add(new H1("Содержимое вкладки 2"));
        add(tab2, content2);

        VerticalLayout usersContent = new VerticalLayout();
        usersContent.add(new H1("Список пользователей"));
        Tab usersTab = new Tab("Управление пользователями");
        usersTab.setId("usersContent");

        usersContent.setAlignItems(FlexComponent.Alignment.STRETCH);

        List<LoginEntity> entities = repo.findAll();
        entities.addFirst(new LoginEntity(-1L,"логин","пароль", "права", "ФИО"));

        UserVirtualList list = new UserVirtualList(entities,new UsersRender(listener));
        usersContent.add(list);
        add(usersTab,usersContent);


        addSelectedChangeListener(eventListener);
    }
}
