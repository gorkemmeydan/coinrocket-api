package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;
import com.gorkemmeydan.coinrocketapi.dto.WatchlistDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.exception.UserAlreadyExistsException;
import com.gorkemmeydan.coinrocketapi.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WatchlistController {
    private final WatchlistService watchlistService;

    @PostMapping("/watchlist/add")
    public ResponseEntity<?> addToWatchlist(@RequestBody WatchlistDto watchlistDto) {
        try {
            AppUser updatedAppUser = watchlistService.saveToWatchlist(watchlistDto);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/watchlist/add").toUriString());
            return ResponseEntity.created(uri).body(updatedAppUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/watchlist/remove")
    public ResponseEntity<?> removeFromWatchlist(@RequestBody WatchlistDto watchlistDto) {
        try {
            AppUser updatedAppUser = watchlistService.deleteFromWatchlist(watchlistDto);
            return ResponseEntity.ok(updatedAppUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
