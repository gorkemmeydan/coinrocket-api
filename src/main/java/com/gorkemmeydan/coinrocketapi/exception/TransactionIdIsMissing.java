package com.gorkemmeydan.coinrocketapi.exception;

public class TransactionIdIsMissing extends Exception{
    public TransactionIdIsMissing() {
        super();
    }

    public TransactionIdIsMissing(String message) {
        super(message);
    }

    public TransactionIdIsMissing(String message, Throwable cause) {
        super(message, cause);
    }
}
