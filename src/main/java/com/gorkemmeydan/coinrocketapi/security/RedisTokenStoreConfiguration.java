package com.gorkemmeydan.coinrocketapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@RequiredArgsConstructor
public class RedisTokenStoreConfiguration {
    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public TokenStore tokenStore() {
        // use redis to store token
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        //Set the prefix in the redis token storage
        redisTokenStore.setPrefix("auth-token:");
        return redisTokenStore;
    }
}
