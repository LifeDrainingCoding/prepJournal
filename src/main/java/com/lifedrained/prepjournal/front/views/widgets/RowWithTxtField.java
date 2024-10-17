package com.lifedrained.prepjournal.front.views.widgets;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility.*;


public class RowWithTxtField extends HorizontalLayout {
    TextField textField;
    CustomLabel label;
    public RowWithTxtField(String text){
        super();

        textField = new TextField();
        label = new CustomLabel(text);

        label.removeClassName(Margin.Right.LARGE);
        label.addClassName(Margin.Right.SMALL);

        add(label,textField);
    }
    public String getFieldText(){
        return textField.getValue();
    }

    public void setLabelWidth(String w){
        label.setWidth(w);
        label.setMaxWidth(w);
    }
    public void setLabelHeight(String h){
        label.setHeight(h);
        label.setMaxHeight(h);
    }
}
