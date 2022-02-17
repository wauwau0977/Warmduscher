package com.x8ing.thsensor.thserver.utils;

public class ThException extends RuntimeException {

    public ThException(String message) {
        super(message);
    }

    public ThException(String message, Throwable cause) {
        super(message, cause);
    }
}
