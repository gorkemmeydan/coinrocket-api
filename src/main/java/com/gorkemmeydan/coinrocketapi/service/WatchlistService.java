package com.gorkemmeydan.coinrocketapi.service;

import com.gorkemmeydan.coinrocketapi.dto.WatchlistDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.WatchList;

import java.util.List;

public interface WatchlistService {
    void saveToWatchlist(WatchlistDto watchlistDto);

    void deleteFromWatchlist(WatchlistDto watchlistDto);

    List<WatchList> getWatchlistOfUser(String email);

    boolean checkIfUserExists(String email);

    boolean checkIfWatchlistExistsInUser(AppUser appUser, String coinName);
}
