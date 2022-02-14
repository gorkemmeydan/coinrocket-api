package com.gorkemmeydan.coinrocketapi.service.impl;

import com.gorkemmeydan.coinrocketapi.dto.WatchlistDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.WatchList;
import com.gorkemmeydan.coinrocketapi.exception.CoinAlreadyExistsInWatchlistException;
import com.gorkemmeydan.coinrocketapi.exception.CoinDoesNotExistsInUserException;
import com.gorkemmeydan.coinrocketapi.exception.UserDoesNotExistsException;
import com.gorkemmeydan.coinrocketapi.repository.AppUserRepository;
import com.gorkemmeydan.coinrocketapi.repository.WatchlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class WatchlistServiceImplTest {
    private AppUserRepository appUserRepository;
    private WatchlistRepository watchlistRepository;
    private WatchlistServiceImpl watchlistService;

    @BeforeEach
    public void setUp() {
        appUserRepository = mock(AppUserRepository.class);
        watchlistRepository = mock(WatchlistRepository.class);
        watchlistService = new WatchlistServiceImpl(appUserRepository, watchlistRepository);
    }

    @Test
    void testSaveToWatchlist_shouldSaveIfAllReqsAreMet() {
        WatchlistDto request = generateRequest();

        AppUser mockAppUser = mock(AppUser.class);
        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<WatchList> mockWatchList = new ArrayList<>();
        WatchList mockWL= mock(WatchList.class);
        mockWatchList.add(mockWL);

        when(mockAppUser.getWatchList()).thenReturn(mockWatchList);
        when(mockWL.getCoinName()).thenReturn("ethereum");

        assertDoesNotThrow(() -> watchlistService.saveToWatchlist(request));
        verify(watchlistRepository, times(1)).save(any());
    }

    @Test
    void testSaveToWatchlist_shouldThrowIfCoinAlreadyExistInWatchlist() {
        WatchlistDto request = generateRequest();

        AppUser mockAppUser = mock(AppUser.class);
        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<WatchList> mockWatchList = new ArrayList<>();
        WatchList mockWL= mock(WatchList.class);
        mockWatchList.add(mockWL);

        when(mockAppUser.getWatchList()).thenReturn(mockWatchList);
        when(mockWL.getCoinName()).thenReturn(request.getCoinName());

        assertThrows(CoinAlreadyExistsInWatchlistException.class, () -> watchlistService.saveToWatchlist(request));
        verifyNoInteractions(watchlistRepository);
    }

    @Test
    void testSaveToWatchlist_shouldThrowIfUserDoesNotExist() {
        WatchlistDto request = generateRequest();

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(null);

        assertThrows(UserDoesNotExistsException.class, () -> watchlistService.saveToWatchlist(request));
        verifyNoInteractions(watchlistRepository);
    }

    @Test
    void testDeleteFromWatchlist_shouldDeleteIfAllReqsAreMet() {
        WatchlistDto request = generateRequest();

        AppUser mockAppUser = mock(AppUser.class);
        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<WatchList> mockWatchList = new ArrayList<>();
        WatchList mockWL= mock(WatchList.class);
        mockWatchList.add(mockWL);

        when(mockAppUser.getWatchList()).thenReturn(mockWatchList);
        when(mockWL.getCoinName()).thenReturn(request.getCoinName());

        assertDoesNotThrow(() -> watchlistService.deleteFromWatchlist(request));
        verify(watchlistRepository, times(1)).delete(any());
    }

    @Test
    void testDeleteFromWatchlist_shouldThrowIfCoinAlreadyExistInWatchlist() {
        WatchlistDto request = generateRequest();

        AppUser mockAppUser = mock(AppUser.class);
        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<WatchList> mockWatchList = new ArrayList<>();
        WatchList mockWL= mock(WatchList.class);
        mockWatchList.add(mockWL);

        when(mockAppUser.getWatchList()).thenReturn(mockWatchList);
        when(mockWL.getCoinName()).thenReturn("ethereum");

        assertThrows(CoinDoesNotExistsInUserException.class, () -> watchlistService.deleteFromWatchlist(request));
        verifyNoInteractions(watchlistRepository);
    }

    @Test
    void testDeleteFromWatchlist_shouldThrowIfUserDoesNotExist() {
        WatchlistDto request = generateRequest();

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(null);

        assertThrows(UserDoesNotExistsException.class, () -> watchlistService.deleteFromWatchlist(request));
        verifyNoInteractions(watchlistRepository);
    }

    @Test
    void testGetWatchlistOfUser_shouldGetIfAllOk () {
        String email = "test@test.com";

        AppUser mockAppUser = mock(AppUser.class);
        when(appUserRepository.findByEmail(email)).thenReturn(mockAppUser);

        List<WatchList> mockWatchLists = new ArrayList<>();
        WatchList mockPortfolio = mock(WatchList.class);
        mockWatchLists.add(mockPortfolio);

        when(mockAppUser.getWatchList()).thenReturn(mockWatchLists);

        assertDoesNotThrow(() -> watchlistService.getWatchlistOfUser(email));
        List<WatchList> result = watchlistService.getWatchlistOfUser(email);
        assertEquals(mockWatchLists, result);
    }

    @Test
    void testGetWatchlistOfUser_shouldThrowIfUserDoesNotExist () {
        String email = "test@test.com";

        when(appUserRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UserDoesNotExistsException.class, () -> watchlistService.getWatchlistOfUser(email));
    }

    private WatchlistDto generateRequest() {
        WatchlistDto watchlistDto = new WatchlistDto();
        watchlistDto.setEmail("test@test.com");
        watchlistDto.setCoinName("bitcoin");
        return watchlistDto;
    }
}