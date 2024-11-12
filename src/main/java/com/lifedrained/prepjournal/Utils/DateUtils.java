package com.lifedrained.prepjournal.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    private static final Logger log = LogManager.getLogger(DateUtils.class);

    public static String getStringFromDateTime(Date date){
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date);
    }
    public static String getStringFromDate(Date date){
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }
    public static Date getDateFromString(String stringDate){
        try {
            return new SimpleDateFormat("dd.MM.yyyy").parse(stringDate);
        }catch (ParseException ex){
            log.error("Error during parsing date  ", ex);
            return new Date();
        }
    }
    public static LocalDateTime asLocalDateTime(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    public static Date asDate(LocalDateTime ldt){
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
