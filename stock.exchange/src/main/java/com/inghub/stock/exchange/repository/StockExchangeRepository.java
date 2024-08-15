package com.inghub.stock.exchange.repository;

import com.inghub.stock.exchange.entity.StockExchange;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Hidden
@Repository
public interface StockExchangeRepository extends JpaRepository<StockExchange, Long> {

    @Query("SELECT se FROM StockExchange se LEFT JOIN FETCH se.stocks WHERE se.liveInMarket = true")
    List<StockExchange> findAllWithStocks();

    @Query("SELECT se FROM StockExchange se WHERE se.liveInMarket = true")
    List<StockExchange> findAllWithoutStocks();

    Optional<StockExchange> findByName(String name);
}







