package com.gorkemmeydan.coinrocketapi.service;

import com.gorkemmeydan.coinrocketapi.dto.WatchlistDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.WatchList;
import com.gorkemmeydan.coinrocketapi.exception.CoinAlreadyExistsInWatchlistException;
import com.gorkemmeydan.coinrocketapi.exception.CoinDoesNotExistsInUserException;
import com.gorkemmeydan.coinrocketapi.exception.UserDoesNotExistsException;

import java.util.List;

public interface WatchlistService {
    AppUser saveToWatchlist(WatchlistDto watchlistDto) throws UserDoesNotExistsException, CoinAlreadyExistsInWatchlistException;

    AppUser deleteFromWatchlist(WatchlistDto watchlistDto) throws UserDoesNotExistsException, CoinDoesNotExistsInUserException;

    List<WatchList> getWatchlistOfUser(String email) throws UserDoesNotExistsException;

    boolean checkIfUserExists(String email);

    boolean checkIfWatchlistExistsInUser(AppUser appUser, String coinName);
}
