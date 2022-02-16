package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.IntegrationTestSupport;
import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.CoinTransaction;
import com.gorkemmeydan.coinrocketapi.entity.Portfolio;
import com.gorkemmeydan.coinrocketapi.entity.WatchList;
import com.gorkemmeydan.coinrocketapi.oauth2.WithMockOAuth2Scope;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;


@ActiveProfiles("test")
@AutoConfigureMockMvc()
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "server-port=0",
        "command.line.runner.enabled=false",
        "spring.main.allow-bean-definition-overriding = true"
})
@ExtendWith(SpringExtension.class)
@DirtiesContext
class UserControllerTest extends IntegrationTestSupport {

    @Test
    public void testSaveUser_shouldCreateUserIfUserDoesNotExist () throws Exception {
        AppUserDto request = generateNewUserRequest();

        this.mockMvc.perform(
            MockMvcRequestBuilders.post(USER_API_ENDPOINT+"signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testSaveUser_shouldNotSaveIfUserAlreadyExist () throws Exception {
        AppUserDto request = generateNewUserRequest();

        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(USER_API_ENDPOINT+"signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("User already exists for this email")));
    }

    @Test
    public void testGetUserHoldings_shouldNotGetIfUnauthorized() throws Exception {
        AppUserDto request = generateNewUserRequest();

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(USER_API_ENDPOINT+"holdings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    @WithMockOAuth2Scope(token = "123456789",
                        username = "resource-tester",
                        scopes = "resource.read",
                        authorities = "CAN_READ")
    public void testGetUserHoldings_whenAuth_shouldGetHoldings() throws Exception {
        AppUserDto request = generateNewUserRequest();

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);


        WatchList w = generateWatchlist(appUser);
        watchlistRepository.save(w);

        Portfolio p = generatePortfolio(appUser);
        portfolioRepository.save(p);

        CoinTransaction c = generateCoinTransaction(p);
        coinTransactionRepository.save(c);

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(USER_API_ENDPOINT+"holdings")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(appUser.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.watchList", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.watchList[0].id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.watchList[0].coinName", CoreMatchers.is(w.getCoinName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolio",CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolio[0].id",CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolio[0].coinName",CoreMatchers.is(p.getCoinName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolio[0].lastWeeksHoldings", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolio[0].coinTransactions",CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolio[0].coinTransactions[0].id",CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolio[0].coinTransactions[0].coinName",CoreMatchers.is(c.getCoinName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolio[0].coinTransactions[0].quantity",CoreMatchers.is(c.getQuantity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolio[0].coinTransactions[0].transactionDate",CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolio[0].coinTransactions[0].positive",CoreMatchers.is(c.isPositive())));
    }

    private WatchList generateWatchlist(AppUser a) {
        WatchList w = new WatchList();
        w.setCoinName("ethereum");
        w.setAppUser(a);
        return w;
    }

    private Portfolio generatePortfolio(AppUser a) {
        Portfolio p = new Portfolio();
        p.setAppUser(a);
        p.setCoinName("bitcoin");

        return p;
    }

    private CoinTransaction generateCoinTransaction(Portfolio p) {
        CoinTransaction c = new CoinTransaction();
        c.setPortfolio(p);
        c.setPositive(true);
        c.setQuantity(1.0);
        c.setCoinName("bitcoin");
        c.setTransactionDate(new Date());
        return c;
    }
}