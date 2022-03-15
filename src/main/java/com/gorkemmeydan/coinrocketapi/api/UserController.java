package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;
import com.gorkemmeydan.coinrocketapi.dto.UserHoldingsDto;
import com.gorkemmeydan.coinrocketapi.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final AppUserService appUserService;

    @PostMapping("/user/signup")
    public ResponseEntity<?> saveUser(@RequestBody AppUserDto appUserDto) {
        appUserService.saveUser(appUserDto);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/signup").toUriString());
        return ResponseEntity.created(uri).body(true);
    }

    @GetMapping("/user/holdings")
    public ResponseEntity<?> getUserHoldings(@RequestParam String email) {
        UserHoldingsDto userHoldingsDto = appUserService.getUserHoldings(email);
        return ResponseEntity.ok(userHoldingsDto);
    }

    @GetMapping("/user/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        String userName = authentication.getName();
        HashMap<String, String> map = new HashMap<>();
        map.put("email", userName);
        return ResponseEntity.ok(map);
    }
}
