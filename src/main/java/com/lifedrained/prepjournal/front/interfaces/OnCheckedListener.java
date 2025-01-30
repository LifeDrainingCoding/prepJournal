package com.lifedrained.prepjournal.front.interfaces;

import com.lifedrained.prepjournal.repo.entities.BaseEntity;

public interface OnCheckedListener<T> {
    void onChecked(String id, T  entity, boolean isChecked, String viewId);
}
