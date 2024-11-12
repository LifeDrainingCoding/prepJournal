package com.lifedrained.prepjournal.front.views;

import com.vaadin.flow.component.UI;

public interface Refreshable {
    default void refresh(){
        UI.getCurrent().refreshCurrentRoute(true);
    }
}
