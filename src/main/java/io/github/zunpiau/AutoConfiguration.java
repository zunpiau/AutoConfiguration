package io.github.zunpiau;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AutoConfiguration {

    String prefix() default "";

    String value();

    boolean autoRefresh() default false;

}
