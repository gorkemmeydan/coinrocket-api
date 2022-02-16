package com.gorkemmeydan.coinrocketapi.service.impl;

import com.gorkemmeydan.coinrocketapi.dto.CoinTransactionDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.CoinTransaction;
import com.gorkemmeydan.coinrocketapi.entity.Portfolio;
import com.gorkemmeydan.coinrocketapi.exception.CoinDoesNotExistsInPortfolioException;
import com.gorkemmeydan.coinrocketapi.exception.TransactionIdIsMissingException;
import com.gorkemmeydan.coinrocketapi.exception.UserDoesNotExistsException;
import com.gorkemmeydan.coinrocketapi.repository.AppUserRepository;
import com.gorkemmeydan.coinrocketapi.repository.CoinTransactionRepository;
import com.gorkemmeydan.coinrocketapi.service.CoinTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CoinTransactionServiceImpl implements CoinTransactionService {
    private final AppUserRepository appUserRepository;
    private final CoinTransactionRepository coinTransactionRepository;

    @Override
    public void saveTransactionToCoin(CoinTransactionDto coinTransactionDto) {
        // get the app user from db
        AppUser appUser = appUserRepository.findByEmail(coinTransactionDto.getEmail());

        // find the user with given email
        if (appUser == null) throw new UserDoesNotExistsException("User with given email does not exist");

        // get the portfolio from the user
        Optional<Portfolio> portfolio = appUser.getPortfolio().stream().filter(item -> item.getCoinName().equals(coinTransactionDto.getCoinName())).findFirst();

        if (!portfolio.isPresent()) throw new CoinDoesNotExistsInPortfolioException("Coin does not exist in portfolio for given user");

        log.info("adding transaction to coin {} to portfolio of user {} ", coinTransactionDto.getCoinName(), coinTransactionDto.getEmail());

        // create a new transaction for the portfolio coin
        CoinTransaction coinTransaction = new CoinTransaction();
        coinTransaction.setCoinName(coinTransactionDto.getCoinName());

        // create a new date object from unix timestamp
        Date transactionDate = new java.util.Date(coinTransactionDto.getUnixTransactionDate()*1000L);
        coinTransaction.setTransactionDate(transactionDate);
        coinTransaction.setQuantity(coinTransactionDto.getQuantity());
        coinTransaction.setPositive(coinTransactionDto.getisPositive());
        coinTransaction.setPortfolio(portfolio.get());
        coinTransactionRepository.save(coinTransaction);

        // refresh user to have latest watchlist
        appUserRepository.refresh(appUser);
    }

    @Override
    public void deleteTransactionFromCoin(CoinTransactionDto coinTransactionDto) {
        // get the app user from db
        AppUser appUser = appUserRepository.findByEmail(coinTransactionDto.getEmail());

        // find the user with given email
        if (appUser == null) throw new UserDoesNotExistsException("User with given email does not exist");

        // check if coin exists in watchlist
        if (!checkIfCoinExistsInPortfolio(appUser, coinTransactionDto.getCoinName())) throw new CoinDoesNotExistsInPortfolioException("Coin does not exist in portfolio for given user");

        // we should delete the transaction with id, since there might me duplicate transactions that are correct
        if (coinTransactionDto.getId() == null) throw new TransactionIdIsMissingException("Transaction ID should be given for delete operation");

        log.info("removing transaction {} with id {} from user {} ", coinTransactionDto.getCoinName(), coinTransactionDto.getId(), coinTransactionDto.getEmail());

        coinTransactionRepository.deleteById(coinTransactionDto.getId());
    }

    @Override
    public List<CoinTransaction> getTransactionHistoryOfUserForGivenCoin(CoinTransactionDto coinTransactionDto) {
        // get the app user from db
        AppUser appUser = appUserRepository.findByEmail(coinTransactionDto.getEmail());

        if (appUser == null) throw new UserDoesNotExistsException("User with given email does not exist");

        Optional<Portfolio> coin_portfolio = appUser.getPortfolio().stream().filter(item -> item.getCoinName().equals(coinTransactionDto.getCoinName())).findFirst();

        // check if coin exists in watchlist
        if (!coin_portfolio.isPresent()) throw new CoinDoesNotExistsInPortfolioException("Coin does not exist in portfolio for given user");

        // find the user with given email
        log.info("Getting transaction history for user {} for the coin {}", coinTransactionDto.getEmail(), coinTransactionDto.getCoinName());

        return coin_portfolio.get().getCoinTransactions();
    }

    @Override
    public boolean checkIfUserExists(String email) {
        return appUserRepository.findByEmail(email) != null;
    }

    @Override
    public boolean checkIfCoinExistsInPortfolio(AppUser appUser, String coinName) {
        return appUser.getPortfolio().stream().anyMatch(item -> item.getCoinName().equals(coinName));
    }
}
