package com.lifedrained.prepjournal.front.pages.admin.views;

import com.lifedrained.prepjournal.Utils.ExcelParser;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.data.searchengine.OnSearchEventListener;
import com.lifedrained.prepjournal.data.searchengine.SearchTypes;
import com.lifedrained.prepjournal.events.SearchEvent;
import com.lifedrained.prepjournal.front.views.SearchView;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class VisitorExporter extends VerticalLayout  implements OnSearchEventListener<GlobalVisitor> {

    private final GlobalVisitorService visitorService;
    private CustomButton exportBtn;
    private List<GlobalVisitor> visitors;
    private Anchor link;

    public VisitorExporter(GlobalVisitorService visitorService) {
        super();
        this.visitorService = visitorService;
        setSizeFull();
        setAlignItems(Alignment.CENTER);

        SearchView<GlobalVisitor, SearchTypes.VISITOR_TYPE> searchView =
                new SearchView<>(this, SearchTypes.VISITOR_TYPE.values(),
                       new ArrayList<>(visitorService.getRepo().findAll()));

        add(searchView );

    }

    @Override
    public void onSearchEvent(SearchEvent<GlobalVisitor> event) {
        Notify.info("Найдено "+event.getSearchResult().size()+" результатов");
        visitors = event.getSearchResult();

        link = ExcelParser.exportExcel(visitors);

        remove(link);
        add(link);

    }
}
