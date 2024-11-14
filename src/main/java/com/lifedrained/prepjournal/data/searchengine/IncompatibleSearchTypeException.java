package com.lifedrained.prepjournal.data.searchengine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IncompatibleSearchTypeException extends IllegalArgumentException {
    private static final Logger log = LogManager.getLogger(IncompatibleSearchTypeException.class);

    public IncompatibleSearchTypeException(String message) {
        super(message);
        log.error("Incompatible Type param was passed! ");
    }
}
