package com.lifedrained.prepjournal.front.views.widgets;

import com.lifedrained.prepjournal.consts.RoleConsts;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.ArrayList;

public class RowWithRoleComboBox extends HorizontalLayout {
    private ComboBox<RoleConsts> comboBox;
    private CustomLabel label;
    public RowWithRoleComboBox(String text, ArrayList<RoleConsts> items){
        comboBox = new ComboBox<>();
        comboBox.setItems(items);
        comboBox.setItemLabelGenerator( item -> item.translation);
        comboBox.setValue(items.get(0));
        label = new CustomLabel(text);

        add(label,comboBox);
    }

    public String getBoxValue(){
        return comboBox.getValue().value;
    }
}
