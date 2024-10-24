package com.lifedrained.prepjournal.front.views.widgets;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Getter;

public class RowDatePicker extends HorizontalLayout {
    @Getter
    private final CustomDateTimePicker dateTimePicker;
    private final CustomLabel label;
    public RowDatePicker(String text){
        dateTimePicker =  new CustomDateTimePicker();
        label =  new CustomLabel(text);
        label.removeClassName(LumoUtility.Margin.Right.LARGE);
        label.addClassName(LumoUtility.Margin.Right.SMALL);
        label.setMaxHeight(null);
        add(label, dateTimePicker);
    }
}
