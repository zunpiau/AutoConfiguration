package io.github.zunpiau;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {

    String value() default "";

}
