package com.lifedrained.prepjournal.front.views.widgets;


import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Getter;

public class RowDateTimePicker extends HorizontalLayout {
    @Getter
    private final CustomDateTimePicker dateTimePicker;
    private final CustomLabel label;
    public RowDateTimePicker(String text){
        dateTimePicker =  new CustomDateTimePicker();
        label =  new CustomLabel(text);
        label.setClassName(LumoUtility.AlignSelf.BASELINE);

        add(label, dateTimePicker);
    }
}
