package com.gorkemmeydan.coinrocketapi.service.impl;

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

class NewsServiceImplTest {
    private RestTemplate restTemplate;

    private NewsServiceImpl newsService;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        newsService = new NewsServiceImpl();
        ReflectionTestUtils.setField(newsService, "restTemplate", restTemplate);
    }

    @Test
    void testGetNews_shouldReturnApiResponse() {
        Object mockObj = new Object();

        when(restTemplate.getForEntity(anyString(), eq(Object.class))).thenReturn(new ResponseEntity<>(mockObj, HttpStatus.OK));

        Object result = newsService.getNews();
        assertEquals(result, mockObj);
    }

}