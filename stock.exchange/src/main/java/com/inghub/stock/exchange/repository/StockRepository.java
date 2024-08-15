package com.inghub.stock.exchange.repository;

import com.inghub.stock.exchange.entity.Stock;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Hidden
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findAllByNameIn(List<String> stockNames);

    Optional<Stock> findByName(String stockName);
}
