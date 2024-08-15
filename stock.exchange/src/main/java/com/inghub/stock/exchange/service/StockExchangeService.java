package com.inghub.stock.exchange.service;

import com.inghub.stock.exchange.annotation.CacheStockExchanges;
import com.inghub.stock.exchange.controller.dto.StockExchangeCreateDto;
import com.inghub.stock.exchange.controller.mapper.StockExchangeMapper;
import com.inghub.stock.exchange.controller.resource.StockExchangeResource;
import com.inghub.stock.exchange.entity.Stock;
import com.inghub.stock.exchange.entity.StockExchange;
import com.inghub.stock.exchange.exception.StockExchangeServiceException;
import com.inghub.stock.exchange.repository.StockExchangeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class StockExchangeService {

    private final StockExchangeRepository stockExchangeRepository;
    private final StockService stockService;
    private final StockExchangeMapper stockExchangeMapper;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    public StockExchangeService(StockExchangeRepository stockExchangeRepository, StockService stockService,
                                StockExchangeMapper stockExchangeMapper) {
        this.stockExchangeRepository = stockExchangeRepository;
        this.stockService = stockService;
        this.stockExchangeMapper = stockExchangeMapper;
    }

    @Transactional
    @CachePut(value = "stockExchanges", key = "#result.name")
    public StockExchangeResource createStockExchange(StockExchangeCreateDto stockExchangeResource) {
        return stockExchangeMapper
                .stockExchangeResourceFromStockExchange(stockExchangeRepository.save(stockExchangeMapper
                        .toEntity(stockExchangeResource)));
    }

    @Transactional
    @CacheEvict(value = "stockExchanges", key = "#exchangeName")
    public void deleteStockExchange(String exchangeName) {
        StockExchange stockExchange = findEntityByName(exchangeName);
        stockExchange.getStocks().clear();
        stockExchangeRepository.delete(stockExchange);
    }

    @Cacheable(value = "stockExchanges", key = "#exchangeName")
    public StockExchangeResource findByName(String exchangeName) {
        return stockExchangeMapper.stockExchangeResourceFromStockExchange(stockExchangeRepository.findByName(exchangeName)
                .orElseThrow(() -> new ResourceNotFoundException("StockExchange not found")));
    }

    @CacheStockExchanges(cacheName = "stockExchanges", key = "#exchangeName")
    @Transactional(readOnly = true)
    public List<StockExchange> getAllStockExchanges(boolean retrieveStock) {
        if (retrieveStock) {
            return stockExchangeRepository.findAllWithStocks();
        } else {
            return stockExchangeRepository.findAllWithoutStocks();
        }
    }
    @CircuitBreaker(name = "StockExchangesRate", fallbackMethod = "fallbackMethod")
    @RateLimiter(name = "StockExchangesRate", fallbackMethod = "fallbackMethod")
    public List<StockExchange> getAllStockExchangesResilience4j(boolean retrieveStock) {
        if (retrieveStock) {
            return stockExchangeRepository.findAllWithStocks();
        } else {
            return stockExchangeRepository.findAllWithoutStocks();
        }
    }

    @Transactional
    @CachePut(value = "stockExchanges", key = "#stockExchangeName")
    public StockExchangeResource putStockToStockExchange(String stockExchangeName, List<String> stockNames) {
        List<Stock> byNames = stockService.findByNames(stockNames);
        if (byNames.isEmpty()) {
            throw new ResourceNotFoundException("Stock not found");
        }

        Set<String> foundNames = byNames.stream()
                .map(Stock::getName)
                .collect(Collectors.toSet());

        List<String> missingNames = stockNames.stream()
                .filter(name -> !foundNames.contains(name))
                .collect(Collectors.toList());

        if (!missingNames.isEmpty()) {
            throw new ResourceNotFoundException("Stock not found for IDs: " + missingNames);
        }

        StockExchange stockExchange = findEntityByName(stockExchangeName);
        Set<Stock> stockSet = new HashSet<>(byNames);

        stockExchange.setStocks(stockSet);
        stockExchange.setLiveInMarket(byNames.size() >= 5);

        return stockExchangeMapper.stockExchangeResourceFromStockExchange(stockExchangeRepository.save(stockExchange));
    }

    @Transactional
    @CachePut(value = "stockExchanges", key = "#stockExchangeName")
    public StockExchangeResource putStockToStockExchangeMultiThread(String stockExchangeName, List<String> stockNames) {
        CompletableFuture<List<Stock>> stocksFuture = CompletableFuture.supplyAsync(() ->
                stockService.findByNames(stockNames), executorService);

        CompletableFuture<Set<String>> foundNamesFuture = stocksFuture.thenApplyAsync(stocks -> stocks.stream()
                .map(Stock::getName)
                .collect(Collectors.toSet()), executorService);

        CompletableFuture<List<String>> missingNamesFuture = stocksFuture.thenCombineAsync(foundNamesFuture,
                (stocks, foundNames) -> stockNames.stream()
                        .filter(name -> !foundNames.contains(name))
                        .collect(Collectors.toList()), executorService);

        List<Stock> byNames;
        Set<String> foundNames;
        List<String> missingNames;
        try {
            byNames = stocksFuture.get();
            foundNames = foundNamesFuture.get();
            missingNames = missingNamesFuture.get();
        } catch (Exception e) {
            throw new StockExchangeServiceException("Error while processing stocks", e);
        }

        if (!missingNames.isEmpty()) {
            throw new ResourceNotFoundException("Stock not found for IDs: " + missingNames);
        }

        StockExchange stockExchange = findEntityByName(stockExchangeName);
        Set<Stock> stockSet = new HashSet<>(byNames);

        stockExchange.setStocks(stockSet);
        stockExchange.setLiveInMarket(byNames.size() >= 5);

        return stockExchangeMapper.stockExchangeResourceFromStockExchange(stockExchangeRepository.save(stockExchange));
    }

    @Transactional
    @CachePut(value = "stockExchanges", key = "#stockExchangeName")
    public StockExchangeResource updateStockToStockExchange(String stockExchangeName, List<String> stockNames) {
        List<Stock> byNames = stockService.findByNames(stockNames);
        if (byNames.isEmpty()) {
            throw new ResourceNotFoundException("Stock not found");
        }

        Set<String> foundNames = byNames.stream()
                .map(Stock::getName)
                .collect(Collectors.toSet());

        List<String> missingNames = stockNames.stream()
                .filter(name -> !foundNames.contains(name))
                .toList();

        if (!missingNames.isEmpty()) {
            throw new ResourceNotFoundException("Stock not found for Names: " + missingNames);
        }

        StockExchange stockExchange = findEntityByName(stockExchangeName);

        Set<Stock> existingStocks = stockExchange.getStocks();
        existingStocks.addAll(byNames);

        stockExchange.setStocks(existingStocks);
        stockExchange.setLiveInMarket(existingStocks.size() >= 5);
        return stockExchangeMapper.stockExchangeResourceFromStockExchange(stockExchangeRepository.save(stockExchange));
    }

    @Transactional
    @CacheEvict(value = "stockExchanges", key = "#stockExchangeName")
    public void removeAllStocksFromStockExchange(String stockExchangeName) {
        StockExchange stockExchange = findEntityByName(stockExchangeName);
        stockExchange.setStocks(new HashSet<>());
        stockExchange.setLiveInMarket(false);
        stockExchangeRepository.save(stockExchange);
    }

    private StockExchange findEntityByName(String name) {
        return stockExchangeRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("StockExchange not found"));
    }

    public List<StockExchange> fallbackMethod(Throwable t) {
        throw new StockExchangeServiceException("Failed to retrieve stock exchanges due to resilience error", t);
    }
}

