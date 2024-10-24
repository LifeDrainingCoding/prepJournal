package com.lifedrained.prepjournal.front.views.widgets;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class CustomTextEditor extends HorizontalLayout {
   public TextArea textArea;
    public CustomTextEditor(String lbl, String content){
        Span span = new Span(lbl);
        span.addClassName(LumoUtility.Margin.Right.MEDIUM);
        textArea = new TextArea();
        if (content == null){
            content = "";
        }
        textArea.setValue(content);
        textArea.setReadOnly(true);
        textArea.addClassName(LumoUtility.Margin.Right.MEDIUM);
        textArea.setWidthFull();

        Checkbox checkbox =  new Checkbox();
        checkbox.setLabel("Изменить");
        checkbox.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<Checkbox, Boolean>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<Checkbox, Boolean> event) {
               toggleTextArea();
            }
        });
        setAlignItems(Alignment.CENTER);
        addClassNames(LumoUtility.Border.BOTTOM, LumoUtility.Border.TOP);
        add(span,textArea,checkbox);
    }
    private void toggleTextArea(){
        textArea.setReadOnly(!textArea.isReadOnly());
    }
    public String getValue(){
        return textArea.getValue();
    }
}
