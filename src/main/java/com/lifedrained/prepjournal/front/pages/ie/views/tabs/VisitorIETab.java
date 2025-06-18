package com.lifedrained.prepjournal.front.pages.ie.views.tabs;

import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.front.pages.ie.views.VisitorExporter;
import com.lifedrained.prepjournal.front.pages.ie.views.VisitorImporter;
import com.lifedrained.prepjournal.repo.GroupsRepo;
import com.lifedrained.prepjournal.repo.entities.GlobalVisitor;
import com.lifedrained.prepjournal.services.GlobalVisitorService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;

import java.io.InputStream;
import java.util.List;

public class VisitorIETab extends AbstractIETab<GlobalVisitor>{
    private GroupsRepo groupsRepo;
    public GlobalVisitorService gvService;


    @Override
    protected List<GlobalVisitor> entitiesToExport() {
        return List.of();
    }

    @Override
    protected ComponentEventListener<ClickEvent<Button>> exportClick() {
        return null;
    }

    @Override
    public void accept(List<List<String>> lists, Throwable throwable) {

    }

    public VisitorIETab() {
        super();
        exportPanel.remove(exportButton);
        importPanel.remove(upload);
    }

    @Override
    protected String name() {
        return "Импорт\\Экспорт студентов";
    }

    @Override
    protected VerticalLayout importPanel() {
        groupsRepo = ContextProvider.getBean(GroupsRepo.class);
        gvService = ContextProvider.getBean(GlobalVisitorService.class);
        return new VisitorImporter(gvService,groupsRepo);
    }

    @Override
    protected VerticalLayout exportPanel() {
        return new VisitorExporter(gvService);
    }

    @Override
    protected String anchorText() {
        return "";
    }
}
