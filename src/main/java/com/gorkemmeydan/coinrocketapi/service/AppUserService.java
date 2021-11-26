package com.gorkemmeydan.coinrocketapi.service;

import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;
import com.gorkemmeydan.coinrocketapi.dto.UserHoldingsDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.exception.UserAlreadyExistsException;
import com.gorkemmeydan.coinrocketapi.exception.UserDoesNotExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppUserService {
    AppUser saveUser(AppUserDto appUserDto) throws UserAlreadyExistsException;

    boolean checkIfUserExists(String email);

    List<AppUser> getAllUsers();

    Page<AppUserDto> getAllUsers(Pageable pageable);

    UserHoldingsDto getUserHoldings(AppUserDto appUserDto) throws UserDoesNotExistsException;
}
