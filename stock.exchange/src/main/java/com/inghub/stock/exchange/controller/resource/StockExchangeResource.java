package com.inghub.stock.exchange.controller.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inghub.stock.exchange.entity.Stock;

import java.time.LocalDateTime;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record StockExchangeResource (Long id,
        LocalDateTime createdAt,
        LocalDateTime lastUpdate,
        String modifiedBy,
        String name,

        Boolean liveInMarket,
        String description,
        List<Stock> stocks
        ) {
}
