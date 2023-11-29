package com.example.demowithtests.util.exception;

public class SameNameUpdException extends RuntimeException{

    public SameNameUpdException(String name, Integer id){
        super("You have entered the same name: "+ name + " for user with id: "+ id);
    }
}
