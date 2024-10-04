package com.lifedrained.prepjournal.front.interfaces;

import com.lifedrained.prepjournal.repo.entities.LoginEntity;

public interface OnCheckBoxPickedListener {
    void onChecked(String id, LoginEntity entity, boolean isChecked);
}
