package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.dto.PortfolioDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.Portfolio;
import com.gorkemmeydan.coinrocketapi.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;

    @GetMapping("/portfolio/get")
    public ResponseEntity<?> getPortfolioOfUser(@RequestBody PortfolioDto portfolioDto) {
        List<Portfolio> userPortfolio = portfolioService.getPortfolioOfUser(portfolioDto.getEmail());
        return ResponseEntity.ok(userPortfolio);
    }

    @PostMapping("/portfolio/add")
    public ResponseEntity<?> addToPortfolio(@RequestBody PortfolioDto portfolioDto) {
        portfolioService.saveToPortfolio(portfolioDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/portfolio/add").toUriString());
        return ResponseEntity.created(uri).body(true);
    }

    @DeleteMapping("/portfolio/remove")
    public ResponseEntity<?> removeFromPortfolio(@RequestBody PortfolioDto portfolioDto) {
        portfolioService.deleteFromPortfolio(portfolioDto);
        return ResponseEntity.ok(true);
    }
}