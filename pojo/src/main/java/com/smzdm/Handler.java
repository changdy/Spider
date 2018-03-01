package com.smzdm;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Handler {

    String value() default "";

    String regPattern() default "";

    HandlerFunction function() default HandlerFunction.DEFAULT;
}