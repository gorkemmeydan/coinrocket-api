package com.gorkemmeydan.coinrocketapi.exception;

public class CoinAlreadyExistsInPortfolioException extends Exception {
    public CoinAlreadyExistsInPortfolioException() {
        super();
    }

    public CoinAlreadyExistsInPortfolioException(String message) {
        super(message);
    }

    public CoinAlreadyExistsInPortfolioException(String message, Throwable cause) {
        super(message, cause);
    }
}
