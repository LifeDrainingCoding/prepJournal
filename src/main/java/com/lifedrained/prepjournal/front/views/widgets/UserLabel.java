package com.lifedrained.prepjournal.front.views.widgets;

import com.lifedrained.prepjournal.front.views.AccountDialog;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.services.ServiceUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static com.vaadin.flow.theme.lumo.LumoUtility.Display.FLEX;

public class UserLabel extends HorizontalLayout {
    public UserLabel(LoginRepo repo, ServiceUtils utils){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<LoginEntity>  optionalEntity = repo.findByLogin(auth.getName());
        if (optionalEntity.isEmpty()){
            System.out.println("Не найден пользователь: "+ auth.getName());
            return;
        }
        LoginEntity entity = optionalEntity.get();
        CustomLabel label = new CustomLabel(entity.getName());
        label.setMaxWidth(150, Unit.PIXELS);
        label.setWidth(150,Unit.PIXELS);
        label.removeClassName(Margin.LARGE);
        label.addClassName(Margin.NONE);
        label.addClassName(Padding.NONE);
        label.addClassName(AlignSelf.CENTER);
        Avatar avatar = new Avatar(entity.getName());

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
