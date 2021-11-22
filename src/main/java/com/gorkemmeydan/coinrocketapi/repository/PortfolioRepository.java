package com.gorkemmeydan.coinrocketapi.repository;

import com.gorkemmeydan.coinrocketapi.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends CustomRepository<Portfolio, Long> {
}
