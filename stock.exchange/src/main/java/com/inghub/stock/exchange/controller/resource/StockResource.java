package com.inghub.stock.exchange.controller.resource;

import com.inghub.stock.exchange.entity.StockExchange;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record StockResource(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime lastUpdate,
        String modifiedBy,
        String name,
        String description,
        BigDecimal currentPrice,
        List<StockExchange> stockExchanges
) {
}
