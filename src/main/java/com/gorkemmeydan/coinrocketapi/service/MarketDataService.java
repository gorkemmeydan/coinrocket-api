package com.gorkemmeydan.coinrocketapi.service;

import com.gorkemmeydan.coinrocketapi.dto.MarketDataDto;

public interface MarketDataService {
    Object[] getTopHundredCoins();

    Object[] getCoinsBySearchVal(MarketDataDto marketDataDto);

    Object getTrendingCoins();
}
