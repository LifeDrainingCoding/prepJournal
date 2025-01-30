package com.lifedrained.prepjournal.front.pages.statistics.views;

import com.lifedrained.prepjournal.consts.RenderLists;
import com.lifedrained.prepjournal.data.StatItem;
import com.lifedrained.prepjournal.front.pages.statistics.SortTime;
import com.lifedrained.prepjournal.front.views.widgets.CustomGrid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class StatisticsGrid extends VerticalLayout implements Consumer<SortTime> {

    private final Function<SortTime, List<StatItem>> itemGetter;
    private List<StatItem> items;
    public StatisticsGrid(Function<SortTime, List<StatItem>> itemGetter) {
        this.itemGetter = itemGetter;
    }

    private void init(){
        CustomGrid<StatItem, ?> statGrid = new CustomGrid<>(StatItem.class, RenderLists.STATS_RENDER);
        statGrid.setItems(items);
        add(statGrid);
        setSizeFull();
    }


    @Override
    public void accept(SortTime sortTime) {
      items = itemGetter.apply(sortTime);
      removeAll();
      init();
    }
}
