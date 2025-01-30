package com.lifedrained.prepjournal.data.searchengine;

import com.lifedrained.prepjournal.events.SearchEvent;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;

public interface OnSearchEventListener<T extends BaseEntity> {
    void onSearchEvent(SearchEvent<T> event);
}
