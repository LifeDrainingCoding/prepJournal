package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.front.views.widgets.RowWithTxtField;
import com.lifedrained.prepjournal.repo.entities.SpecialityEntity;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import org.checkerframework.checker.units.qual.N;

public class ChangeSpecialityDialog extends AbstractDialog<SpecialityEntity> {
    private RowWithTxtField name, code;
    public ChangeSpecialityDialog(OnConfirmListener<SpecialityEntity> confirmListener, SpecialityEntity entity) {
        super(confirmListener, entity);

        name =  new RowWithTxtField("Введите название специальности");
        code =  new RowWithTxtField("Введите код специальности");
        code.getBody().addValueChangeListener(event -> {
            String value = event.getValue();
            if (value != null && !value.matches("[0-9.]*")) {
                code.getBody().setValue(value.replaceAll("[^0-9.]", ""));
            }
        });

        headerLayout.add(name,code);

    }

    @Override
    protected ComponentEventListener<ClickEvent<Button>> onOk() {
        return event -> {

            if (entity == null) {
                entity = new SpecialityEntity();
            }

            entity.setName(name.getFieldText());
            entity.setCode(code.getFieldText());

            String nameS,codeS;
            nameS = name.getFieldText();
            codeS = code.getFieldText();

            if (nameS == null || nameS.isEmpty()){
                Notify.error("Не заполнено поле с наименованием специальности");
                return;
            }
            if (codeS == null || codeS.isEmpty()){
                Notify.error("Не заполнено поле с кодом специальности");
                return;
            }


            confirmListener.onConfirm(entity);
            close();
        };
    }
}
