package com.lifedrained.prepjournal.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {
    private static final Logger log = LogManager.getLogger(DateUtils.class);
    public static final String DB_DT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String DB_DATE_FORMAT = "yyyy-MM-dd";

    private static final String LD_FORMAT = "dd.MM.yyyy";
    private static final String LDT_FORMAT = "dd.MM.yyyy HH:mm";
    private static final String LT_FORMAT = "HH:mm";

    public static String getStringFromDateTime(Date date){
        return new SimpleDateFormat(LDT_FORMAT).format(date);
    }
    public static String getStringFromDate(Date date){
        return new SimpleDateFormat("dd.MM.yyyy").format(date);
    }
    public static Date getDateFromString(String stringDate){
        try {
            return new SimpleDateFormat(LD_FORMAT).parse(stringDate);
        }catch (ParseException ex){
            log.error("Error during parsing date  ", ex);
            return new Date();
        }
    }
    public static String getStringFromTime(LocalTime time) {
        return new SimpleDateFormat(LT_FORMAT).format(asDate(time));
    }
    public static Date asDate(LocalTime localTime) {
        return Date.from(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }
    public static String getStringFromDateTime(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern(LDT_FORMAT));
    }
    public static LocalDateTime getLDTFromString(String stringDate) throws DateTimeParseException {
        return LocalDateTime.parse(stringDate, DateTimeFormatter.ofPattern(LDT_FORMAT));
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
    public static String getStringDateTimeFromDate(Date date) {
        return new SimpleDateFormat(LDT_FORMAT).format(date);
    }
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public static String getStringFromDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(LD_FORMAT));
    }
    public static String parseLocalTemporal(Object localTemporal) {
        if (localTemporal instanceof LocalDateTime) {
            return getStringFromDateTime((LocalDateTime) localTemporal);
        }
        if (localTemporal instanceof Date){
            return getStringDateTimeFromDate((Date) localTemporal);
        }
        if (localTemporal instanceof LocalDate) {
            return getStringFromDate((LocalDate) localTemporal);
        }
        if (localTemporal instanceof LocalTime) {
            return getStringFromTime((LocalTime) localTemporal);
        }
        return "";
    }
    public static LocalDate getLDFromString(String stringDate) throws DateTimeParseException {
        return LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(LD_FORMAT));
    }
    public static LocalTime getTimeFromString(String stringTime) throws DateTimeParseException {
        return LocalTime.parse(stringTime, DateTimeFormatter.ofPattern(LT_FORMAT));
    }
    public static int countWeeksBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        long weeks = (daysBetween + 6) / 7;

        return (int) weeks;
    }
}
