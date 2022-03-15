package com.gorkemmeydan.coinrocketapi.service.impl;

import com.gorkemmeydan.coinrocketapi.dto.MarketDataDto;
import com.gorkemmeydan.coinrocketapi.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketDataServiceImpl implements MarketDataService {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public Object[] getTopHundredCoins() {
        log.info("Getting top 100 market data");
        return restTemplate.getForEntity(
        "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=true&price_change_percentage=1h%2C24h%2C7d",
            Object[].class).getBody();
    }

    @Override
    public Object[] getCoinsBySearchVal(String searchval) {
        log.info("Getting top coins for the search " + searchval);

        String url =
                "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=" +
                searchval +
                "&order=market_cap_desc&per_page=100&page=1&sparkline=true&price_change_percentage=1h%2C24h%2C7d";

        return restTemplate.getForEntity(url, Object[].class).getBody();
    }

    @Override
    public Object getTrendingCoins() {
        log.info("Getting top trending coins");
        return restTemplate.getForEntity(
        "https://api.coingecko.com/api/v3/search/trending",
            Object.class).getBody();
    }
}
