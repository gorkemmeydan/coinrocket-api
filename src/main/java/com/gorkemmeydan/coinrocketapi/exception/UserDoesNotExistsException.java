package com.gorkemmeydan.coinrocketapi.exception;

public class UserDoesNotExistsException extends Exception {

    public UserDoesNotExistsException() {
        super();
    }

    public UserDoesNotExistsException(String message) {
        super(message);
    }

    public UserDoesNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}