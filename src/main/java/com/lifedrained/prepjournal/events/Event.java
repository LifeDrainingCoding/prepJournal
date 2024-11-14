package com.lifedrained.prepjournal.events;

import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import lombok.Getter;


@Getter
public abstract class Event {
    private final EventType type;


    public Event(EventType type) {
        this.type = type;
    }

}
