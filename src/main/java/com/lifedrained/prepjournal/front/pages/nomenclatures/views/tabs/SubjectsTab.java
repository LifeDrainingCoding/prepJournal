package com.lifedrained.prepjournal.front.pages.nomenclatures.views.tabs;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.consts.RenderLists;
import com.lifedrained.prepjournal.consts.StringConsts;
import com.lifedrained.prepjournal.front.renders.PropertyRender;
import com.lifedrained.prepjournal.front.views.dialogs.ActionDialog;
import com.lifedrained.prepjournal.front.views.dialogs.ChangeSubjectDialog;
import com.lifedrained.prepjournal.repo.SubjectRepo;
import com.lifedrained.prepjournal.repo.entities.SubjectEntity;
import com.lifedrained.prepjournal.services.SubjectService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public class SubjectsTab extends AbstractTab<SubjectEntity>{
    private static final Logger log = LogManager.getLogger(SubjectsTab.class);
    private SubjectService subjectService;

    public SubjectsTab() {
        super(SubjectEntity.class);

    }

    @Override
    protected List<String> getCrudNames() {
        return StringConsts.GeneralCRUDNames;
    }

    @Override
    protected List<PropertyRender<SubjectEntity, ComponentRenderer<Component, SubjectEntity>>> getRenders() {
        return RenderLists.SUBJECT_RENDER;
    }

    @Override
    protected List<SubjectEntity> getItems() {
        subjectService = ContextProvider.getApplicationContext().getBean(SubjectService.class);
        return subjectService.getRepo().findAll();
    }

    @Override
    protected String name() {
        return "Дисциплины";
    }

    @Override
    protected VerticalLayout createContent() {
        return new VerticalLayout();
    }

    @Override
    public void onChecked(String id, SubjectEntity entity, boolean isChecked, String viewId) {
        if (isChecked) {
            checkedEntities.add(entity);
        }else{
            checkedEntities.remove(entity);
        }
        afterCheck();

    }

    @Override
    public void onCreate() {
        new ChangeSubjectDialog(entity -> {
            log.error("saved subject id "+ subjectService.getRepo().save(entity).getId());
            Notify.success("Успешно добавлено");
            refresh();
        }, null).open();
    }

    @Override
    public void onUpdate() {

        checkedEntities.forEach(entity -> {

            new ChangeSubjectDialog(entity1 -> {
                subjectService.getRepo().save(entity1);
                turnOffSwitcher();
                Notify.success("Успешно обновлены выбранные элементы");
            },  entity).open();
        });




    }

    @Override
    public void onDelete() {
        try {
            subjectService.deleteAll(checkedEntities);

            refresh();
            Notify.success("Успешно удалены выбранные элементы");
        }catch (DataIntegrityViolationException ex){
            new ActionDialog(aBoolean -> {
                if(aBoolean){
                    subjectService.deleteAllCollapse(checkedEntities);
                    Notify.warning("Успешно удалены выбранные элементы принудительно");
                    refresh();
                }
            }).open();
        }
    }
}
