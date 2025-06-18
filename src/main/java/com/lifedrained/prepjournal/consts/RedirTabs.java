package com.lifedrained.prepjournal.consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RedirTabs {
    NOMENCLATURE("nomen", Routes.NOMENCLATURES_PAGE), IE("import/exopt", Routes.IE_PAGE);

    public final String value,route;
    RedirTabs(String value, String route) {
        this.route = route;
        this.value = value;
    }

    public static List<RedirTabs> all() {
        return new ArrayList<>(Arrays.asList(RedirTabs.values()));
    }
}
