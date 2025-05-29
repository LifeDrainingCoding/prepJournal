package com.lifedrained.prepjournal.front.views.widgets;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;

public class RowWithComboBox<T> extends HorizontalLayout {
    private final ComboBox<T> cBox;
    private final CustomLabel label;
    private final List<T> entities;

    public RowWithComboBox(String text, List<T> entities,
                           ItemLabelGenerator<T> itemLabelGenerator,
                           ComboBox.ItemFilter<T> filter){
        super();
        this.entities = entities;
        cBox = new ComboBox<>();
        cBox.setItems(filter,entities);
        cBox.setItemLabelGenerator(itemLabelGenerator);
        cBox.setValue(entities.get(0));
        label = new CustomLabel(text);
        label.removeClassName(LumoUtility.Margin.Right.LARGE);
        label.addClassName(LumoUtility.Margin.Right.SMALL);
        addClassNames(LumoUtility.AlignSelf.BASELINE);
        add(label,cBox);
    }

    public T getCBoxValue(){
        return cBox.getValue();
    }

    public void setLabelWidth(String w){
        label.setWidth(w);
        label.setMaxWidth(null);
    }
    public void setLabelHeight(String h){
        label.setHeight(h);
        label.setMaxHeight("40px");
    }
    public void setLabelHeight(String h, String mh){
        label.setHeight(h);
        label.setMaxHeight(mh);
    }
    public ComboBox<T> getBody(){
        return cBox;
    }

}
