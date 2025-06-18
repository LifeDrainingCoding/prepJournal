package com.lifedrained.prepjournal.front.pages.nomenclatures.views.tabs;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.consts.RenderLists;
import com.lifedrained.prepjournal.consts.StringConsts;
import com.lifedrained.prepjournal.front.renders.PropertyRender;
import com.lifedrained.prepjournal.front.views.dialogs.ActionDialog;
import com.lifedrained.prepjournal.front.views.dialogs.ChangeSpecialityDialog;
import com.lifedrained.prepjournal.repo.SpecialityRepo;
import com.lifedrained.prepjournal.repo.entities.SpecialityEntity;
import com.lifedrained.prepjournal.services.SpecialityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public class SpecialityTab extends AbstractTab<SpecialityEntity>{
    private SpecialityService specialityService;
    public SpecialityTab() {
        super(SpecialityEntity.class);


    }

    @Override
    protected List<String> getCrudNames() {
        return StringConsts.GeneralCRUDNames;
    }

    @Override
    protected List<SpecialityEntity> getItems() {
        specialityService = ContextProvider.getBean(SpecialityService.class);
        return specialityService.getRepo().findAll();
    }

    @Override
    protected String name() {
        return "Специальности";
    }

    @Override
    protected List<PropertyRender<SpecialityEntity, ComponentRenderer<Component, SpecialityEntity>>> getRenders() {
        return RenderLists.SPECIALITY_RENDER;
    }

    @Override
    protected VerticalLayout createContent() {
        return new VerticalLayout();
    }

    @Override
    public void onChecked(String id, SpecialityEntity entity, boolean isChecked, String viewId) {
        if (isChecked) {
            checkedEntities.add(entity);
        }else {
            checkedEntities.remove(entity);
        }
        afterCheck();

    }

    @Override
    public void onCreate() {
        new ChangeSpecialityDialog(entity -> {
            specialityService.getRepo().save(entity);
            Notify.success("Успешно сохранено");
            refresh();
        }, null).open();
    }

    @Override
    public void onUpdate() {
        switcher = false;
        checkedEntities.forEach(entity -> {
            new ChangeSpecialityDialog(entity1 ->{
                specialityService.getRepo().save(entity1);
                turnOffSwitcher();
            },entity).open();
        });

        Notify.success("Успешно обновлены выбранные элементы");

    }

    @Override
    public void onDelete() {

        try {
            specialityService.getRepo().deleteAll(checkedEntities);

            Notify.success("Успешно удалены выбранные элементы");
            refresh();
        }catch (DataIntegrityViolationException ex){
           new ActionDialog(aBoolean -> {
               if(aBoolean){
                   specialityService.deleteAllCollapse(checkedEntities);
                   Notify.warning("Успешно удалены выбранные элементы принудительно");
                   refresh();
               }
           }).open();
        }


    }
}
