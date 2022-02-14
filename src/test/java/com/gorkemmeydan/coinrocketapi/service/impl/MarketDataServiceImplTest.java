package com.gorkemmeydan.coinrocketapi.service.impl;

import com.gorkemmeydan.coinrocketapi.dto.MarketDataDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MarketDataServiceImplTest {
    private RestTemplate restTemplate;

    private MarketDataServiceImpl marketDataService;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        marketDataService = new MarketDataServiceImpl();
        ReflectionTestUtils.setField(marketDataService, "restTemplate", restTemplate);
    }

    @Test
    void testGetTopHundredCoins_shouldReturnApiResponse() {
       Object[] mockObj = new Object[2];
       mockObj[0] = new Object();
       mockObj[1] = new Object();

       when(restTemplate.getForEntity(anyString(), eq(Object[].class))).thenReturn(new ResponseEntity<>(mockObj, HttpStatus.OK));

       Object[] result = marketDataService.getTopHundredCoins();
       assertEquals(result, mockObj);
    }

    @Test
    void testGetCoinsBySearchVal_shouldReturnApiResponse() {
        Object[] mockObj = new Object[2];
        mockObj[0] = new Object();
        mockObj[1] = new Object();

        when(restTemplate.getForEntity(anyString(), eq(Object[].class))).thenReturn(new ResponseEntity<>(mockObj, HttpStatus.OK));

        MarketDataDto mockDto = new MarketDataDto();
        mockDto.setSearchVal("bitcoin");

        Object[] result = marketDataService.getCoinsBySearchVal(mockDto);
        assertEquals(result, mockObj);
    }

    @Test
    void testGetTrendingCoins_shouldReturnApiResponse() {
        Object mockObj = new Object();

        when(restTemplate.getForEntity(anyString(), eq(Object.class))).thenReturn(new ResponseEntity<>(mockObj, HttpStatus.OK));

        Object result = marketDataService.getTrendingCoins();
        assertEquals(result, mockObj);
    }
}