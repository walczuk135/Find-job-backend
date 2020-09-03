package com.findjob.findjobgradle.service;

public class UserExceptionHandler extends RuntimeException{
    public UserExceptionHandler(String message) {
        super(message);
    }
}
