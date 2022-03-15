package com.gorkemmeydan.coinrocketapi.service.impl;

import com.gorkemmeydan.coinrocketapi.dto.CoinTransactionDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.Portfolio;
import com.gorkemmeydan.coinrocketapi.exception.CoinDoesNotExistsInPortfolioException;
import com.gorkemmeydan.coinrocketapi.exception.TransactionIdIsMissingException;
import com.gorkemmeydan.coinrocketapi.exception.UserDoesNotExistsException;
import com.gorkemmeydan.coinrocketapi.repository.AppUserRepository;
import com.gorkemmeydan.coinrocketapi.repository.CoinTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class CoinTransactionServiceImplTest {
    private AppUserRepository appUserRepository;
    private CoinTransactionRepository coinTransactionRepository;
    private CoinTransactionServiceImpl coinTransactionService;

    @BeforeEach
    public void setUp() {
        appUserRepository = mock(AppUserRepository.class);
        coinTransactionRepository = mock(CoinTransactionRepository.class);
        coinTransactionService = new CoinTransactionServiceImpl(appUserRepository, coinTransactionRepository);
    }

    @Test
    void testSaveTransactionToCoin_shouldSaveTransactionIfUserAndPortfolioExists() {
        CoinTransactionDto request = generateRequestData();

        AppUser mockAppUser = mock(AppUser.class);

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<Portfolio> mockPortfolioList = new ArrayList<>();
        Portfolio mockPortfolio = mock(Portfolio.class);
        mockPortfolioList.add(mockPortfolio);

        when(mockAppUser.getPortfolio()).thenReturn(mockPortfolioList);
        when(mockPortfolio.getCoinName()).thenReturn("bitcoin");

        assertDoesNotThrow(() -> coinTransactionService.saveTransactionToCoin(request));
    }

    @Test
    void testSaveTransactionToCoin_shouldThrowIfUserDoesNotExits() {
        CoinTransactionDto request = generateRequestData();

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(null);

        assertThrows(UserDoesNotExistsException.class, () -> coinTransactionService.saveTransactionToCoin(request));
        verifyNoInteractions(coinTransactionRepository);
    }

    @Test
    void testSaveTransactionToCoin_shouldThrowIfCoinDoesNotExistInPortfolio() {
        CoinTransactionDto request = generateRequestData();

        AppUser mockAppUser = mock(AppUser.class);

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<Portfolio> emptyList = new ArrayList<>();
        when(mockAppUser.getPortfolio()).thenReturn(emptyList);

        assertThrows(CoinDoesNotExistsInPortfolioException.class, () -> coinTransactionService.saveTransactionToCoin(request));
        verifyNoInteractions(coinTransactionRepository);
    }

    @Test
    void testDeleteTransactionFromCoin_shouldDeleteTransactionIfUserAndPortfolioExists() {
        CoinTransactionDto request = generateRequestData();

        AppUser mockAppUser = mock(AppUser.class);

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<Portfolio> mockPortfolioList = new ArrayList<>();
        Portfolio mockPortfolio = mock(Portfolio.class);
        mockPortfolioList.add(mockPortfolio);

        when(mockAppUser.getPortfolio()).thenReturn(mockPortfolioList);
        when(mockPortfolio.getCoinName()).thenReturn("bitcoin");

        assertDoesNotThrow(() -> coinTransactionService.deleteTransactionFromCoin(request));
    }

    @Test
    void testDeleteTransactionFromCoin_shouldThrowIfUserDoesNotExist() {
        CoinTransactionDto request = generateRequestData();

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(null);

        assertThrows(UserDoesNotExistsException.class, () -> coinTransactionService.deleteTransactionFromCoin(request));
        verifyNoInteractions(coinTransactionRepository);
    }

    @Test
    void testDeleteTransactionFromCoin_shouldThrowIfPortfolioDoesNotExist() {
        CoinTransactionDto request = generateRequestData();

        AppUser mockAppUser = mock(AppUser.class);

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<Portfolio> emptyList = new ArrayList<>();
        when(mockAppUser.getPortfolio()).thenReturn(emptyList);

        assertThrows(CoinDoesNotExistsInPortfolioException.class, () -> coinTransactionService.deleteTransactionFromCoin(request));
        verifyNoInteractions(coinTransactionRepository);
    }

    @Test
    void testDeleteTransactionFromCoin_shouldThrowIfTransactionIdIsMissing() {
        CoinTransactionDto request = generateRequestData();

        AppUser mockAppUser = mock(AppUser.class);

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<Portfolio> mockPortfolioList = new ArrayList<>();
        Portfolio mockPortfolio = mock(Portfolio.class);
        mockPortfolioList.add(mockPortfolio);

        when(mockAppUser.getPortfolio()).thenReturn(mockPortfolioList);
        when(mockPortfolio.getCoinName()).thenReturn("bitcoin");

        // delete the id from the request
        request.setId(null);
        assertThrows(TransactionIdIsMissingException.class, () -> coinTransactionService.deleteTransactionFromCoin(request));
        verifyNoInteractions(coinTransactionRepository);
    }

    @Test
    void testGetTransactionHistoryOfUserForGivenCoin_shouldNotThrowIfUserAndPortfolioExists() {
        CoinTransactionDto request = generateRequestData();

        AppUser mockAppUser = mock(AppUser.class);

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<Portfolio> mockPortfolioList = new ArrayList<>();
        Portfolio mockPortfolio = mock(Portfolio.class);
        mockPortfolioList.add(mockPortfolio);

        when(mockAppUser.getPortfolio()).thenReturn(mockPortfolioList);
        when(mockPortfolio.getCoinName()).thenReturn("bitcoin");

        assertDoesNotThrow(() -> coinTransactionService.getTransactionHistoryOfUserForGivenCoin(request.getEmail(), request.getCoinName()));
    }

    @Test
    void testGetTransactionHistoryOfUserForGivenCoin_shouldThrowIfUserDoesNotExist() {
        CoinTransactionDto request = generateRequestData();

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(null);

        assertThrows(UserDoesNotExistsException.class, () -> coinTransactionService
                .getTransactionHistoryOfUserForGivenCoin(request.getEmail(), request.getCoinName()));
        verifyNoInteractions(coinTransactionRepository);
    }

    @Test
    void testGetTransactionHistoryOfUserForGivenCoin_shouldThrowIfPortfolioDoesNotExist() {
        CoinTransactionDto request = generateRequestData();

        AppUser mockAppUser = mock(AppUser.class);

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<Portfolio> emptyList = new ArrayList<>();
        when(mockAppUser.getPortfolio()).thenReturn(emptyList);

        assertThrows(CoinDoesNotExistsInPortfolioException.class, () -> coinTransactionService
                .getTransactionHistoryOfUserForGivenCoin(request.getEmail(), request.getCoinName()));
        verifyNoInteractions(coinTransactionRepository);
    }

    private CoinTransactionDto generateRequestData() {
        CoinTransactionDto req = new CoinTransactionDto();
        req.setId(1L);
        req.setEmail("test@test.com");
        req.setCoinName("bitcoin");
        req.setisPositive(true);
        req.setQuantity(1.0);
        long timeStamp = 1644436134;
        req.setUnixTransactionDate(timeStamp);
        return req;
    }
}