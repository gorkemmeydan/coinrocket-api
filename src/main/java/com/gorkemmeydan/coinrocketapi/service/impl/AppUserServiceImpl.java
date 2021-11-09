package com.gorkemmeydan.coinrocketapi.service.impl;

import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.repository.AppUserRepository;
import com.gorkemmeydan.coinrocketapi.service.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser saveUser(AppUserDto appUserDto) {
        log.info("Saving new user {} to the database", appUserDto.getFullName());

        AppUser appUser = new AppUser();
        appUser.setEmail(appUserDto.getEmail());
        appUser.setFullName(appUserDto.getFullName());
        appUser.setPassword(passwordEncoder.encode(appUserDto.getPassword()));
        appUser.setCreatedAt(new Date());
        return appUserRepository.save(appUser);
    }

    @Override
    public List<AppUserDto> getAllUsers() {
        return null;
    }

    @Override
    public Page<AppUserDto> getAllUsers(Pageable pageable) {
        return null;
    }

}
