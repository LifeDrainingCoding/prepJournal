package com.lifedrained.prepjournal.front.views.filters;

import com.lifedrained.prepjournal.repo.entities.SpecialityEntity;

public class SpecialityFilter extends AbstractEntityFilter<SpecialityEntity>{
    @Override
    public boolean test(SpecialityEntity entity, String s) {
        return entity.getName().toLowerCase().contains(s.toLowerCase());
    }
}
