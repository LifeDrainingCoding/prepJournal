package com.lifedrained.prepjournal.front.views.filters;

import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.vaadin.flow.component.combobox.ComboBox;

public class LoginFilter extends AbstractEntityFilter<LoginEntity>  {

    @Override
    public boolean test(LoginEntity loginEntity, String s) {
        return loginEntity.getName().toLowerCase().contains(s.toLowerCase());
    }
}
