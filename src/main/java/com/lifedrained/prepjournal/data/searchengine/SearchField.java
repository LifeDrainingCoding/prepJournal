package com.lifedrained.prepjournal.data.searchengine;

import com.vaadin.flow.component.textfield.TextField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class SearchField<TYPE extends Enum<TYPE>> {
    private Enum<TYPE> type;
    private TextField textField;
    
}
