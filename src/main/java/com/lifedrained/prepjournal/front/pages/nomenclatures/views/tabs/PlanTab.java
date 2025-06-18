package com.lifedrained.prepjournal.front.pages.nomenclatures.views.tabs;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.consts.RenderLists;
import com.lifedrained.prepjournal.consts.StringConsts;
import com.lifedrained.prepjournal.front.renders.PropertyRender;
import com.lifedrained.prepjournal.front.views.dialogs.ChangePlanDialog;
import com.lifedrained.prepjournal.repo.PlanRepo;
import com.lifedrained.prepjournal.repo.entities.PlanEntity;
import com.lifedrained.prepjournal.services.PlanService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class PlanTab extends AbstractTab<PlanEntity>{

    private PlanService planService;

    public PlanTab() {
        super(PlanEntity.class);

    }

    @Override
    protected List<String> getCrudNames() {
        return StringConsts.GeneralCRUDNames;
    }

    @Override
    protected List<PropertyRender<PlanEntity, ComponentRenderer<Component, PlanEntity>>> getRenders() {
        return RenderLists.PLANS_RENDER;
    }

    @Override
    protected List<PlanEntity> getItems() {
        planService = ContextProvider.getApplicationContext().getBean(PlanService.class);
        return planService.getRepo().findAll();
    }

    @Override
    protected String name() {
        return "Учебный план";
    }

    @Override
    protected VerticalLayout createContent() {
        return new VerticalLayout();
    }

    @Override
    public void onChecked(String id, PlanEntity entity, boolean isChecked, String viewId) {
        if (isChecked) {
            checkedEntities.add(entity);
        }else {
            checkedEntities.remove(entity);
        }
        afterCheck();
    }

    @Override
    public void onCreate() {
        new ChangePlanDialog(entity -> {
            planService.getRepo().save(entity);
            refresh();
            Notify.success("Успешно добавлен элемент плана");
        }, null).open();


    }

    @Override
    public void onUpdate() {

        checkedEntities.forEach(entity -> {
            new ChangePlanDialog(entity1 -> {
                planService.getRepo().save(entity1);
                turnOffSwitcher();
                Notify.success("Успешно обновлены учебные планы ");
            },entity).open();
        });



    }

    @Override
    public void onDelete() {
        try {
            planService.deleteAll(checkedEntities);
            refresh();
            Notify.success("Успешно удалены выбранные элементы плана");
        }catch (ConstraintViolationException ex){
            Notify.error("Нельзя удалить,  т.к. к выбранным элементам привязаны другие данные");
        }
    }

    @Override
    public void refresh() {
        super.refresh();
    }
}
