package com.gorkemmeydan.coinrocketapi.service;

import com.gorkemmeydan.coinrocketapi.dto.CoinTransactionDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.CoinTransaction;

import java.util.List;

public interface CoinTransactionService {
    void saveTransactionToCoin(CoinTransactionDto coinTransactionDto);

    void deleteTransactionFromCoin(CoinTransactionDto coinTransactionDto);

    List<CoinTransaction> getTransactionHistoryOfUserForGivenCoin(String email, String coinName);

    boolean checkIfUserExists(String email);

    boolean checkIfCoinExistsInPortfolio(AppUser appUser, String coinName);
}
