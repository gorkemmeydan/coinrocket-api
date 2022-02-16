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
    public void saveToPortfolio(PortfolioDto portfolioDto) {
        // get the app user from db
        AppUser appUser = appUserRepository.findByEmail(portfolioDto.getEmail());

        // find the user with given email
        if (appUser == null) throw new UserDoesNotExistsException("User with given email does not exist");

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
    }

    @Override
    public void deleteFromPortfolio(PortfolioDto portfolioDto) {
        // get the app user from db
        AppUser appUser = appUserRepository.findByEmail(portfolioDto.getEmail());

        // find the user with given email
        if (appUser == null) throw new UserDoesNotExistsException("User with given email does not exist");

        Optional<Portfolio> to_be_removed = appUser.getPortfolio().stream().filter(item -> item.getCoinName().equals(portfolioDto.getCoinName())).findFirst();

        // check if coin exists in watchlist
        if (!to_be_removed.isPresent()) throw new CoinDoesNotExistsInPortfolioException("Coin does not exist in portfolio for given user");

        log.info("removing coin {} from user {} ", portfolioDto.getCoinName(), portfolioDto.getEmail());

        to_be_removed.ifPresent(portfolioRepository::delete);
    }

    @Override
    public List<Portfolio> getPortfolioOfUser(String email) {
        // get the app user from db
        AppUser appUser = appUserRepository.findByEmail(email);

        if (appUser == null) throw new UserDoesNotExistsException("User with given email does not exist");

        log.info("Getting portfolio for user {}", email);

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
