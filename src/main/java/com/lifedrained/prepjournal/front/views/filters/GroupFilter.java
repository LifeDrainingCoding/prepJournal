package com.lifedrained.prepjournal.front.views.filters;

import com.lifedrained.prepjournal.repo.entities.GroupEntity;

public class GroupFilter extends AbstractEntityFilter<GroupEntity>{
    @Override
    public boolean test(GroupEntity groupEntity, String s) {
        return groupEntity.getName().toLowerCase().contains(s);
    }
}
