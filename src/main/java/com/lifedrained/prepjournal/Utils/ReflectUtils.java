package com.lifedrained.prepjournal.Utils;

import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.lifedrained.prepjournal.repo.entities.SubjectEntity;
import org.apache.commons.collections4.IterableUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtils {
    private static final Logger log = LogManager.getLogger(ReflectUtils.class);

    public static <T> Object parseArgument(Class<T> clazz , String value) {
        value = value.trim().replaceAll("[\r\n]", "");
        if (clazz == Boolean.class || clazz == boolean.class) {
            return Boolean.parseBoolean(value);
        }
        if (clazz == Integer.class || clazz == int.class) {

            if (value.contains("."))
                value = value.substring( 0,value.indexOf(".") );
            return Integer.parseInt(value);
        }

        if (clazz == Double.class || clazz == double.class) {
            return Double.parseDouble(value);
        }
        if (clazz == Float.class || clazz == float.class) {
            return Float.parseFloat(value);
        }
        if (clazz == String.class) {
            return value;
        }
        if (clazz == Long.class || clazz == long.class) {
            if (value.contains("."))
                value = value.substring( 0,value.indexOf(".") );
            return Long.parseLong(value);
        }

        if (clazz == LocalDateTime.class ){
            StringBuilder sb = new StringBuilder(value);
            if (!value.contains(":")){

                sb.append(" 00:00");
            }
            return DateUtils.getLDTFromString(sb.toString());
        }
        if (clazz == LocalDate.class ){
            return DateUtils.getLDFromString(value);
        }
        if (clazz == LocalTime.class ){
            return DateUtils.getTimeFromString(value);
        }
        return value;
    }


    public static  <T> String fieldToString( Field field, Object obj ) throws IllegalAccessException {
        field.setAccessible(true);
        Object value =  field.get(obj);
        String parsedValue = DateUtils.parseLocalTemporal(value);
        if (parsedValue.isEmpty()){
            return String.valueOf(value);
        }
        return parsedValue;
    }

    public static List<Object> formArgs(List<Field> fields , List<String> values) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < fields.size(); i++) {
            String s = values.get(i);
            Field f = fields.get(i);
            System.out.println("parsing " + s + " for " + f.getType().getSimpleName());
            list.add(parseArgument(f.getType(), s));
        }
        return list;
    }
    public static Constructor<?> getNonEmptyConstructor(Class<?> clazz) {
        List<Constructor<?>> constructors = new ArrayList<>(List.of(clazz.getConstructors()));
        constructors.removeIf(constructor -> {
            System.out.println("Constructor param count: " + constructor.getParameterCount());
            return constructor.getParameterCount() <= 0;
        });
        return constructors.get(0);
    }
    public static String getEntityName(List<Field> fields, BaseEntity entity) throws IllegalAccessException {
        fields.removeIf(field -> {
            if (field.getName().toLowerCase().contains("name") || field.getName().toLowerCase().contains("subject") ) {

                return false;
            }
            return true;

        });



        log.info("{} fields found", fields.size());

        Field subjectField = IterableUtils.find(fields, field ->
                field.getName().toLowerCase().contains("subject"));

        if (subjectField != null) {
            subjectField.setAccessible(true);

            try {
                return ((SubjectEntity) subjectField.get(entity)).getName();
            }catch (ClassCastException e){
                log.info("{} field wrongly associated with subject", subjectField.getName());
            }
        }

        fields.get(0).setAccessible(true);
        return (String) fields.get(0).get(entity);



    }
}
