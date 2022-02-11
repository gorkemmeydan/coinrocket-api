package com.gorkemmeydan.coinrocketapi.exception;

public class CoinDoesNotExistsInUserException extends RuntimeException {
    public CoinDoesNotExistsInUserException() {
        super();
    }

    public CoinDoesNotExistsInUserException(String message) {
        super(message);
    }

    public CoinDoesNotExistsInUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
