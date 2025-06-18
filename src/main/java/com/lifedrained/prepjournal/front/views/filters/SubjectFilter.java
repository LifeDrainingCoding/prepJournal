package com.lifedrained.prepjournal.front.views.filters;

import com.lifedrained.prepjournal.repo.entities.SubjectEntity;

import javax.security.auth.Subject;

public class SubjectFilter extends AbstractEntityFilter<SubjectEntity> {
    @Override
    public boolean test(SubjectEntity subject, String s) {
        return subject.getName().toLowerCase().contains(s.toLowerCase());
    }


}
