package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.dto.CoinTransactionDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.CoinTransaction;
import com.gorkemmeydan.coinrocketapi.service.CoinTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CoinTransactionController {
    private final CoinTransactionService coinTransactionService;

    @GetMapping("/cointransaction/get")
    public ResponseEntity<?> getTransactionForGivenCoin(@RequestBody CoinTransactionDto coinTransactionDto) {
        try {
            List<CoinTransaction> coinTransactions = coinTransactionService.getTransactionHistoryOfUserForGivenCoin(coinTransactionDto);
            return ResponseEntity.ok(coinTransactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/cointransaction/add")
    public ResponseEntity<?> addTransactionToPortfolioCoin(@RequestBody CoinTransactionDto coinTransactionDto) {
        try {
            AppUser appUser = coinTransactionService.saveTransactionToCoin(coinTransactionDto);
            return ResponseEntity.ok(appUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/cointransaction/remove")
    public ResponseEntity<?> removeTransactionFromPortfolioCoin(@RequestBody CoinTransactionDto coinTransactionDto) {
        try {
            AppUser appUser = coinTransactionService.deleteTransactionFromCoin(coinTransactionDto);
            return ResponseEntity.ok(appUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
