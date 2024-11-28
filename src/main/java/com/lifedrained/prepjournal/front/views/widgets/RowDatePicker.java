package com.lifedrained.prepjournal.front.views.widgets;

import com.lifedrained.prepjournal.Utils.DateUtils;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Getter;

import java.util.Date;

public class RowDatePicker extends HorizontalLayout {
    private CustomLabel label;
    @Getter
    private CustomDatePicker datePicker;
    public RowDatePicker(String text, Date date) {
        label = new CustomLabel(text);
        datePicker = new CustomDatePicker();
        datePicker.setValue(DateUtils.asLocalDate(date));
        addClassName(LumoUtility.AlignSelf.BASELINE);
        add(label,datePicker);
    }
}
