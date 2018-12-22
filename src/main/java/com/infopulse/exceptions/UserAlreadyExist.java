package com.infopulse.exceptions;

public class UserAlreadyExist extends RuntimeException {

    public UserAlreadyExist(String message){
        super(message);
    }

}
