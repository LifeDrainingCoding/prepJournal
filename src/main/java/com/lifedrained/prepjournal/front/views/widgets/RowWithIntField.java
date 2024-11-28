package com.lifedrained.prepjournal.front.views.widgets;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class RowWithIntField extends HorizontalLayout {
    private final CustomLabel label;
    private final IntegerField field;
    public RowWithIntField(String lbl){
        label = new CustomLabel(lbl);
        field =  new IntegerField();
        label.removeClassName(LumoUtility.Margin.Right.LARGE);
        label.addClassName(LumoUtility.Margin.Right.SMALL);
        addClassName(LumoUtility.AlignSelf.BASELINE);
        add(label,field);
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
    public int getValue(){
        return field.getValue();
    }
    public void setValue(int value){
        field.setValue(value);
    }
    public IntegerField getBody(){
        return field;
    }
}
