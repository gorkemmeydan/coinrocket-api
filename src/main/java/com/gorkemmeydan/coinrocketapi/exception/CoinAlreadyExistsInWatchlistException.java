package com.gorkemmeydan.coinrocketapi.exception;

public class CoinAlreadyExistsInWatchlistException extends RuntimeException {
    public CoinAlreadyExistsInWatchlistException() {
        super();
    }

    public CoinAlreadyExistsInWatchlistException(String message) {
        super(message);
    }

    public CoinAlreadyExistsInWatchlistException(String message, Throwable cause) {
        super(message, cause);
    }
}