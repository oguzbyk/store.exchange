package com.inghub.stock.exchange.controller.mapper;

import com.inghub.stock.exchange.controller.dto.StockCreateDto;
import com.inghub.stock.exchange.controller.dto.StockUpdateDto;
import com.inghub.stock.exchange.controller.resource.StockResource;
import com.inghub.stock.exchange.entity.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateStockFromDto(StockUpdateDto stockUpdateDto, @MappingTarget Stock stock);

    @Mapping(target = "id", ignore = true)
    Stock toEntity(StockCreateDto stockCreateDto);

    StockCreateDto toDto(Stock stock);

    StockResource stockResourceFromStock(Stock stock);

    default List<StockResource> stockResourcesFromStocks(List<Stock> stocks) {
        return stocks.stream()
                .map(this::stockResourceFromStock)
                .collect(Collectors.toList());
    }
}
