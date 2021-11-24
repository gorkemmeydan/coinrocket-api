package com.gorkemmeydan.coinrocketapi.service;

import com.gorkemmeydan.coinrocketapi.dto.CoinTransactionDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.CoinTransaction;
import com.gorkemmeydan.coinrocketapi.exception.CoinDoesNotExistsInPortfolioException;
import com.gorkemmeydan.coinrocketapi.exception.TransactionIdIsMissing;
import com.gorkemmeydan.coinrocketapi.exception.UserDoesNotExistsException;

import java.util.List;

public interface CoinTransactionService {
    AppUser saveTransactionToCoin(CoinTransactionDto coinTransactionDto) throws UserDoesNotExistsException, CoinDoesNotExistsInPortfolioException;

    AppUser deleteTransactionFromCoin(CoinTransactionDto coinTransactionDto) throws UserDoesNotExistsException, CoinDoesNotExistsInPortfolioException, TransactionIdIsMissing;

    List<CoinTransaction> getTransactionHistoryOfUserForGivenCoin(CoinTransactionDto coinTransactionDto) throws UserDoesNotExistsException, CoinDoesNotExistsInPortfolioException;

    boolean checkIfUserExists(String email);

    boolean checkIfCoinExistsInPortfolio(AppUser appUser, String coinName);
}
