package com.lifedrained.prepjournal.Utils;

import com.lifedrained.prepjournal.front.views.ControlButtons;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;

import java.util.LinkedHashMap;

public class OnCheckedEntityHandler<T extends BaseEntity> {
    public OnCheckedEntityHandler(String id, T entity,boolean isChecked, LinkedHashMap<String, T>  map, ControlButtons<T> buttons){
        if(isChecked){
            map.put(id,entity);
        }else {
            map.remove(id);
        }
        buttons.checkButtons(map);
    }
}
