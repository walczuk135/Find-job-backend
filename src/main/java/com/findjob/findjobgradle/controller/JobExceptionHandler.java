package com.findjob.findjobgradle.controller;


public class JobExceptionHandler extends RuntimeException {
    public JobExceptionHandler(String message) {
        super(message);
    }
}
