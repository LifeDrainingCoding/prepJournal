package com.lifedrained.prepjournal.front.views.widgets;

import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.front.views.dialogs.AccountDialog;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.services.ServiceUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.vaadin.flow.theme.lumo.LumoUtility.Display.FLEX;

public class UserLabel extends HorizontalLayout {
    private static final Logger log = LogManager.getLogger(UserLabel.class);

    public UserLabel(CurrentSession session, ServiceUtils utils){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        LoginEntity  optionalEntity = session.getEntity();
        if (optionalEntity == null){
            log.error("Не найден пользователь: {}", auth.getName());
            return;
        }
        setId("userLabel");
        Span label = new Span(optionalEntity.getName());
        label.setMaxWidth(150, Unit.PIXELS);
        label.setWidth(150,Unit.PIXELS);
        label.removeClassName(Margin.LARGE);
        label.addClassName(Margin.NONE);
        label.addClassName(Padding.NONE);
        label.addClassName(AlignSelf.CENTER);
        label.addClassName(AlignContent.STRETCH);
        Avatar avatar = new Avatar(optionalEntity.getName());
        addClassName(AlignSelf.END);
        addClassName(AlignItems.END);
        avatar.addThemeVariants(AvatarVariant.LUMO_XLARGE);

        avatar.setColorIndex((int)(Math.random()*7));

        Div div = new Div();
        div.addClassName(FLEX);
        div.add(avatar);
        div.addClickListener(new ComponentEventListener<ClickEvent<Div>>() {
            @Override
            public void onComponentEvent(ClickEvent<Div> event) {
                new AccountDialog(utils){{
                    open();
                }};
            }
        });
        add(label,div);
    }
}
