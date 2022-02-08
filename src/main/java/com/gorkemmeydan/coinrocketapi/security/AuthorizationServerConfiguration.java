package com.gorkemmeydan.coinrocketapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.clientId:coinrocket}")
    private String clientId;

    @Value("${jwt.client-secret:coinrocket-secret}")
    private String clientSecret;

    @Value("${jwt.accessTokenValiditySeconds:43200}") // 12 hours
    private int accessTokenValiditySeconds;

    @Value("${jwt.authorizedGrantTypes:password,authorization_code,refresh_token}")
    private String[] authorizedGrantTypes;

    @Value("${jwt.refreshTokenValiditySeconds:2592000}") // 30 days
    private int refreshTokenValiditySeconds;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        try {
            //Allow client form submission
            oauthServer.allowFormAuthenticationForClients()
                //The client verifies the token access permission
                .checkTokenAccess("permitAll()")
                //Client token call permission
                .tokenKeyAccess("permitAll()");
        } catch (Error e) {
            throw new Exception(e);
        }
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(clientId)
                .secret(passwordEncoder.encode(clientSecret))
                .authorizedGrantTypes(authorizedGrantTypes)
                .scopes("openid");
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore)
                .tokenServices(tokenService())
                .authenticationManager(authenticationManager)
                .userDetailsService(customUserDetailsService);
    }

    @Bean
    public DefaultTokenServices tokenService() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        //Configure token storage
        tokenServices.setTokenStore(tokenStore);
        //Enable support for refresh_token. If there is no configuration before,
        // restarting the service after starting the service may cause the problem of not returning the token.
        // Solution: Clear the token storage corresponding to redis
        tokenServices.setSupportRefreshToken(true);
        //Reuse refresh_token
        tokenServices.setReuseRefreshToken(true);
        //Token validity period, set 12 hours
        tokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        //refresh_token validity period, set one month
        tokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
        return tokenServices;
    }
}
