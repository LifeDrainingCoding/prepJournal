package com.lifedrained.prepjournal.front.views.widgets;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class CustomLabel extends TextArea {
    public CustomLabel(String label){
        super("");
        setReadOnly(true);
        setValue(label);
        addClassName(LumoUtility.Margin.Right.LARGE);

        getStyle().set("word-wrap", "break-word");
        setMaxWidth(100, Unit.PIXELS);
        setWidth(100,Unit.PIXELS);

    }
}
