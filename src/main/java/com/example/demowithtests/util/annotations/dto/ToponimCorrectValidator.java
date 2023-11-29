package com.example.demowithtests.util.annotations.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToponimCorrectValidator implements ConstraintValidator<ToponimCorrect, String> {

    private final String regexWithoutComma = "^(([A-Z]{1}[a-z]*){1}([-' ]{1}[A-Z]*[a-z]+)*)+";

    private final String regexWithComma = "([^ ]([A-Z]*[a-z]*){1}([-' ][A-Z]*[a-z]*)*,([A-Z]*[a-z]*))+";

//    private final Pattern patternCorrectToponimName = Pattern.compile(regex);

    @Override
    public void initialize(ToponimCorrect constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String toponim, ConstraintValidatorContext constraintValidatorContext) {

 //       Matcher correct = patternCorrectToponimName.matcher(toponim);

 //       Matcher notAllowed = patternNotAllowed.matcher(toponim.replace("-", ""));
        if (toponim.contains(","))
            return Pattern.matches(regexWithComma, toponim);

        return Pattern.matches(regexWithoutComma, toponim);
    }
}
