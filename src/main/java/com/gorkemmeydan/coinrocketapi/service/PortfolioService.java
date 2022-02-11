package com.gorkemmeydan.coinrocketapi.service;

import com.gorkemmeydan.coinrocketapi.dto.PortfolioDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.Portfolio;

import java.util.List;

public interface PortfolioService {
    void saveToPortfolio(PortfolioDto portfolioDto);

    void deleteFromPortfolio(PortfolioDto portfolioDto);

    List<Portfolio> getPortfolioOfUser(String email);

    boolean checkIfUserExists(String email);

    boolean checkIfCoinExistsInPortfolio(AppUser appUser, String coinName);
}
