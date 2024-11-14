package com.lifedrained.prepjournal.data.searchengine;

import com.lifedrained.prepjournal.events.SearchEvent;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;

import java.util.List;

public interface OnSearchEventListener<T extends BaseEntity> {
    void onSearchEvent(SearchEvent<T> event);
}
