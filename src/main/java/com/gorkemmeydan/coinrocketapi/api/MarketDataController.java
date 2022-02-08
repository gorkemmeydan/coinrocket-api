package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.dto.MarketDataDto;
import com.gorkemmeydan.coinrocketapi.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MarketDataController {
    private final MarketDataService marketDataService;

    @GetMapping("/marketdata/top100")
    public ResponseEntity<?> getMarketData() {
        try {
            Object[] topHundredData = marketDataService.getTopHundredCoins();
            return ResponseEntity.ok(topHundredData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/marketdata/searchval")
    public ResponseEntity<?> getMarketBySearchVal(@RequestBody MarketDataDto marketDataDto) {
        try {
            Object[] coinsBySearchVal = marketDataService.getCoinsBySearchVal(marketDataDto);
            return ResponseEntity.ok(coinsBySearchVal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/marketdata/trending")
    public ResponseEntity<?> getTrendingMarket() {
        try {
            Object trendingMarketItems = marketDataService.getTrendingCoins();
            return ResponseEntity.ok(trendingMarketItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
