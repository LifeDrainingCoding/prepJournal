package com.lifedrained.prepjournal.front.views;

import com.lifedrained.prepjournal.repo.entities.BaseEntity;

public interface CRUDListener<T extends BaseEntity> {

    void onCreate();
    void onUpdate();
    void onDelete();
}
