package com.lifedrained.prepjournal.front.views;

import com.lifedrained.prepjournal.data.searchengine.OnSearchEventListener;
import com.lifedrained.prepjournal.data.searchengine.SearchEngine;
import com.lifedrained.prepjournal.data.searchengine.SearchField;
import com.lifedrained.prepjournal.data.searchengine.SearchQuery;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.MultiSelectionListener;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class SearchView<ENTITY extends BaseEntity, TYPE extends Enum<TYPE>> extends VerticalLayout {
    private static final Logger log = LogManager.getLogger(SearchView.class);
    private final CustomButton searchBtn =  new CustomButton("Найти");
    private final List<SearchField<TYPE>> textFields;
    public SearchView(OnSearchEventListener<ENTITY> eventListener, Enum<TYPE>[] types, ArrayList<ENTITY> entities) {
        super();
        textFields =  new ArrayList<>();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        List<Enum<TYPE>> listTypes = Arrays.stream(types).toList();
        listTypes.forEach(typeEnum -> {
            TextField textField =  new TextField();
            textField.setHelperText(typeEnum.toString());
            textField.addClassNames(LumoUtility.Margin.MEDIUM);
            textFields.add(new SearchField<>(typeEnum,textField));

        });

        MultiSelectComboBox<SearchField<TYPE>> comboBox = new MultiSelectComboBox<>("Поиск по критериям:");

        comboBox.setItems(textFields);
        comboBox.setItemLabelGenerator(searchField -> searchField.getType().toString());
        comboBox.addSelectionListener(
                (MultiSelectionListener<MultiSelectComboBox<SearchField<TYPE>>, SearchField<TYPE>>)
                        event -> {
          List<SearchField<TYPE>> addedItems,removedItems;
          addedItems =  event.getAddedSelection().stream().toList();

          addedItems.forEach(searchField -> {
              horizontalLayout.add(searchField.getTextField());
          });

          removedItems = event.getRemovedSelection().stream().toList();
          removedItems.forEach(searchField -> {
              horizontalLayout.remove(searchField.getTextField());
          });

        });
        horizontalLayout.add(comboBox);

        add(horizontalLayout);

        searchBtn.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
            new SearchEngine<>((List<ENTITY>) entities.clone(), formQueries(), eventListener).search();
        });
        add(searchBtn);
    }
    private List<SearchQuery<TYPE>> formQueries(){
        List<SearchQuery<TYPE>> searchQueries = new ArrayList<>();
        textFields.forEach((searchField) -> {
            TextField textField = searchField.getTextField();
            if (textField.getValue()!= null){
                SearchQuery<TYPE> searchQuery = new SearchQuery<>(searchField.getType(), textField.getValue());
                searchQueries.add(searchQuery);
            }
        });
        log.info("Total amount of search queries: {}", searchQueries.size());
        return searchQueries;
    }
}
