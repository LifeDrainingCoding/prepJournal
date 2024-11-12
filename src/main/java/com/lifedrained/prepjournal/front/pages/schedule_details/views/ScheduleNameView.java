package com.lifedrained.prepjournal.front.pages.schedule_details.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class ScheduleNameView extends TextField {
    public ScheduleNameView(String value,String w){
        setValue(value);
        setWidth(w);
        setReadOnly(true);
        Icon icon = new Icon(VaadinIcon.PENCIL);
        setSuffixComponent(new Button(icon, new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                setReadOnly(!isReadOnly());
            }
        }));
        addClassNames(LumoUtility.Border.BOTTOM, LumoUtility.Border.TOP);
        setLabel("Название занятия");
    }
}
