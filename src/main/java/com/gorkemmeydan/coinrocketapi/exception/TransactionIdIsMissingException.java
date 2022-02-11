package com.gorkemmeydan.coinrocketapi.exception;

public class TransactionIdIsMissingException extends RuntimeException {
    public TransactionIdIsMissingException() {
        super();
    }

    public TransactionIdIsMissingException(String message) {
        super(message);
    }

    public TransactionIdIsMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
