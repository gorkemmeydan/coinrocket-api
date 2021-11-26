package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;
import com.gorkemmeydan.coinrocketapi.dto.UserHoldingsDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.exception.UserAlreadyExistsException;
import com.gorkemmeydan.coinrocketapi.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final AppUserService appUserService;

    @PostMapping("/signup")
    public ResponseEntity<?> saveUser(@RequestBody AppUserDto appUserDto) {
        try {
            AppUser newAppUser = appUserService.saveUser(appUserDto);
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/signup").toUriString());
            return ResponseEntity.created(uri).body(newAppUser);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/userholdings")
    public ResponseEntity<?> getUserHoldings(@RequestBody AppUserDto appUserDto) {
        try {
            UserHoldingsDto userHoldingsDto = appUserService.getUserHoldings(appUserDto);
            return ResponseEntity.ok(userHoldingsDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(appUserService.getAllUsers());
    }
}
