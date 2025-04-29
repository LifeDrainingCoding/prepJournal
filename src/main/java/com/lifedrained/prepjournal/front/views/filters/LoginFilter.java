package com.lifedrained.prepjournal.front.views.filters;

import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.vaadin.flow.component.combobox.ComboBox;

public class LoginFilter extends AbstractEntityFilter<LoginEntity>  {

    @Override
    public  ComboBox.ItemFilter<LoginEntity> get(){
        return (loginEntity, s) -> loginEntity.getName().toLowerCase().contains(s.toLowerCase());
    }


}
