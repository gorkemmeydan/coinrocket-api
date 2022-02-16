package com.gorkemmeydan.coinrocketapi.oauth2;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@TestConfiguration
@Configuration
public class AuthenticationManagerProvider {

    @Bean
    @Primary
    public TokenStore tokenStore2() {
        return new InMemoryTokenStore();
    }
}
