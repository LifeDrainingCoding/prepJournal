package com.lifedrained.prepjournal.Utils;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.time.Duration;

public class Notify {
    public static void error(String text){
        new Notification(text, (int) Duration.ofSeconds(5).toMillis(),
                Notification.Position.TOP_CENTER){{
            addThemeVariants(NotificationVariant.LUMO_ERROR);
            open();
        }};
    }
    public static void info(String text){
        new Notification(text,(int) Duration.ofSeconds(5).toMillis(),
                Notification.Position.TOP_CENTER){{
            open();
        }};
    }
    public static void warning(String text){
        new Notification(text,(int) Duration.ofSeconds(5).toMillis(),
                Notification.Position.TOP_CENTER){{
            addThemeVariants(NotificationVariant.LUMO_WARNING);
            open();
        }};
    }
    public static void success(String text){
        new Notification(text, (int) Duration.ofSeconds(5).toMillis(), Notification.Position.TOP_CENTER){{
            addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            open();
        }};
    }
    public static void error(String text, int duration, Notification.Position position){
        new Notification(text, (int) Duration.ofSeconds(duration).toMillis(), position){{
            addThemeVariants(NotificationVariant.LUMO_ERROR);
            open();
        }};
    }
}
