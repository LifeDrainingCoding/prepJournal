package com.lifedrained.prepjournal.front.views;

import com.lifedrained.prepjournal.consts.SearchType;
import com.lifedrained.prepjournal.front.interfaces.OnSearchEventListener;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.front.views.widgets.CustomLabel;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class SearchView extends HorizontalLayout {
    private CustomLabel label , optionalLabel;
    private CustomButton searchBtn;
    private TextField field, field1,field2,field3;
    private ComboBox<SearchType> searchTypeComboBox;
    public SearchView(OnSearchEventListener eventListener ){
        super();

    }

}
