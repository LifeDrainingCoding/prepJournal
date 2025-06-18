package com.lifedrained.prepjournal.front.pages.scheduledetails.views;

import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class ScheduleNameView extends TextField {
    public ScheduleNameView(String value, String w, CurrentSession session){
        setValue(value);
        setWidth(w);
        setReadOnly(true);
        Icon icon = new Icon(VaadinIcon.PENCIL);
        setSuffixComponent(new Button(icon,  event -> setReadOnly(!isReadOnly())){
            {
                if (!session.getRole().equals(RoleConsts.ADMIN.value)){
                    setVisible(false);
                    setEnabled(false);
                }
            }
        });
        addClassNames(LumoUtility.Border.BOTTOM, LumoUtility.Border.TOP);
        setLabel("Название занятия");
    }
}
