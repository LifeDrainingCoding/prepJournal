package com.lifedrained.prepjournal.events;

import lombok.Getter;

public abstract class Event {
    @Getter
    private EventType type;

    public Event(EventType type) {
        this.type = type;
    }

    private void setType(EventType type){
        this.type = type;
    }

}
