package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.IntegrationTestSupport;
import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;
import com.gorkemmeydan.coinrocketapi.dto.PortfolioDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
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
class PortfolioControllerTest  extends IntegrationTestSupport {
    @Test
    public void testGetPortfolioOfUser_shouldNotGetIfUnauthorized() throws Exception {
        AppUserDto request = generateNewUserRequest();
        PortfolioDto portfolioDto = generatePortfolioRequest(request.getEmail());

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(PORTFOLIO_API_ENDPOINT +"get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(portfolioDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testGetPortfolioOfUser_whenAuth_shouldGetHoldings() throws Exception {
        AppUserDto request = generateNewUserRequest();
        PortfolioDto portfolioDto = generatePortfolioRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        Portfolio p = new Portfolio();
        p.setCoinName(portfolioDto.getCoinName());
        p.setAppUser(appUser);
        portfolioRepository.save(p);

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(PORTFOLIO_API_ENDPOINT +"get")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(portfolioDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].coinName", CoreMatchers.is(p.getCoinName())));
    }

    @Test
    public void testAdPortfolioOfUser_shouldNotGetIfUnauthorized() throws Exception {
        AppUserDto request = generateNewUserRequest();
        PortfolioDto portfolioDto = generatePortfolioRequest(request.getEmail());

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(PORTFOLIO_API_ENDPOINT +"add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(portfolioDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testAddPortfolioOfUser_whenAuth_shouldGetHoldings() throws Exception {
        AppUserDto request = generateNewUserRequest();
        PortfolioDto portfolioDto = generatePortfolioRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(PORTFOLIO_API_ENDPOINT +"add")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(portfolioDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testAddPortfolioOfUser_whenAuth_shouldNotAddIfAlreadyExist() throws Exception {
        AppUserDto request = generateNewUserRequest();
        PortfolioDto portfolioDto = generatePortfolioRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        Portfolio p = new Portfolio();
        p.setCoinName(portfolioDto.getCoinName());
        p.setAppUser(appUser);
        portfolioRepository.save(p);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(PORTFOLIO_API_ENDPOINT +"add")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(portfolioDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Coin already exists in portfolio for given user")));
    }

    @Test
    public void testDeletePortfolioOfUser_shouldNotGetIfUnauthorized() throws Exception {
        AppUserDto request = generateNewUserRequest();
        PortfolioDto portfolioDto = generatePortfolioRequest(request.getEmail());

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete(PORTFOLIO_API_ENDPOINT +"remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(portfolioDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testDeletePortfolioOfUser_whenAuth_shouldDeleteIfCoinExists() throws Exception {
        AppUserDto request = generateNewUserRequest();
        PortfolioDto portfolioDto = generatePortfolioRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        Portfolio p = new Portfolio();
        p.setCoinName(portfolioDto.getCoinName());
        p.setAppUser(appUser);
        portfolioRepository.save(p);

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete(PORTFOLIO_API_ENDPOINT +"remove")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(portfolioDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testDeletePortfolioOfUser_whenAuth_shouldNotDeleteIfCoinDoesNotExists() throws Exception {
        AppUserDto request = generateNewUserRequest();
        PortfolioDto portfolioDto = generatePortfolioRequest(request.getEmail());

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
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(portfolioDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Coin does not exist in portfolio for given user")));
    }

    private PortfolioDto generatePortfolioRequest(String email) {
        PortfolioDto portfolioDto = new PortfolioDto();
        portfolioDto.setCoinName("bitcoin");
        portfolioDto.setEmail(email);
        return portfolioDto;
    }

}