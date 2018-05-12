package io.github.zunpiau.utils;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtil {

    @Nullable
    public static <T extends Annotation> T getAnnotation(AnnotatedElement element, Class<T> annotationClass) {
        try {
            return element.getAnnotation(annotationClass);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static void setValue(Field field, Object target, String value, Class valueClass) throws IllegalAccessException {
        setAccessible(field);
        field.set(target, cast(value, valueClass));
    }

    private static void setAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    private static Object cast(String value, Class clazz) {
        if (clazz.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (clazz.equals(long.class)) {
            return Long.parseLong(value);
        } else if (clazz.equals(double.class)) {
            return Double.parseDouble(value);
        } else if (clazz.equals(float.class)) {
            return Float.parseFloat(value);
        } else if (clazz.equals(boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (clazz.equals(byte.class)) {
            return Byte.parseByte(value);
        } else if (clazz.equals(char.class)) {
            if (value.length() > 1)
                throw new IllegalArgumentException("[" + value + "has too mach character");
            return value.charAt(0);
        } else if (clazz.equals(short.class)) {
            return Short.parseShort(value);
        } else return value;
    }

}
