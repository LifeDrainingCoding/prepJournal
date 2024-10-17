package com.lifedrained.prepjournal.front.interfaces;

import com.lifedrained.prepjournal.front.views.ControlButtons;

public interface CRUDControl {
    void onDelete(String id);
    void onUpdate(String id);
    void onCreate(String id);

}
