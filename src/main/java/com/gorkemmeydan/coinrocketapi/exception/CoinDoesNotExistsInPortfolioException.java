package com.gorkemmeydan.coinrocketapi.exception;

public class CoinDoesNotExistsInPortfolioException extends RuntimeException {
    public CoinDoesNotExistsInPortfolioException() {
        super();
    }

    public CoinDoesNotExistsInPortfolioException(String message) {
        super(message);
    }

    public CoinDoesNotExistsInPortfolioException(String message, Throwable cause) {
        super(message, cause);
    }
}