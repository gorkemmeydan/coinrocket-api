package com.gorkemmeydan.coinrocketapi.service.impl;

import com.gorkemmeydan.coinrocketapi.dto.WatchlistDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.WatchList;
import com.gorkemmeydan.coinrocketapi.exception.CoinAlreadyExistsInWatchlistException;
import com.gorkemmeydan.coinrocketapi.exception.CoinDoesNotExistsInUserException;
import com.gorkemmeydan.coinrocketapi.exception.UserDoesNotExistsException;
import com.gorkemmeydan.coinrocketapi.repository.AppUserRepository;
import com.gorkemmeydan.coinrocketapi.repository.WatchlistRepository;
import com.gorkemmeydan.coinrocketapi.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WatchlistServiceImpl implements WatchlistService {
    private final AppUserRepository appUserRepository;
    private final WatchlistRepository watchlistRepository;

    @Override
    public void saveToWatchlist(WatchlistDto watchlistDto) {
        // find the user with given email
        if (!checkIfUserExists(watchlistDto.getEmail())) throw new UserDoesNotExistsException("User with given email does not exist");

        // get the app user from db
        AppUser appUser = appUserRepository.findByEmail(watchlistDto.getEmail());

        // check if coin exists in watchlist
        if (checkIfWatchlistExistsInUser(appUser, watchlistDto.getCoinName())) throw new CoinAlreadyExistsInWatchlistException("Coin already exists in watchlist for given user");

        log.info("adding coin {} to user {} ", watchlistDto.getCoinName(), watchlistDto.getEmail());

        // create a new item for the coin watchlist
        WatchList watchList = new WatchList();
        watchList.setAppUser(appUser);
        watchList.setCoinName(watchlistDto.getCoinName());
        watchlistRepository.save(watchList);

        // refresh user to have latest watchlist
        appUserRepository.refresh(appUser);
    }

    @Override
    public void deleteFromWatchlist(WatchlistDto watchlistDto) {
        // find the user with given email
        if (!checkIfUserExists(watchlistDto.getEmail())) throw new UserDoesNotExistsException("User with given email does not exist");

        // get the app user from db
        AppUser appUser = appUserRepository.findByEmail(watchlistDto.getEmail());

        // check if coin exists in watchlist
        if (!checkIfWatchlistExistsInUser(appUser, watchlistDto.getCoinName())) throw new CoinDoesNotExistsInUserException("Coin does not exist in watchlist for given user");

        log.info("removing coin {} from user {} ", watchlistDto.getCoinName(), watchlistDto.getEmail());

        Optional<WatchList> to_be_removed = appUser.getWatchList().stream().filter(item -> item.getCoinName().equals(watchlistDto.getCoinName())).findFirst();

        to_be_removed.ifPresent(watchlistRepository::delete);

    }

    @Override
    public List<WatchList> getWatchlistOfUser(String email) {
        // find the user with given email
        log.info("Getting watchlist for user {}", email);

        if (!checkIfUserExists(email)) throw new UserDoesNotExistsException("User with given email does not exist");


        // get the app user from db
        AppUser appUser = appUserRepository.findByEmail(email);
        return appUser.getWatchList();
    }

    @Override
    public boolean checkIfUserExists(String email) {
        return appUserRepository.findByEmail(email) != null;
    }

    @Override
    public boolean checkIfWatchlistExistsInUser(AppUser appUser, String coinName) {
        return appUser.getWatchList().stream().anyMatch(item -> item.getCoinName().equals(coinName));
    }


}
