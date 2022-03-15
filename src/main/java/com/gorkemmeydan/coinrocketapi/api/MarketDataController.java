package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.service.MarketDataService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MarketDataController {
    private final MarketDataService marketDataService;

    // values to cache if there is fallback situation
    // since external api's cannot be available sometimes or rate limited
    Object[] cachedTopHundred;
    Object cachedTrending;

    @GetMapping("/marketdata/top100")
    @HystrixCommand(fallbackMethod = "getCachedTopHundred")
    public ResponseEntity<?> getMarketData() {
        Object[] topHundredData = marketDataService.getTopHundredCoins();
        cachedTopHundred = topHundredData;
        return ResponseEntity.ok(topHundredData);
    }

    @SuppressWarnings("unused")
    public ResponseEntity<?> getCachedTopHundred() {
        log.info("Fallback for top 100");
        return ResponseEntity.ok(cachedTopHundred);
    }

    @GetMapping("/marketdata/searchval")
    public ResponseEntity<?> getMarketBySearchVal(@RequestParam String searchval) {
        try {
            Object[] coinsBySearchVal = marketDataService.getCoinsBySearchVal(searchval);
            return ResponseEntity.ok(coinsBySearchVal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/marketdata/trending")
    @HystrixCommand(fallbackMethod = "getCachedTrending")
    public ResponseEntity<?> getTrendingMarket() {
        Object trendingMarketItems = marketDataService.getTrendingCoins();
        cachedTrending = trendingMarketItems;
        return ResponseEntity.ok(trendingMarketItems);
    }

    @SuppressWarnings("unused")
    public ResponseEntity<?> getCachedTrending() {
        log.info("Fallback for trending");
        return ResponseEntity.ok(cachedTrending);
    }
}
