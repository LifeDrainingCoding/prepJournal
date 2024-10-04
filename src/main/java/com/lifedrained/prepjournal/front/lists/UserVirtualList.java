package com.lifedrained.prepjournal.front.lists;

import com.lifedrained.prepjournal.front.lists.renders.UsersRender;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.vaadin.flow.component.virtuallist.VirtualList;

import java.util.List;

public class UserVirtualList extends VirtualList<LoginEntity> {
    public UserVirtualList(List<LoginEntity> entities, UsersRender render){
        setItems(entities);
        setRenderer(render);
    }
}
