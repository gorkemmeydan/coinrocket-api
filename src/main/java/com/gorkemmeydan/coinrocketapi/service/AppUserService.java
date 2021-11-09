package com.gorkemmeydan.coinrocketapi.service;

import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppUserService {
    AppUser saveUser(AppUserDto appUserDto);

    List<AppUserDto> getAllUsers();

    Page<AppUserDto> getAllUsers(Pageable pageable);
}
