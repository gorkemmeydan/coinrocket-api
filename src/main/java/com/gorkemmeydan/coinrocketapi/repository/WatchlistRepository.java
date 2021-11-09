package com.gorkemmeydan.coinrocketapi.repository;

import com.gorkemmeydan.coinrocketapi.entity.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchList, Long> {
}
