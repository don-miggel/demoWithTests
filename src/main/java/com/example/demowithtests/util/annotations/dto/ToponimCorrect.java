package com.example.demowithtests.util.annotations.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ToponimCorrectValidator.class)
public @interface ToponimCorrect {

    String message() default "Country should have a correct format. If a city consists of a couple of words" +
            " like Los-Angeles, it should be written with a hyphen without any whitespaces both inside and outside!" +
            "If a city contains many hyphens, it should be written like Avon-by-the-Sea";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
