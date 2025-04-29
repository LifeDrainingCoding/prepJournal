package com.lifedrained.prepjournal.front.views.filters;

import com.vaadin.flow.component.combobox.ComboBox;

public abstract class AbstractEntityFilter<T> {

    public abstract   ComboBox.ItemFilter<T> get();
}
