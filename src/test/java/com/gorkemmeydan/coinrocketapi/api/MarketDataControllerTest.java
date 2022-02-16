package com.gorkemmeydan.coinrocketapi.api;

import com.gorkemmeydan.coinrocketapi.IntegrationTestSupport;
import com.gorkemmeydan.coinrocketapi.dto.MarketDataDto;
import com.gorkemmeydan.coinrocketapi.oauth2.WithMockOAuth2Scope;
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
class MarketDataControllerTest extends IntegrationTestSupport {

    @Test
    public void testGetMarketData_shouldNotGetIfUnauthorized() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get(MARKET_DATA_API_ENDPOINT+"top100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testGetMarketData_whenAuth_shouldGetMarketData() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get(MARKET_DATA_API_ENDPOINT+"top100")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSearchVal_shouldNotGetIfUnauthorized() throws Exception {
        MarketDataDto marketDataDto = new MarketDataDto();
        marketDataDto.setSearchVal("bitcoin");

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(MARKET_DATA_API_ENDPOINT+"searchval")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(marketDataDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testSearchVal_whenAuth_shouldGetSearchVal() throws Exception {
        MarketDataDto marketDataDto = new MarketDataDto();
        marketDataDto.setSearchVal("bitcoin");

        this.mockMvc.perform(
                MockMvcRequestBuilders.get(MARKET_DATA_API_ENDPOINT+"searchval")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(marketDataDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testTrending_shouldNotGetIfUnauthorized() throws Exception {
        this.mockMvc.perform(
            MockMvcRequestBuilders.get(MARKET_DATA_API_ENDPOINT+"trending")
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    @WithMockOAuth2Scope(token = "123456789",
            username = "resource-tester",
            scopes = "resource.read",
            authorities = "CAN_READ")
    public void testTrending_whenAuth_shouldGetTrending() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.get(MARKET_DATA_API_ENDPOINT+"trending")
                        .header("Authorization", "Bearer 123456789")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}