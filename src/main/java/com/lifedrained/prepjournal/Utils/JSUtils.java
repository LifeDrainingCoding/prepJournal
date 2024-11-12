package com.lifedrained.prepjournal.Utils;

import com.vaadin.flow.component.UI;

public class JSUtils {
    public static void openNewTab(String url){
        url = "http://localhost:8080"+url;
        UI.getCurrent().getPage().executeJs("window.open($0, '_blank');", url);
    }
}
