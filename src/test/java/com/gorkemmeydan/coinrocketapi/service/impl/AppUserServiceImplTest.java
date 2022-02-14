package com.gorkemmeydan.coinrocketapi.service.impl;

import com.gorkemmeydan.coinrocketapi.TestUtils;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserServiceImplTest {
    private AppUserRepository  appUserRepository;
    private AppUserServiceImpl appUserService;

    @BeforeEach
    public void setUp() {
        appUserRepository = mock(AppUserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        appUserService = new AppUserServiceImpl(appUserRepository, passwordEncoder);

        when(passwordEncoder.encode(any())).thenReturn("");
    }

    @Test
    void testSaveUser_whenUserDoesNotExist_shouldCreate() {
        AppUserDto request = TestUtils.generateNewUserRequest();

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(null);

        assertDoesNotThrow(() -> appUserService.saveUser(request));
        verify(appUserRepository, times(1)).findByEmail(request.getEmail());

        AppUser mockAppUser = new AppUser();
        mockAppUser.setEmail(request.getEmail());

        verify(appUserRepository, times(1)).save(mockAppUser);
    }

    @Test
    void testSaveUser_whenUserExists_shouldNotCreate() {
        AppUserDto request = TestUtils.generateNewUserRequest();

        AppUser mockAppUser = new AppUser();
        mockAppUser.setEmail(request.getEmail());

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        assertThrows(UserAlreadyExistsException.class, () -> appUserService.saveUser(request));
        verify(appUserRepository, times(1)).findByEmail(request.getEmail());
    }

    @Test
    void testGetUserHoldings_shouldThrowIfUserDoesNotExist() {
        AppUserDto request = TestUtils.generateNewUserRequest();

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(null);

        assertThrows(UserDoesNotExistsException.class, () -> appUserService.getUserHoldings(request));
        verify(appUserRepository, times(1)).findByEmail(request.getEmail());
    }

    @Test
    void testGetUserHoldings_shouldNotThrowIfUserExists() {
        AppUserDto request = TestUtils.generateNewUserRequest();

        AppUser mockAppUser = new AppUser();
        mockAppUser.setEmail(request.getEmail());

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        assertDoesNotThrow(() -> appUserService.getUserHoldings(request));
        verify(appUserRepository, times(2)).findByEmail(request.getEmail());
    }

    @Test
    void testGetUserHoldings_shouldGenerateCorrectDtoFromRequest() {
        AppUserDto request = TestUtils.generateNewUserRequest();

        AppUser mockAppUser = generateMockAppUser(request.getEmail());

        when(appUserRepository.findByEmail(request.getEmail())).thenReturn(mockAppUser);

        UserHoldingsDto mockUserHoldingsDto = generateMockUserHoldingsDto(request.getEmail());
        UserHoldingsDto result = appUserService.getUserHoldings(request);

        assertEquals(mockUserHoldingsDto, result);
        verify(appUserRepository, times(2)).findByEmail(request.getEmail());
    }

    private AppUser generateMockAppUser(String email) {
        AppUser mockAppUser = new AppUser();

        mockAppUser.setEmail(email);
        mockAppUser.setId(1L);

        List<WatchList> mockWatchList = new ArrayList<>();

        WatchList mockWatch1 = new WatchList();
        mockWatch1.setId(1L);
        mockWatch1.setCoinName("bitcoin");
        mockWatch1.setAppUser(mockAppUser);
        WatchList mockWatch2 = new WatchList();
        mockWatch2.setId(2L);
        mockWatch2.setCoinName("tether");
        mockWatch2.setAppUser(mockAppUser);

        mockWatchList.add(mockWatch1);
        mockWatchList.add(mockWatch2);

        mockAppUser.setWatchList(mockWatchList);

        List<Portfolio> mockPortfolioList = new ArrayList<>();

        Portfolio mockPortfolio = new Portfolio();
        mockPortfolio.setCoinName("ethereum");
        mockPortfolio.setAppUser(mockAppUser);

        List<CoinTransaction> mockCoinTransactionList = new ArrayList<>();
        long timeStamp1 = 1644436134;
        Date mockDate1 = new Date(timeStamp1*1000);
        CoinTransaction mockCoinTransaction1 = new CoinTransaction(1L,"ethereum",true, 2.0, mockDate1, mockPortfolio);
        long timeStamp2 = 1644608995;
        Date mockDate2 = new Date(timeStamp2*1000);
        CoinTransaction mockCoinTransaction2 = new CoinTransaction(2L,"ethereum",false, 1.0, mockDate2, mockPortfolio);
        mockCoinTransactionList.add(mockCoinTransaction1);
        mockCoinTransactionList.add(mockCoinTransaction2);

        mockPortfolio.setCoinTransactions(mockCoinTransactionList);

        mockPortfolioList.add(mockPortfolio);

        mockAppUser.setPortfolio(mockPortfolioList);

        return mockAppUser;
    }

    private UserHoldingsDto generateMockUserHoldingsDto(String email) {
        UserHoldingsDto mockUserHoldingsDto = new UserHoldingsDto();
        mockUserHoldingsDto.setId(1L);
        mockUserHoldingsDto.setEmail(email);

        List<WatchListItemPojo> mockWatchList = new ArrayList<>();

        WatchListItemPojo mockWatchlistItemPojo1 = new WatchListItemPojo();
        mockWatchlistItemPojo1.setId(1L);
        mockWatchlistItemPojo1.setCoinName("bitcoin");
        WatchListItemPojo mockWatchlistItemPojo2 = new WatchListItemPojo();
        mockWatchlistItemPojo2.setId(2L);
        mockWatchlistItemPojo2.setCoinName("tether");

        mockWatchList.add(mockWatchlistItemPojo1);
        mockWatchList.add(mockWatchlistItemPojo2);

        mockUserHoldingsDto.setWatchList(mockWatchList);

        List<PortfolioItemPojo> mockPortfolio = new ArrayList<>();

        PortfolioItemPojo mockPortfolioItemPojo = new PortfolioItemPojo();
        mockPortfolioItemPojo.setId(1L);
        mockPortfolioItemPojo.setCoinName("ethereum");

        List<CoinTransactionItemPojo> mockCoinTransactionItemPojoList = new ArrayList<>();

        CoinTransactionItemPojo mockCoinTransactionItemPojo1 = new CoinTransactionItemPojo();
        mockCoinTransactionItemPojo1.setId(1L);
        mockCoinTransactionItemPojo1.setCoinName("ethereum");
        mockCoinTransactionItemPojo1.setPositive(true);
        mockCoinTransactionItemPojo1.setQuantity(2.0);
        long timeStamp1 = 1644436134;
        Date mockDate1 = new Date(timeStamp1*1000);
        mockCoinTransactionItemPojo1.setTransactionDate(mockDate1);

        CoinTransactionItemPojo mockCoinTransactionItemPojo2 = new CoinTransactionItemPojo();
        mockCoinTransactionItemPojo2.setId(2L);
        mockCoinTransactionItemPojo2.setCoinName("ethereum");
        mockCoinTransactionItemPojo2.setPositive(false);
        mockCoinTransactionItemPojo1.setQuantity(1.0);
        long timeStamp2 = 1644608995;
        Date mockDate2 = new Date(timeStamp2*1000);
        mockCoinTransactionItemPojo1.setTransactionDate(mockDate2);

        mockCoinTransactionItemPojoList.add(mockCoinTransactionItemPojo1);
        mockCoinTransactionItemPojoList.add(mockCoinTransactionItemPojo2);

        mockPortfolioItemPojo.setCoinTransactions(mockCoinTransactionItemPojoList);

        mockPortfolio.add(mockPortfolioItemPojo);

        mockUserHoldingsDto.setPortfolio(mockPortfolio);

        return mockUserHoldingsDto;
    }
}