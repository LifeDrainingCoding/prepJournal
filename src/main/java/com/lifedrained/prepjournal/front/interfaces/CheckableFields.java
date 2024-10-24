package com.lifedrained.prepjournal.front.interfaces;

import com.vaadin.flow.component.notification.Notification;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;

public interface CheckableFields<T> {
    default boolean isFieldsEmpty (List<T> data){
        if (data.stream().anyMatch(new Predicate<Object>() {
            @Override
            public boolean test(Object s) {
                if (s instanceof String){
                    return ((String) s).isEmpty();
                }
                return false;
            }
        })){
            new Notification("Не все поля заполнены!",
                    (int) Duration.ofSeconds(5).toMillis())
            {{
                open();
            }};

            return true;
        }
        return false;
    }
}
