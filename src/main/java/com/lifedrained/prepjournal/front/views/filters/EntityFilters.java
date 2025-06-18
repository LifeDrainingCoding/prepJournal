package com.lifedrained.prepjournal.front.views.filters;

public enum EntityFilters {
    SPECIALITY(new SpecialityFilter()), LOGIN(new LoginFilter()),GROUP(new GroupFilter())
    ,SUBJECT(new SubjectFilter());


    private final AbstractEntityFilter<?> filter;
    <T> EntityFilters(AbstractEntityFilter<T> filter ){
        this.filter = filter;
    }
    public <T> T get(){
        return (T) filter.get();
    }

    public <T> T value(){
        return (T) filter;
    }
}
