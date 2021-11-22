package com.gorkemmeydan.coinrocketapi.repository;

import com.gorkemmeydan.coinrocketapi.entity.CoinTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinTransactionRepository extends CustomRepository<CoinTransaction, Long> {
}
