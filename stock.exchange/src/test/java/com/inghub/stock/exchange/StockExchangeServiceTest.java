package com.inghub.stock.exchange;

import com.inghub.stock.exchange.controller.dto.StockExchangeCreateDto;
import com.inghub.stock.exchange.controller.mapper.StockExchangeMapper;
import com.inghub.stock.exchange.controller.resource.StockExchangeResource;
import com.inghub.stock.exchange.entity.Stock;
import com.inghub.stock.exchange.entity.StockExchange;
import com.inghub.stock.exchange.repository.StockExchangeRepository;
import com.inghub.stock.exchange.service.StockExchangeService;
import com.inghub.stock.exchange.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StockExchangeServiceTest {

    @Mock
    private StockExchangeRepository stockExchangeRepository;

    @Mock
    private StockExchangeMapper stockExchangeMapper;

    @Mock
    private StockService stockService;

    @InjectMocks
    private StockExchangeService stockExchangeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create_stock_exchange_success() {
        // Given
        StockExchangeCreateDto dto = new StockExchangeCreateDto("NYSE", "New York Stock Exchange");
        StockExchange stockExchange = new StockExchange();
        stockExchange.setName("NYSE");
        stockExchange.setDescription("New York Stock Exchange");
        StockExchangeResource expectedResource = new StockExchangeResource(1L, null, null, null, "NYSE", false, "New York Stock Exchange", null);

        when(stockExchangeMapper.toEntity(dto)).thenReturn(stockExchange);
        when(stockExchangeRepository.save(stockExchange)).thenReturn(stockExchange);
        when(stockExchangeMapper.stockExchangeResourceFromStockExchange(stockExchange)).thenReturn(expectedResource);

        // When
        StockExchangeResource result = stockExchangeService.createStockExchange(dto);

        // Then
        assertEquals(expectedResource, result);
    }

    @Test
    public void delete_stock_exchange_success() {
        // Given
        String exchangeName = "NYSE";
        StockExchange stockExchange = new StockExchange();
        stockExchange.setName(exchangeName);

        when(stockExchangeRepository.findByName(exchangeName)).thenReturn(Optional.of(stockExchange));

        // When
        stockExchangeService.deleteStockExchange(exchangeName);

        // Then
        verify(stockExchangeRepository).delete(stockExchange);
    }

    @Test
    public void find_stock_exchange_by_name_success() {
        // Given
        String exchangeName = "NYSE";
        StockExchange stockExchange = new StockExchange();
        stockExchange.setName(exchangeName);
        StockExchangeResource expectedResource = new StockExchangeResource(1L, null, null, null, "NYSE", false, "New York Stock Exchange", null);

        when(stockExchangeRepository.findByName(exchangeName)).thenReturn(Optional.of(stockExchange));
        when(stockExchangeMapper.stockExchangeResourceFromStockExchange(stockExchange)).thenReturn(expectedResource);

        // When
        StockExchangeResource result = stockExchangeService.findByName(exchangeName);

        // Then
        assertEquals(expectedResource, result);
    }

    @Test
    public void get_all_stock_exchanges_with_stocks_success() {
        // Given
        List<StockExchange> expectedExchanges = List.of(new StockExchange(), new StockExchange());

        when(stockExchangeRepository.findAllWithStocks()).thenReturn(expectedExchanges);

        // When
        List<StockExchange> result = stockExchangeService.getAllStockExchanges(true);

        // Then
        assertEquals(expectedExchanges, result);
    }

    @Test
    public void add_stocks_to_stock_exchange_success() {
        // Given
        String exchangeName = "NYSE";
        List<String> stockNames = List.of("AAPL", "GOOGL");
        List<Stock> stocks = List.of(new Stock(), new Stock());
        stocks.get(0).setName("AAPL");
        stocks.get(1).setName("GOOGL");

        StockExchange stockExchange = new StockExchange();
        stockExchange.setName(exchangeName);

        when(stockService.findByNames(stockNames)).thenReturn(stocks);
        when(stockExchangeRepository.findByName(exchangeName)).thenReturn(Optional.of(stockExchange));

        // When
        stockExchangeService.putStockToStockExchange(exchangeName, stockNames);

        // Then
        assertEquals(new HashSet<>(stocks), stockExchange.getStocks());
    }

    @Test
    public void update_stocks_in_stock_exchange_success() {
        // Given
        String exchangeName = "NYSE";
        List<String> stockNames = List.of("AAPL", "GOOGL");
        List<Stock> stocks = List.of(new Stock(), new Stock());
        stocks.get(0).setName("AAPL");
        stocks.get(1).setName("GOOGL");

        StockExchange stockExchange = new StockExchange();
        stockExchange.setName(exchangeName);

        when(stockService.findByNames(stockNames)).thenReturn(stocks);
        when(stockExchangeRepository.findByName(exchangeName)).thenReturn(Optional.of(stockExchange));

        // When
        stockExchangeService.updateStockToStockExchange(exchangeName, stockNames);

        // Then
        assertTrue(stockExchange.getStocks().containsAll(stocks));
    }


    @Test
    public void delete_non_existent_stock_exchange_throws_exception() {
        // Given
        String exchangeName = "NonExistent";

        when(stockExchangeRepository.findByName(exchangeName)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            stockExchangeService.deleteStockExchange(exchangeName);
        });
    }

    @Test
    public void find_non_existent_stock_exchange_throws_exception() {
        // Given
        String exchangeName = "NonExistent";

        when(stockExchangeRepository.findByName(exchangeName)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            stockExchangeService.findByName(exchangeName);
        });
    }

    @Test
    public void add_non_existent_stocks_to_stock_exchange_throws_exception() {
        // Given
        String exchangeName = "NYSE";
        List<String> stockNames = List.of("NonExistentStock");

        when(stockService.findByNames(stockNames)).thenReturn(List.of());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            stockExchangeService.putStockToStockExchange(exchangeName, stockNames);
        });
    }

    @Test
    public void update_stock_exchange_with_non_existent_stocks_throws_exception() {
        // Given
        String exchangeName = "NYSE";
        List<String> stockNames = List.of("NonExistentStock");

        when(stockService.findByNames(stockNames)).thenReturn(List.of());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            stockExchangeService.updateStockToStockExchange(exchangeName, stockNames);
        });
    }

    @Test
    public void multi_threaded_addition_of_non_existent_stocks_throws_exception() {
        // Given
        String exchangeName = "NYSE";
        List<String> stockNames = List.of("NonExistentStock");

        when(stockService.findByNames(stockNames)).thenReturn(List.of());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            stockExchangeService.putStockToStockExchangeMultiThread(exchangeName, stockNames);
        });
    }
}
