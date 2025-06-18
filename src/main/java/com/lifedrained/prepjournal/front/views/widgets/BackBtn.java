package com.lifedrained.prepjournal.front.views.widgets;

import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.consts.Routes;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class BackBtn extends Button {
    public BackBtn(CurrentSession session){
        String redirectUrl;
        if (session.getRole().equals(RoleConsts.USER_TIER1.value)){
            redirectUrl = Routes.MASTER_PAGE;
            setText("Вернуться к странице педагога");

        }else {
            redirectUrl = Routes.ADMIN_PAGE;
            setText("Вернуться к странице администратора");
        }
        addClassName(LumoUtility.AlignSelf.START);
        setIcon(VaadinIcon.ARROW_LEFT.create());
        addThemeVariants(ButtonVariant.LUMO_LARGE);
        addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            UI.getCurrent().navigate(redirectUrl);
            UI.getCurrent().refreshCurrentRoute(true);
        });
    }
}
