package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.IntegrationTestSupport;
import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;
import com.gorkemmeydan.coinrocketapi.dto.CoinTransactionDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.CoinTransaction;
import com.gorkemmeydan.coinrocketapi.entity.Portfolio;
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
class CoinTransactionControllerTest extends IntegrationTestSupport  {
    @Test
    public void testGetCoinTransactionsOfUser_shouldNotGetIfUnauthorized() throws Exception {
        AppUserDto request = generateNewUserRequest();
        CoinTransactionDto coinTransactionDto = generateTransactionRequest(request.getEmail());

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(COIN_TRANSACTION_API_ENDPOINT +"get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(coinTransactionDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testGetCoinTransactionsOfUser_whenAuth_shouldGetHoldings() throws Exception {
        AppUserDto request = generateNewUserRequest();
        CoinTransactionDto coinTransactionDto = generateTransactionRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        Portfolio p = new Portfolio();
        p.setCoinName(coinTransactionDto.getCoinName());
        p.setAppUser(appUser);
        portfolioRepository.save(p);

        CoinTransaction c = new CoinTransaction();
        c.setCoinName(coinTransactionDto.getCoinName());
        c.setPositive(coinTransactionDto.getisPositive());
        c.setTransactionDate(new Date());
        c.setQuantity(coinTransactionDto.getQuantity());
        c.setPortfolio(p);
        coinTransactionRepository.save(c);

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(COIN_TRANSACTION_API_ENDPOINT +"get")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(coinTransactionDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].coinName", CoreMatchers.is(c.getCoinName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].transactionDate", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].quantity", CoreMatchers.is(c.getQuantity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].positive", CoreMatchers.is(c.isPositive())));
    }

    @Test
    public void testAddCoinTransactionsOfUser_shouldNotAddIfUnauthorized() throws Exception {
        AppUserDto request = generateNewUserRequest();
        CoinTransactionDto coinTransactionDto = generateTransactionRequest(request.getEmail());

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(COIN_TRANSACTION_API_ENDPOINT +"add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(coinTransactionDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testAddCoinTransactionsOfUser_whenAuth_shouldAdd() throws Exception {
        AppUserDto request = generateNewUserRequest();
        CoinTransactionDto coinTransactionDto = generateTransactionRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        Portfolio p = new Portfolio();
        p.setCoinName(coinTransactionDto.getCoinName());
        p.setAppUser(appUser);
        portfolioRepository.save(p);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(COIN_TRANSACTION_API_ENDPOINT +"add")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(coinTransactionDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testAddCoinTransactionsOfUser_whenAuth_shouldNotAddIfCoinDoesNotExistInPortfolio() throws Exception {
        AppUserDto request = generateNewUserRequest();
        CoinTransactionDto coinTransactionDto = generateTransactionRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(COIN_TRANSACTION_API_ENDPOINT +"add")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(coinTransactionDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Coin does not exist in portfolio for given user")));
    }


    @Test
    public void testDeleteCoinTransactionsOfUser_shouldNotDeleteIfUnauthorized() throws Exception {
        AppUserDto request = generateNewUserRequest();
        CoinTransactionDto coinTransactionDto = generateTransactionRequest(request.getEmail());

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete(COIN_TRANSACTION_API_ENDPOINT +"remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(coinTransactionDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testDeleteCoinTransactionsOfUser_whenAuth_shouldDeleteIfCoinExists() throws Exception {
        AppUserDto request = generateNewUserRequest();
        CoinTransactionDto coinTransactionDto = generateTransactionRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        Portfolio p = new Portfolio();
        p.setCoinName(coinTransactionDto.getCoinName());
        p.setAppUser(appUser);
        portfolioRepository.save(p);

        CoinTransaction c = new CoinTransaction();
        c.setCoinName(coinTransactionDto.getCoinName());
        c.setPositive(coinTransactionDto.getisPositive());
        c.setTransactionDate(new Date());
        c.setQuantity(coinTransactionDto.getQuantity());
        c.setPortfolio(p);
        CoinTransaction saveResult =  coinTransactionRepository.save(c);

        coinTransactionDto.setId(saveResult.getId());

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete(COIN_TRANSACTION_API_ENDPOINT +"remove")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(coinTransactionDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testDeleteCoinTransactionsOfUser_whenAuth_shouldNotDeleteIfIdIsMissing() throws Exception {
        AppUserDto request = generateNewUserRequest();
        CoinTransactionDto coinTransactionDto = generateTransactionRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        Portfolio p = new Portfolio();
        p.setCoinName(coinTransactionDto.getCoinName());
        p.setAppUser(appUser);
        portfolioRepository.save(p);

        CoinTransaction c = new CoinTransaction();
        c.setCoinName(coinTransactionDto.getCoinName());
        c.setPositive(coinTransactionDto.getisPositive());
        c.setTransactionDate(new Date());
        c.setQuantity(coinTransactionDto.getQuantity());
        c.setPortfolio(p);
        coinTransactionRepository.save(c);

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete(COIN_TRANSACTION_API_ENDPOINT +"remove")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(coinTransactionDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Transaction ID should be given for delete operation")));
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testDeleteCoinTransactionsOfUser_whenAuth_shouldNotDeleteIfCoinDoesNotExists() throws Exception {
        AppUserDto request = generateNewUserRequest();
        CoinTransactionDto coinTransactionDto = generateTransactionRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete(PORTFOLIO_API_ENDPOINT +"remove")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(coinTransactionDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Coin does not exist in portfolio for given user")));
    }

    private CoinTransactionDto generateTransactionRequest(String email) {
        CoinTransactionDto coinTransactionDto = new CoinTransactionDto();
        coinTransactionDto.setCoinName("bitcoin");
        coinTransactionDto.setEmail(email);
        coinTransactionDto.setUnixTransactionDate(15151515115L);
        coinTransactionDto.setisPositive(true);
        coinTransactionDto.setQuantity(1.0);
        return coinTransactionDto;
    }

}