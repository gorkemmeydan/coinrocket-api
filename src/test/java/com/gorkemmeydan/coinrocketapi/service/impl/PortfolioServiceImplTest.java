package com.gorkemmeydan.coinrocketapi.service.impl;

import com.gorkemmeydan.coinrocketapi.dto.PortfolioDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.Portfolio;
import com.gorkemmeydan.coinrocketapi.exception.CoinAlreadyExistsInPortfolioException;
import com.gorkemmeydan.coinrocketapi.exception.CoinDoesNotExistsInPortfolioException;
import com.gorkemmeydan.coinrocketapi.exception.UserDoesNotExistsException;
import com.gorkemmeydan.coinrocketapi.repository.AppUserRepository;
import com.gorkemmeydan.coinrocketapi.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class PortfolioServiceImplTest {
    private AppUserRepository appUserRepository;
    private PortfolioRepository portfolioRepository;
    private PortfolioServiceImpl portfolioService;

    @BeforeEach
    public void setUp() {
        appUserRepository = mock(AppUserRepository.class);
        portfolioRepository = mock(PortfolioRepository.class);
        portfolioService = new PortfolioServiceImpl(appUserRepository, portfolioRepository);
    }

    @Test
    void testSaveToPortfolio_shouldSaveToPortfolioIfAllCorrect() {
        PortfolioDto request = generateRequest();

        AppUser mockAppUser = mock(AppUser.class);
        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<Portfolio> mockPortfolioList = new ArrayList<>();
        Portfolio mockPortfolio = mock(Portfolio.class);
        mockPortfolioList.add(mockPortfolio);

        when(mockAppUser.getPortfolio()).thenReturn(mockPortfolioList);
        when(mockPortfolio.getCoinName()).thenReturn("ethereum");

        assertDoesNotThrow(() -> portfolioService.saveToPortfolio(request));
        verify(portfolioRepository, times(1)).save(any());
    }

    @Test
    void testSaveToPortfolio_shouldThrowIfCoinAlreadyExists() {
        PortfolioDto request = generateRequest();

        AppUser mockAppUser = mock(AppUser.class);
        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<Portfolio> mockPortfolioList = new ArrayList<>();
        Portfolio mockPortfolio = mock(Portfolio.class);
        mockPortfolioList.add(mockPortfolio);

        when(mockAppUser.getPortfolio()).thenReturn(mockPortfolioList);
        when(mockPortfolio.getCoinName()).thenReturn("bitcoin");

        assertThrows(CoinAlreadyExistsInPortfolioException.class, () -> portfolioService.saveToPortfolio(request));
        verifyNoInteractions(portfolioRepository);
    }

    @Test
    void testSaveToPortfolio_shouldThrowIfUserDoesNotExist() {
        PortfolioDto request = generateRequest();

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(null);

        assertThrows(UserDoesNotExistsException.class, () -> portfolioService.saveToPortfolio(request));
        verifyNoInteractions(portfolioRepository);
    }

    @Test
    void testDeleteFromPortfolio_shouldDeleteFromPortfolioIfReqsAreMet() {
        PortfolioDto request = generateRequest();

        AppUser mockAppUser = mock(AppUser.class);
        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<Portfolio> mockPortfolioList = new ArrayList<>();
        Portfolio mockPortfolio = mock(Portfolio.class);
        mockPortfolioList.add(mockPortfolio);

        when(mockAppUser.getPortfolio()).thenReturn(mockPortfolioList);
        when(mockPortfolio.getCoinName()).thenReturn("bitcoin");

        assertDoesNotThrow(() -> portfolioService.deleteFromPortfolio(request));
        verify(portfolioRepository, times(1)).delete(any());
    }

    @Test
    void testDeleteFromPortfolio_shouldThrowIfCoinDoesNotExistInPortfolio() {
        PortfolioDto request = generateRequest();

        AppUser mockAppUser = mock(AppUser.class);
        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        List<Portfolio> mockPortfolioList = new ArrayList<>();
        Portfolio mockPortfolio = mock(Portfolio.class);
        mockPortfolioList.add(mockPortfolio);

        when(mockAppUser.getPortfolio()).thenReturn(mockPortfolioList);
        when(mockPortfolio.getCoinName()).thenReturn("ethereum");

        assertThrows(CoinDoesNotExistsInPortfolioException.class,() -> portfolioService.deleteFromPortfolio(request));
        verifyNoInteractions(portfolioRepository);
    }

    @Test
    void testDeleteFromPortfolio_shouldThrowIfUserDoesNotExist() {
        PortfolioDto request = generateRequest();

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(null);

        assertThrows(UserDoesNotExistsException.class,() -> portfolioService.deleteFromPortfolio(request));
        verifyNoInteractions(portfolioRepository);
    }

    @Test
    void testGetPortfolioOfUser_shouldThrowIfUserDoesNotExist() {
        String email = "test@test.com";

        when(appUserRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UserDoesNotExistsException.class,() -> portfolioService.getPortfolioOfUser(email));
        verifyNoInteractions(portfolioRepository);
    }

    @Test
    void testGetPortfolioOfUser_shouldReturnPortfolioIfReqsAreMet() {
        String email = "test@test.com";

        AppUser mockAppUser = mock(AppUser.class);
        when(appUserRepository.findByEmail(email)).thenReturn(mockAppUser);

        List<Portfolio> mockPortfolioList = new ArrayList<>();
        Portfolio mockPortfolio = mock(Portfolio.class);
        mockPortfolioList.add(mockPortfolio);

        when(mockAppUser.getPortfolio()).thenReturn(mockPortfolioList);

        assertDoesNotThrow(() -> portfolioService.getPortfolioOfUser(email));
        List<Portfolio> result = portfolioService.getPortfolioOfUser(email);
        assertEquals(mockPortfolioList, result);
    }

    private PortfolioDto generateRequest() {
        PortfolioDto portfolioDto = new PortfolioDto();
        portfolioDto.setEmail("test@test.com");
        portfolioDto.setCoinName("bitcoin");
        return portfolioDto;
    }
}