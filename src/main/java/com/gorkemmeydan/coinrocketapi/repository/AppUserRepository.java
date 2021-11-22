package com.gorkemmeydan.coinrocketapi.repository;

import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends CustomRepository<AppUser, Long> {
    AppUser findByEmail(String email);
}
