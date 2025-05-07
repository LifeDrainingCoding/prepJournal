package com.lifedrained.prepjournal.Utils;


public class JSONValidator {

    private JSONValidator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isValid(String json) {
        return json.startsWith("[{\"");
    }
}
