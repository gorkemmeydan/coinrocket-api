package com.gorkemmeydan.coinrocketapi.service.impl;

import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;
import com.gorkemmeydan.coinrocketapi.dto.UserHoldingsDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.CoinTransaction;
import com.gorkemmeydan.coinrocketapi.entity.Portfolio;
import com.gorkemmeydan.coinrocketapi.entity.WatchList;
import com.gorkemmeydan.coinrocketapi.exception.UserAlreadyExistsException;
import com.gorkemmeydan.coinrocketapi.exception.UserDoesNotExistsException;
import com.gorkemmeydan.coinrocketapi.pojo.CoinTransactionItemPojo;
import com.gorkemmeydan.coinrocketapi.pojo.PortfolioItemPojo;
import com.gorkemmeydan.coinrocketapi.pojo.WatchListItemPojo;
import com.gorkemmeydan.coinrocketapi.repository.AppUserRepository;
import com.gorkemmeydan.coinrocketapi.service.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(AppUserDto appUserDto){

        if(checkIfUserExists(appUserDto.getEmail()))
            throw new UserAlreadyExistsException("User already exists for this email");

        log.info("Saving new user {} to the database", appUserDto.getEmail());

        AppUser appUser = new AppUser();
        appUser.setEmail(appUserDto.getEmail());
        appUser.setFullName(appUserDto.getFullName());
        appUser.setPassword(passwordEncoder.encode(appUserDto.getPassword()));
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);
    }

    @Override
    public UserHoldingsDto getUserHoldings(String email) {
        AppUser appUser = appUserRepository.findByEmail(email);

        if (appUser == null) throw new UserDoesNotExistsException("User with given email does not exists");

        log.info("Returning user holdings for {}", email);

        UserHoldingsDto userHoldingsDto = new UserHoldingsDto();
        userHoldingsDto.setId(appUser.getId());
        userHoldingsDto.setEmail(appUser.getEmail());

        // add watchlist items
        for (WatchList item : appUser.getWatchList())
            userHoldingsDto.getWatchList().add(makeWatchlistPojoFromWatchlistItem(item));

        // add portfolio items
        for (Portfolio item: appUser.getPortfolio()) {
            PortfolioItemPojo portfolioItemPojo = new PortfolioItemPojo();
            portfolioItemPojo.setId(item.getId());
            portfolioItemPojo.setCoinName(item.getCoinName());

            // sort the transaction list by date
            List<CoinTransaction> transactionsByAscendingDate = item.getCoinTransactions();
            transactionsByAscendingDate.sort(Comparator.comparing(CoinTransaction::getTransactionDate));

            // iterate over sorted transactions list to add to transactions and 7 days holdings
            for (CoinTransaction coinTransaction: transactionsByAscendingDate) {
                // add transaction item
                portfolioItemPojo.getCoinTransactions().add(makeCoinTransactionItemPojoFromCoinTransactionItem(coinTransaction));

                // add to 7 days transaction history array if according to date
                // firstly get the current local date
                LocalDate tomorrow = LocalDate.now().plusDays(1);
                // convert the transaction time to local date
                LocalDate transactionLocalDate = coinTransaction.getTransactionDate().
                    toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                double addedValue = coinTransaction.getQuantity();
                if (!coinTransaction.isPositive()) addedValue = -1 * addedValue;

                // update the week by 7 days
                for (int i=6; i >= 0 ; i--) {
                   if(transactionLocalDate.isBefore(tomorrow.minusDays(6-i))) {
                       portfolioItemPojo.addLastWeeksHoldingsByIndex(i, addedValue);
                   }
                }

            }
            // add new portfolio item to portfolio list
            userHoldingsDto.getPortfolio().add(portfolioItemPojo);
        }

        return userHoldingsDto;
    }

    @Override
    public boolean checkIfUserExists(String email) {
        return appUserRepository.findByEmail(email) != null;
    }

    private WatchListItemPojo makeWatchlistPojoFromWatchlistItem(WatchList item) {
        WatchListItemPojo watchListItemPojo = new WatchListItemPojo();
        watchListItemPojo.setId(item.getId());
        watchListItemPojo.setCoinName(item.getCoinName());
        return watchListItemPojo;
    }

    private CoinTransactionItemPojo makeCoinTransactionItemPojoFromCoinTransactionItem(CoinTransaction coinTransaction) {
        CoinTransactionItemPojo coinTransactionItemPojo = new CoinTransactionItemPojo();
        coinTransactionItemPojo.setId(coinTransaction.getId());
        coinTransactionItemPojo.setCoinName(coinTransaction.getCoinName());
        coinTransactionItemPojo.setTransactionDate(coinTransaction.getTransactionDate());
        coinTransactionItemPojo.setPositive(coinTransaction.isPositive());
        coinTransactionItemPojo.setQuantity(coinTransaction.getQuantity());
        return coinTransactionItemPojo;
    }
}
