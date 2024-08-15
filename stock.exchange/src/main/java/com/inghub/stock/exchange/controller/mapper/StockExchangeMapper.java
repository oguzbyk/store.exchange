package com.inghub.stock.exchange.controller.mapper;

import com.inghub.stock.exchange.controller.dto.StockExchangeCreateDto;
import com.inghub.stock.exchange.controller.resource.StockExchangeResource;
import com.inghub.stock.exchange.entity.StockExchange;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StockExchangeMapper {
    StockExchangeMapper INSTANCE = Mappers.getMapper(StockExchangeMapper.class);

    @Mapping(target = "id", ignore = true)
    StockExchange toEntity(StockExchangeCreateDto stockCreateDto);
    StockExchangeResource stockExchangeResourceFromStockExchange(StockExchange stockExchange);
}
