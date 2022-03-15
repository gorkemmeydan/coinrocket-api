package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.IntegrationTestSupport;
import com.gorkemmeydan.coinrocketapi.dto.AppUserDto;
import com.gorkemmeydan.coinrocketapi.dto.WatchlistDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
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
class WatchlistControllerTest extends IntegrationTestSupport {
    @Test
    public void testGetWatchlistOfUser_shouldNotGetIfUnauthorized() throws Exception {
        AppUserDto request = generateNewUserRequest();
        WatchlistDto watchlistDto = generateWatchlistRequest(request.getEmail());

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(WATCHLIST_API_ENDPOINT +"get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", request.getEmail()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testGetWatchlistOfUser_whenAuth_shouldGetHoldings() throws Exception {
        AppUserDto request = generateNewUserRequest();
        WatchlistDto watchlistDto = generateWatchlistRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        WatchList w = new WatchList();
        w.setCoinName(watchlistDto.getCoinName());
        w.setAppUser(appUser);
        watchlistRepository.save(w);

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(WATCHLIST_API_ENDPOINT +"get")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", request.getEmail()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].coinName", CoreMatchers.is(w.getCoinName())));
    }

    @Test
    public void testAddWatchlistOfUser_shouldNotGetIfUnauthorized() throws Exception {
        AppUserDto request = generateNewUserRequest();
        WatchlistDto watchlistDto = generateWatchlistRequest(request.getEmail());

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(WATCHLIST_API_ENDPOINT +"add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(watchlistDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testAddWatchlistOfUser_whenAuth_shouldGetHoldings() throws Exception {
        AppUserDto request = generateNewUserRequest();
        WatchlistDto watchlistDto = generateWatchlistRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(WATCHLIST_API_ENDPOINT +"add")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(watchlistDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testAddWatchlistOfUser_whenAuth_shouldNotAddIfAlreadyExist() throws Exception {
        AppUserDto request = generateNewUserRequest();
        WatchlistDto watchlistDto = generateWatchlistRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        WatchList w = new WatchList();
        w.setCoinName(watchlistDto.getCoinName());
        w.setAppUser(appUser);
        watchlistRepository.save(w);

        this.mockMvc.perform(
                MockMvcRequestBuilders.post(WATCHLIST_API_ENDPOINT +"add")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(watchlistDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Coin already exists in watchlist for given user")));
    }

    @Test
    public void testDeleteWatchlistOfUser_shouldNotGetIfUnauthorized() throws Exception {
        AppUserDto request = generateNewUserRequest();
        WatchlistDto watchlistDto = generateWatchlistRequest(request.getEmail());

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete(WATCHLIST_API_ENDPOINT +"remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(watchlistDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testDeleteWatchlistOfUser_whenAuth_shouldDeleteIfCoinExists() throws Exception {
        AppUserDto request = generateNewUserRequest();
        WatchlistDto watchlistDto = generateWatchlistRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        WatchList w = new WatchList();
        w.setCoinName(watchlistDto.getCoinName());
        w.setAppUser(appUser);
        watchlistRepository.save(w);

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete(WATCHLIST_API_ENDPOINT +"remove")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(watchlistDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testDeleteWatchlistOfUser_whenAuth_shouldNotDeleteIfCoinDoesNotExists() throws Exception {
        AppUserDto request = generateNewUserRequest();
        WatchlistDto watchlistDto = generateWatchlistRequest(request.getEmail());

        // create a user
        AppUser appUser = new AppUser();
        appUser.setEmail(request.getEmail());
        appUser.setFullName(request.getFullName());
        appUser.setPassword(request.getPassword());
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        this.mockMvc.perform(
                MockMvcRequestBuilders.delete(WATCHLIST_API_ENDPOINT +"remove")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(watchlistDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Coin does not exist in watchlist for given user")));
    }

    private WatchlistDto generateWatchlistRequest(String email) {
        WatchlistDto watchlistDto = new WatchlistDto();
        watchlistDto.setCoinName("bitcoin");
        watchlistDto.setEmail(email);
        return watchlistDto;
    }
}