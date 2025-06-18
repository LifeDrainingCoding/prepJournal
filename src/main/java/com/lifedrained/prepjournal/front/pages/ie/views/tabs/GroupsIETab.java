package com.lifedrained.prepjournal.front.pages.ie.views.tabs;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.repo.entities.GroupEntity;
import com.lifedrained.prepjournal.services.GroupService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

public class GroupsIETab extends AbstractIETab<GroupEntity> {

    private GroupService groupService;

    public GroupsIETab() {
        super();

    }

    @Override
    protected String name() {
        return "Импорт\\Экспорт групп";
    }

    @Override
    protected VerticalLayout importPanel() {
        groupService = ContextProvider.getBean(GroupService.class);

        return new VerticalLayout();
    }

    @Override
    protected VerticalLayout exportPanel() {
        return new VerticalLayout();
    }

    @Override
    protected String anchorText() {
        return "Скачать экспорт групп";
    }

    @Override
    protected ComponentEventListener<ClickEvent<Button>> exportClick() {
        return null;
    }

    @Override
    protected List<GroupEntity> entitiesToExport() {
        return groupService.getRepo().findAll();
    }

    @Override
    public void accept(List<List<String>> rows, Throwable throwable) {
        List<GroupEntity> groupsToImport = new ArrayList<>();

        List<String> headerRow = rows.get(0);
        rows.remove(headerRow);

        rows.forEach(row -> {
            String groupName, masterName, specialityName;
            groupName = row.get(0);
            masterName = row.get(1);
            specialityName = row.get(2);
            GroupEntity group ;
            try {


                 group = groupService.createImportGroup(groupName, masterName, specialityName);
            }catch (DataIntegrityViolationException ex){
                Notify.error("Преподаватель "+masterName+" уже курирует другую группу!");
                return;
            }
            if (group == null) {
                return;
            }

            groupsToImport.add(group);
        });

        groupService.getRepo().saveAll(groupsToImport);
        refresh();
        Notify.success("Успешно импортированы данные");
    }
}
