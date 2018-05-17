/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    /**
     * @author Juergen Hoeller
     * @author Rob Harrop
     * @author Rod Johnson
     * @author Costin Leau
     * @author Sam Brannen
     * @author Chris Beams
     * copy from @see <a href="https://docs.spring.io/spring-framework/docs/5.0.5.RELEASE/javadoc-api/org/springframework/util/ReflectionUtils.html#makeAccessible-java.lang.reflect.Field-'>ReflectionUtils.html</a>
     */
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
