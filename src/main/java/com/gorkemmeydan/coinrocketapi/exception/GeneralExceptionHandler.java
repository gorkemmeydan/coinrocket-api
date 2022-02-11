package com.gorkemmeydan.coinrocketapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CoinAlreadyExistsInPortfolioException.class)
    public ResponseEntity<?> coinAlreadyExistsInPortfolioExceptionHandler(CoinAlreadyExistsInPortfolioException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CoinAlreadyExistsInWatchlistException.class)
    public ResponseEntity<?> coinAlreadyExistsInWatchlistExceptionHandler(CoinAlreadyExistsInWatchlistException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CoinDoesNotExistsInPortfolioException.class)
    public ResponseEntity<?> coinDoesNotExistsInPortfolioExceptionHandler(CoinDoesNotExistsInPortfolioException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CoinDoesNotExistsInUserException.class)
    public ResponseEntity<?> coinDoesNotExistsInUserExceptionHandler(CoinDoesNotExistsInUserException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionIdIsMissingException.class)
    public ResponseEntity<?> transactionIdIsMissingExceptionHandler(TransactionIdIsMissingException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> userAlreadyExistsExceptionHandler(UserAlreadyExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDoesNotExistsException.class)
    public ResponseEntity<?> userDoesNotExistsExceptionHandler(UserDoesNotExistsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
