package com.inghub.stock.exchange.service;

import com.inghub.stock.exchange.controller.dto.StockCreateDto;
import com.inghub.stock.exchange.controller.dto.StockUpdateDto;
import com.inghub.stock.exchange.controller.mapper.StockMapper;
import com.inghub.stock.exchange.controller.resource.StockResource;
import com.inghub.stock.exchange.entity.Stock;
import com.inghub.stock.exchange.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;

    @Autowired
    public StockService(StockRepository stockRepository,
                        StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }

    @Transactional
    public StockResource createStock(StockCreateDto stock) {

        return stockMapper.stockResourceFromStock(stockRepository.save(stockMapper.toEntity(stock)));
    }

    @Transactional
    public void deleteStock(String stockName) {
        Stock stock = findByName(stockName);
        stock.getStockExchanges().clear();
        stockRepository.delete(stock);
    }

    @Transactional
    public Stock updateStock(String name, StockUpdateDto stock) {
        Stock stockByName = stockRepository.findByName(name).orElseThrow(RuntimeException::new);
        stockMapper.updateStockFromDto(stock, stockByName);
        return stockRepository.save(stockByName);
    }

    public Stock findByName(String stockName) {
        return stockRepository.findByName(stockName)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found"));
    }

    public List<Stock> findByNames(List<String> stockNames) {
        return stockRepository.findAllByNameIn(stockNames);
    }

    public List<StockResource> getAllStocks() {
        List<Stock> stocks = stockRepository.findAll();
        return stockMapper.stockResourcesFromStocks(stocks);
    }
}
