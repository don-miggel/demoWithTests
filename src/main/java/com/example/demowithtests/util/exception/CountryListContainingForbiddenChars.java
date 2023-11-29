package com.example.demowithtests.util.exception;

import java.util.List;

public class CountryListContainingForbiddenChars extends RuntimeException{

    public CountryListContainingForbiddenChars(List<Character> illegalCharacters){

        super("Countries list contains following illegal characters : '"+ illegalCharacters.toString() + "'. " +
                "Countries list should contain a comma as a delimeter and/or a hyphen if " +
                "a country name contains it!");
    }
}
