package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.Utils.ValidationUtils;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.consts.Exclusions;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.front.views.filters.EntityFilters;
import com.lifedrained.prepjournal.front.views.widgets.RowWithComboBox;
import com.lifedrained.prepjournal.front.views.widgets.RowWithIntField;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.entities.SubjectEntity;
import com.lifedrained.prepjournal.services.SubjectService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import jakarta.persistence.EntityManager;

public class ChangeSubjectDialog extends AbstractDialog<SubjectEntity> {

    private LoginRepo repo;
    private RowWithTxtField name;
    private RowWithComboBox<LoginEntity> master;
    private RowWithIntField cabinet;
    private EntityManager entityManager;

    public ChangeSubjectDialog(OnConfirmListener<SubjectEntity> confirmListener, SubjectEntity entity) {
        super(confirmListener, entity);
        repo = ContextProvider.getBean(LoginRepo.class);
        cabinet = new RowWithIntField("Кабинет проведения дисциплины");
        cabinet.getBody().setMin(0);
        cabinet.getBody().setMax(1000);

        name = new RowWithTxtField("Введите имя дисциплины");
        master = new RowWithComboBox<>("Выберите преподавателя", repo.findByRole(RoleConsts.USER_TIER1.value),
                LoginEntity::getName, EntityFilters.LOGIN.get());

        entityManager = ContextProvider.getBean(EntityManager.class);
        headerLayout.add(name, master, cabinet);

    }

    @Override
    protected ComponentEventListener<ClickEvent<Button>> onOk() {
        return event -> {

            if (entity == null) {
                entity = new SubjectEntity();
            }

            SubjectService subjectService = ContextProvider.getBean(SubjectService.class);

            entity = subjectService.saveDialog(entity,master.getCBoxValue(),name.getFieldText(), cabinet.getValue());
            if (!ValidationUtils.hasValidStrings(entity, Exclusions.EXCLUDES)) {
                Notify.error("Введено некорректное имя преподавателя");
                return;
            }
            if (ValidationUtils.hasAnyNull(entity)) {
                Notify.error("не все поля заполнены");
                return;
            }


            confirmListener.onConfirm(entity);
            close();
        };
    }
}
