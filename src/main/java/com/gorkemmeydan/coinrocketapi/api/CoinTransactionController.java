package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.dto.CoinTransactionDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.CoinTransaction;
import com.gorkemmeydan.coinrocketapi.service.CoinTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CoinTransactionController {
    private final CoinTransactionService coinTransactionService;

    @GetMapping("/cointransaction/get")
    public ResponseEntity<?> getTransactionForGivenCoin(@RequestParam String email, @RequestParam String coinName) {
        List<CoinTransaction> coinTransactions = coinTransactionService.getTransactionHistoryOfUserForGivenCoin(email, coinName);
        return ResponseEntity.ok(coinTransactions);
    }

    @PostMapping("/cointransaction/add")
    public ResponseEntity<?> addTransactionToPortfolioCoin(@RequestBody CoinTransactionDto coinTransactionDto) {
        coinTransactionService.saveTransactionToCoin(coinTransactionDto);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/cointransaction/remove")
    public ResponseEntity<?> removeTransactionFromPortfolioCoin(@RequestBody CoinTransactionDto coinTransactionDto) {
        coinTransactionService.deleteTransactionFromCoin(coinTransactionDto);
        return ResponseEntity.ok(true);
    }
}
