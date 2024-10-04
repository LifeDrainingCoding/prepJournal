package com.lifedrained.prepjournal.front.views;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.front.views.widgets.CustomLabel;
import com.lifedrained.prepjournal.services.ServiceUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.*;

public class AccountDialog extends Dialog {

    public AccountDialog(ServiceUtils utils){
        super();
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        CustomLabel label = new CustomLabel("Действия с аккаунтом"){{
            setMaxWidth(null);
            setWidth(null);
            addClassNames(AlignContent.CENTER, TextAlignment.CENTER);
        }};
        label.removeClassName(Margin.Right.LARGE);
        CustomButton logoutBtn = new CustomButton("Выйти из аккаунта", new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                utils.logout(UI.getCurrent());
            }
        });
        logoutBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
        CustomButton closeBtn =  new CustomButton("Закрыть", new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                close();
            }
        });
        layout.add(label,logoutBtn);
        getHeader().add(layout);
        getFooter().add(closeBtn);
    }

}
