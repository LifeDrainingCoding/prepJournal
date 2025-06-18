package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.repo.entities.BaseEntity;

public interface OnConfirmListener<T extends BaseEntity> {
    void onConfirm(T entity);
}
