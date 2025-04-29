package com.lifedrained.prepjournal.Utils;

import com.vaadin.flow.component.UI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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
        url = "http://localhost:"+port+url;
        UI.getCurrent().getPage().executeJs("window.open($0, '_blank');", url);
    }

    public static void setTab(String url){
        url = "http://localhost:"+port+url;
        UI.getCurrent().getPage().setLocation(url);
    }


}
