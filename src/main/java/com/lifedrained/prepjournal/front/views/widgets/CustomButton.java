package com.lifedrained.prepjournal.front.views.widgets;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class CustomButton extends Button {
    private ButtonVariant currentTheme;
    public CustomButton(String text , ComponentEventListener<ClickEvent<Button>> eventListener){
        super();
        setText(text);
        addClickListener(eventListener);
        addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        currentTheme = ButtonVariant.LUMO_TERTIARY;
        addClassNames(LumoUtility.Margin.Right.MEDIUM);
    }
    public CustomButton(String text){
        super();
        setText(text);
        addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        currentTheme = ButtonVariant.LUMO_TERTIARY;
        addClassNames(LumoUtility.Margin.Right.MEDIUM);
    }
    public void setTheme(ButtonVariant theme){
        removeThemeVariants(currentTheme);
        addThemeVariants(theme);
        currentTheme = theme;
    }

}
