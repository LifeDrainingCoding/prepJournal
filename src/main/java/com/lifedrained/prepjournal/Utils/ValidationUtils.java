package com.lifedrained.prepjournal.Utils;

import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import jakarta.validation.Validation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.util.Arrays;
import java.util.List;

public class ValidationUtils {


    public static <T extends BaseEntity> boolean hasAnyNull(T entity) {
        return !Validation.buildDefaultValidatorFactory().getValidator().validate(entity).isEmpty();
    }
    public static boolean hasValidStrings(Object object, String... excludedFields) {
        List<String> excluded = Arrays.asList(excludedFields);

        return Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> field.getType() == String.class) // Только строковые поля
                .filter(field -> !excluded.contains(field.getName())) // Исключаем указанные
                .noneMatch(field -> {
                    ReflectionUtils.makeAccessible(field);
                    String value = (String) ReflectionUtils.getField(field, object);
                    return StringUtils.isBlank(value); // Проверка на null, "" и "   "
                });
    }
}
