package com.myx.library.retrofit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by mayuxin on 2017/3/23.
 */
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface BaseUrl {
    String host() default "";
    String port() default "";
}
