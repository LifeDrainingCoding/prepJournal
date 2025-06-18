package com.lifedrained.prepjournal.Utils;

import com.vaadin.flow.component.UI;
import elemental.json.JsonString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class JSUtils {
    private static final Logger log = LogManager.getLogger(JSUtils.class);
    private static String port;

    @Autowired
    private Environment env;

    @EventListener(ApplicationReadyEvent.class)
    private void setPort(){
        if(env != null){
            port = env.getProperty("server.port");
        }else {
            log.error("Environment is null");
        }

    }


    public static void openNewTab(String url){
        log.info("tab port {}",port);
        final AtomicReference<String> urlRef = new AtomicReference<>(url);
        UI.getCurrent().getPage().executeJs("return window.location.origin").then(String.class,jsonValue -> {
                urlRef.set(jsonValue+url) ;
            UI.getCurrent().getPage().executeJs("window.open($0, '_blank');", urlRef.get());
        });

    }

    public static void setTab(String url){
        final AtomicReference<String> urlRef = new AtomicReference<>(url);
        UI.getCurrent().getPage().executeJs("return window.location.origin").then(String.class,jsonValue -> {
            urlRef.set(jsonValue+url) ;
            UI.getCurrent().getPage().setLocation(urlRef.get());
        });

    }


}
