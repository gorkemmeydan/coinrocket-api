package com.gorkemmeydan.coinrocketapi.service;

import com.gorkemmeydan.coinrocketapi.dto.PortfolioDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.Portfolio;
import com.gorkemmeydan.coinrocketapi.exception.*;

import java.util.List;

public interface PortfolioService {
    AppUser saveToPortfolio(PortfolioDto portfolioDto) throws UserDoesNotExistsException, CoinAlreadyExistsInPortfolioException;

    AppUser deleteFromPortfolio(PortfolioDto portfolioDto) throws UserDoesNotExistsException, CoinDoesNotExistsInPortfolioException;

    List<Portfolio> getPortfolioOfUser(String email) throws UserDoesNotExistsException;

    boolean checkIfUserExists(String email);

    boolean checkIfCoinExistsInPortfolio(AppUser appUser, String coinName);
}
