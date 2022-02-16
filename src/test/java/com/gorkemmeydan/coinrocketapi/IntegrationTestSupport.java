package com.gorkemmeydan.coinrocketapi;

import com.gorkemmeydan.coinrocketapi.oauth2.AuthenticationManagerProvider;
import com.gorkemmeydan.coinrocketapi.repository.AppUserRepository;
import com.gorkemmeydan.coinrocketapi.repository.CoinTransactionRepository;
import com.gorkemmeydan.coinrocketapi.repository.PortfolioRepository;
import com.gorkemmeydan.coinrocketapi.repository.WatchlistRepository;
import com.gorkemmeydan.coinrocketapi.service.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;


@Import(AuthenticationManagerProvider.class)
public abstract class IntegrationTestSupport extends TestSupport {

    @Autowired
    public MockMvc mockMvc;

    public final ObjectMapper mapper = new ObjectMapper();

    // repositories

    @Autowired
    public AppUserRepository appUserRepository;

    @Autowired
    public CoinTransactionRepository coinTransactionRepository;

    @Autowired
    public PortfolioRepository portfolioRepository;

    @Autowired
    public WatchlistRepository watchlistRepository;

    // services

    @Autowired
    public AppUserService appUserService;

    @Autowired
    public CoinTransactionService coinTransactionService;

    @Autowired
    public MarketDataService marketDataService;

    @Autowired
    public NewsService newsService;

    @Autowired
    public PortfolioService portfolioService;

    @Autowired
    WatchlistService watchlistService;

}
