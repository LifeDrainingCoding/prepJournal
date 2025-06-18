package com.lifedrained.prepjournal.front.pages.nomenclatures.views.tabs;

import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.front.interfaces.OnCheckedListener;
import com.lifedrained.prepjournal.front.renders.PropertyRender;
import com.lifedrained.prepjournal.front.views.CRUDListener;
import com.lifedrained.prepjournal.front.views.ControlButtons;
import com.lifedrained.prepjournal.front.views.Refreshable;
import com.lifedrained.prepjournal.front.views.widgets.CustomGrid;
import com.lifedrained.prepjournal.front.views.widgets.UserLabel;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.lifedrained.prepjournal.repo.entities.GroupEntity;
import com.lifedrained.prepjournal.services.ServiceUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractTab<T extends BaseEntity> extends Tab implements CRUDListener<T>, OnCheckedListener<T>,
        AfterCheckListener,
        Refreshable {

    @Getter
    protected VerticalLayout content;

    protected Set<T> checkedEntities;

    protected ControlButtons<T> controlButtons;


    protected CustomGrid<T,?> grid;

    protected boolean switcher = false;

    protected List<PropertyRender<T, ComponentRenderer<Component, T>>> renders;

    public AbstractTab(Class<T> clazz) {
        super();
        setLabel(name());
        renders = getRenders();
        grid = new CustomGrid<>(clazz,this, new ArrayList<>(renders));

        grid.setItems(getItems());
        content = createContent();
        checkedEntities = new HashSet<>();




        controlButtons = new ControlButtons<>(this,getCrudNames());
        content.addComponentAtIndex(0,controlButtons);
        content.addComponentAtIndex(1,grid);

        if (content == null) {
            throw new NullPointerException("Tab content is null");
        }
    }

    @Override
    public void afterCheck() {
        controlButtons.toggleButtons(!checkedEntities.isEmpty());
    }

    protected abstract List<PropertyRender<T, ComponentRenderer<Component, T>>> getRenders();

    protected abstract List<T> getItems();

    protected abstract List<String> getCrudNames();

    protected abstract String name();

    protected abstract VerticalLayout createContent();
    protected boolean turnOffSwitcher(){
        if (!switcher){
            refresh();
        }
        return true;
    }
}
