package com.gorkemmeydan.coinrocketapi.service.impl;

import com.gorkemmeydan.coinrocketapi.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsServiceImpl implements NewsService {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public Object getNews() {
        log.info("Getting news");
        return restTemplate.getForEntity(
            "https://data.messari.io/api/v1/news",
            Object.class).getBody();
    }
}
