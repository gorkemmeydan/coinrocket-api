package com.gorkemmeydan.coinrocketapi.service;

import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;
import com.gorkemmeydan.coinrocketapi.dto.UserHoldingsDto;

public interface AppUserService {
    void saveUser(AppUserDto appUserDto);

    boolean checkIfUserExists(String email);

    UserHoldingsDto getUserHoldings(String email);
}
