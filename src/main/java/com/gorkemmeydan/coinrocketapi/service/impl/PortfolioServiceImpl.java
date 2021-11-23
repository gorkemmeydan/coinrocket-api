package com.gorkemmeydan.coinrocketapi.service.impl;

import com.gorkemmeydan.coinrocketapi.dto.PortfolioDto;
import com.gorkemmeydan.coinrocketapi.entity.AppUser;
import com.gorkemmeydan.coinrocketapi.entity.Portfolio;
import com.gorkemmeydan.coinrocketapi.exception.*;
import com.gorkemmeydan.coinrocketapi.repository.AppUserRepository;
import com.gorkemmeydan.coinrocketapi.repository.PortfolioRepository;
import com.gorkemmeydan.coinrocketapi.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PortfolioServiceImpl implements PortfolioService {
    private final AppUserRepository appUserRepository;
    private final PortfolioRepository portfolioRepository;

    @Override
    public AppUser saveToPortfolio(PortfolioDto portfolioDto) throws UserDoesNotExistsException, CoinAlreadyExistsInPortfolioException {
        // find the user with given email
        if (!checkIfUserExists(portfolioDto.getEmail())) throw new UserDoesNotExistsException("User with given email does not exist");

        // get the app user from db
        AppUser appUser = appUserRepository.findByEmail(portfolioDto.getEmail());

        // check if coin exists in watchlist
        if (checkIfCoinExistsInPortfolio(appUser, portfolioDto.getCoinName())) throw new CoinAlreadyExistsInPortfolioException("Coin already exists in portfolio for given user");

        log.info("adding coin {} to portfolio of user {} ", portfolioDto.getCoinName(), portfolioDto.getEmail());

        // create a new item for the coin portfolio
        Portfolio portfolio = new Portfolio();
        portfolio.setAppUser(appUser);
        portfolio.setCoinName(portfolioDto.getCoinName());
        portfolioRepository.save(portfolio);

        // refresh user to have latest watchlist
        appUserRepository.refresh(appUser);
        return appUser;
    }

    @Override
    public AppUser deleteFromPortfolio(PortfolioDto portfolioDto) throws UserDoesNotExistsException, CoinDoesNotExistsInPortfolioException {
        // find the user with given email
        if (!checkIfUserExists(portfolioDto.getEmail())) throw new UserDoesNotExistsException("User with given email does not exist");

        // get the app user from db
        AppUser appUser = appUserRepository.findByEmail(portfolioDto.getEmail());

        // check if coin exists in watchlist
        if (!checkIfCoinExistsInPortfolio(appUser, portfolioDto.getCoinName())) throw new CoinDoesNotExistsInPortfolioException("Coin does not exist in portfolio for given user");

        log.info("removing coin {} from user {} ", portfolioDto.getCoinName(), portfolioDto.getEmail());

        Optional<Portfolio> to_be_removed = appUser.getPortfolio().stream().filter(item -> item.getCoinName().equals(portfolioDto.getCoinName())).findFirst();

        to_be_removed.ifPresent(portfolioRepository::delete);

        return appUser;
    }

    @Override
    public List<Portfolio> getPortfolioOfUser(String email) throws UserDoesNotExistsException {
        // find the user with given email
        log.info("Getting portfolio for user {}", email);

        if (!checkIfUserExists(email)) throw new UserDoesNotExistsException("User with given email does not exist");

        // get the app user from db
        AppUser appUser = appUserRepository.findByEmail(email);
        return appUser.getPortfolio();
    }

    @Override
    public boolean checkIfUserExists(String email)  {
        return appUserRepository.findByEmail(email) != null;
    }

    @Override
    public boolean checkIfCoinExistsInPortfolio(AppUser appUser, String coinName) {
        return appUser.getPortfolio().stream().anyMatch(item -> item.getCoinName().equals(coinName));
    }
}
