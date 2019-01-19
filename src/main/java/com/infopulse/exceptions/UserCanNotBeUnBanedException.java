package com.infopulse.exceptions;

public class UserCanNotBeUnBanedException extends RuntimeException {
    public UserCanNotBeUnBanedException(String message){
        super(message);
    }
}
