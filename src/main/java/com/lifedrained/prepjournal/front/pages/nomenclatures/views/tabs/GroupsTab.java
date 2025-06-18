package com.lifedrained.prepjournal.front.pages.nomenclatures.views.tabs;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.consts.RenderLists;
import com.lifedrained.prepjournal.consts.StringConsts;
import com.lifedrained.prepjournal.front.renders.PropertyRender;
import com.lifedrained.prepjournal.front.views.dialogs.ChangeGroupDialog;
import com.lifedrained.prepjournal.repo.GroupsRepo;
import com.lifedrained.prepjournal.repo.entities.GroupEntity;
import com.lifedrained.prepjournal.services.GroupService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupsTab extends AbstractTab<GroupEntity>  {


    private static final Logger log = LogManager.getLogger(GroupsTab.class);
    private GroupService groupService;

    @Override
    protected List<PropertyRender<GroupEntity, ComponentRenderer<Component, GroupEntity>>> getRenders() {
        return  new ArrayList<>(RenderLists.GROUPS_RENDER);
    }

    @Override
    protected List<GroupEntity> getItems() {
        groupService = ContextProvider.getApplicationContext().getBean(GroupService.class);
        return groupService.getRepo().findAll();
    }

    public GroupsTab() {
        super (GroupEntity.class);

    }

    @Override
    protected List<String> getCrudNames() {
        return StringConsts.GeneralCRUDNames;
    }

    @Override
    protected VerticalLayout createContent() {
        return new VerticalLayout();
    }

    @Override
    protected String name() {
        return "Учебные группы";
    }

    @Override
    public void onDelete() {
        try {
            groupService.deleteAll(checkedEntities);
            refresh();
            Notify.success("Успешно удалены выбранные группы");
        }catch (ConstraintViolationException ex){
            Notify.error("Нельзя удалить,  т.к. к выбранным элементам привязаны другие данные");
        }
    }

    @Override
    public void onUpdate() {
        checkedEntities.forEach(checkedEntity -> {
             new ChangeGroupDialog(entity -> {
                 groupService.getRepo().save(entity);
                 turnOffSwitcher();
             }, checkedEntity).open();
        });
    }

    @Override
    public void onCreate() {
        new ChangeGroupDialog(entity -> {
            groupService.getRepo().save(entity);
            refresh();
        },null).open();
    }

    @Override
    public void onChecked(String id, GroupEntity entity, boolean isChecked, String viewId) {
        if (isChecked) {
            checkedEntities.add(entity);
        }else {
            checkedEntities.remove(entity);
        }

        afterCheck();
    }
}
