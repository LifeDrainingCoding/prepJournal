package com.lifedrained.prepjournal.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtils {
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
        System.out.println("ldt " + values.get(3));
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
}
