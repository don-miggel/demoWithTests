package com.example.demowithtests.util.annotations.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorrectStreetValidator.class)
public @interface StreetCorrect {

    String message() default "Please, enter a correct street format. At least, it should contain a house number and a zip code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
