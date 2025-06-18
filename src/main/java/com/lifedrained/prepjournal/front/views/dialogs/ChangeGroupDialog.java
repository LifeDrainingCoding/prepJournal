package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.Utils.ValidationUtils;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.consts.Exclusions;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.front.views.filters.EntityFilters;
import com.lifedrained.prepjournal.front.views.filters.LoginFilter;
import com.lifedrained.prepjournal.front.views.widgets.RowWithComboBox;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.SpecialityRepo;
import com.lifedrained.prepjournal.repo.entities.GroupEntity;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.entities.SpecialityEntity;
import com.lifedrained.prepjournal.services.GroupService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

public class ChangeGroupDialog extends AbstractDialog<GroupEntity> {

    private LoginRepo loginRepo;
    private SpecialityRepo specialityRepo;
    private RowWithTxtField groupName;
    private RowWithComboBox<LoginEntity> master;
    private RowWithComboBox<SpecialityEntity> speciality;
    public ChangeGroupDialog(OnConfirmListener<GroupEntity> confirmListener, GroupEntity entity) {
        super(confirmListener, entity);
        loginRepo = ContextProvider.getBean(LoginRepo.class);
        specialityRepo = ContextProvider.getBean(SpecialityRepo.class);

        groupName = new RowWithTxtField("Название группы");

        master = new RowWithComboBox<>("Выберите куратора", loginRepo.findByRole(RoleConsts.USER_TIER1.value),
                LoginEntity::getName, new LoginFilter());
        speciality =  new RowWithComboBox<>("Выберите специальность группы", specialityRepo.findAll(),
                SpecialityEntity::getName, EntityFilters.SPECIALITY.get());

        headerLayout.add(groupName, master, speciality);


    }

    @Override
    protected ComponentEventListener<ClickEvent<Button>> onOk() {
        return event -> {

            GroupService groupService = ContextProvider.getBean(GroupService.class);

             entity = groupService.saveDialogGroup(entity,master.getCBoxValue(),
                    speciality.getCBoxValue(), groupName.getFieldText());

            if (ValidationUtils.hasAnyNull(entity) ) {
                Notify.error("не все поля заполнены");
                return;
            }

            if (!ValidationUtils.hasValidStrings(entity, Exclusions.EXCLUDES)) {
                Notify.error("неправильно заполнены поля");
                return;
            }

            confirmListener.onConfirm(entity);
            close();
        };
    }
}
