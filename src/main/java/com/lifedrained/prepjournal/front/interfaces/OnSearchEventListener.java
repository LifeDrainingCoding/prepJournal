package com.lifedrained.prepjournal.front.interfaces;

import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;

import java.util.List;

public interface OnSearchEventListener {
    void onSearchEvent(List<ScheduleEntity> entities );
}
