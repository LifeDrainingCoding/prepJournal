package com.lifedrained.prepjournal.events;

import com.lifedrained.prepjournal.data.searchengine.SearchTypes;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;

import java.util.List;

public class SearchEvent<T extends BaseEntity> extends Event {

    private final List<T> entities;

    public SearchEvent(List<T> entities) {
        super(EventType.SEARCH);
        this.entities = entities;

    }
    public List<T> getSearchResult(){
        return entities;
    }
}
