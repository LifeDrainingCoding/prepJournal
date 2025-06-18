package com.lifedrained.prepjournal.Utils;

import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.vaadin.flow.component.HasEnabled;

import java.util.Arrays;
import java.util.List;

public interface ComponentSecurer {
     default  void checkSecurity(int reqLevel, HasEnabled... components) {

        List<HasEnabled> componentsList = Arrays.asList(components);
        CurrentSession session = ContextProvider.getBean(CurrentSession.class);

        componentsList.forEach(component -> {
            if (RoleConsts.valueOf(session.getRole()).ordinal()<reqLevel) {
                component.setEnabled(false);
            }
        });

    }
}
