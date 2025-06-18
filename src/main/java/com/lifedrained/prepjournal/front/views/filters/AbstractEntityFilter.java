package com.lifedrained.prepjournal.front.views.filters;

import com.vaadin.flow.component.combobox.ComboBox;

public abstract class AbstractEntityFilter<T>  implements ComboBox.ItemFilter<T> {

    public ComboBox.ItemFilter<T> get(){
        return this;
    }
}
