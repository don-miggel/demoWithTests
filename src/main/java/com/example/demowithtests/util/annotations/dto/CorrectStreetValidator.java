package com.example.demowithtests.util.annotations.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CorrectStreetValidator implements ConstraintValidator<StreetCorrect, String> {

    private final static String streetRegex = "([\\d]{2,}-?([\\d]{2,})*) [A-Za-z\\d\\s,\\.\\w]+[/-]?[\\d\\-\\w]+";

    @Override
    public boolean isValid(String street, ConstraintValidatorContext constraintValidatorContext) {

        return Pattern.matches(streetRegex, street);
    }
}
