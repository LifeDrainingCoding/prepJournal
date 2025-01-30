package com.lifedrained.prepjournal.front.pages.statistics.views;

import com.lifedrained.prepjournal.front.pages.statistics.SortTime;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.front.views.widgets.CustomLabel;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;
import java.util.function.Consumer;

public class SortStats extends HorizontalLayout {
    private Consumer<SortTime> dateConsumer;
    public SortStats( Consumer<SortTime> dateConsumer) {
        this.dateConsumer = dateConsumer;
        init();
    }



    private void init(){
        ComboBox<SortTime> comboBox =  new ComboBox<>();
        comboBox.setItems(List.of(SortTime.values()));
        comboBox.setItemLabelGenerator(item -> item.translation);
        comboBox.setValue(List.of(SortTime.values()).get(0));
        CustomLabel label = new CustomLabel("Отсортировать по: ");
        add(label);
        add(comboBox);
        CustomButton button =  new CustomButton("Применить сортировку");
        button.addClickListener(event ->
                dateConsumer.accept(comboBox.getValue()));
        add(button);
    }


}
