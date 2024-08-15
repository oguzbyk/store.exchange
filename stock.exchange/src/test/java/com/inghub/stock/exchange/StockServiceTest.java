package com.inghub.stock.exchange;

import com.inghub.stock.exchange.controller.dto.StockCreateDto;
import com.inghub.stock.exchange.controller.dto.StockUpdateDto;
import com.inghub.stock.exchange.controller.mapper.StockMapper;
import com.inghub.stock.exchange.controller.resource.StockResource;
import com.inghub.stock.exchange.entity.Stock;
import com.inghub.stock.exchange.repository.StockRepository;
import com.inghub.stock.exchange.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockService stockService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create_stock_success() {
        // Given
        StockCreateDto dto = new StockCreateDto("AYG","test",BigDecimal.TEN);
        Stock stock = new Stock();
        StockResource expectedResource = new StockResource(1L,LocalDateTime.now(),LocalDateTime.now(),"admin","BYK","desc",BigDecimal.ONE,null);

        when(stockMapper.toEntity(dto)).thenReturn(stock);
        when(stockRepository.save(stock)).thenReturn(stock);
        when(stockMapper.stockResourceFromStock(stock)).thenReturn(expectedResource);

        // When
        StockResource result = stockService.createStock(dto);

        // Then
        assertEquals(expectedResource, result);
    }

    @Test
    public void create_stock_failure_due_to_repository_error() {
        // Given
        StockCreateDto dto = new StockCreateDto("AYG","test",BigDecimal.TEN);
        Stock stock = new Stock();

        when(stockMapper.toEntity(dto)).thenReturn(stock);
        when(stockRepository.save(stock)).thenThrow(RuntimeException.class);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            stockService.createStock(dto);
        });
    }

    @Test
    public void delete_stock_success() {
        // Given
        String stockName = "AAPL";
        Stock stock = new Stock();

        when(stockRepository.findByName(stockName)).thenReturn(Optional.of(stock));

        // When
        stockService.deleteStock(stockName);

        // Then
        verify(stockRepository).delete(stock);
        assertTrue(stock.getStockExchanges().isEmpty());
    }

    @Test
    public void delete_stock_failure_stock_not_found() {
        // Given
        String stockName = "AAPL";

        when(stockRepository.findByName(stockName)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            stockService.deleteStock(stockName);
        });
    }

    @Test
    public void update_stock_success() {
        // Given
        String stockName = "AAPL";
        StockUpdateDto dto = new StockUpdateDto("test",BigDecimal.TEN);
        Stock existingStock = new Stock();

        when(stockRepository.findByName(stockName)).thenReturn(Optional.of(existingStock));
        when(stockRepository.save(existingStock)).thenReturn(existingStock);

        // When
        Stock result = stockService.updateStock(stockName, dto);

        // Then
        verify(stockMapper).updateStockFromDto(dto, existingStock);
        assertEquals(existingStock, result);
    }

    @Test
    public void update_stock_failure_stock_not_found() {
        // Given
        String stockName = "AAPL";
        StockUpdateDto dto = new StockUpdateDto("test",BigDecimal.TEN);

        when(stockRepository.findByName(stockName)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            stockService.updateStock(stockName, dto);
        });
    }

    @Test
    public void find_stock_by_name_success() {
        // Given
        String stockName = "AAPL";
        Stock expectedStock = new Stock();

        when(stockRepository.findByName(stockName)).thenReturn(Optional.of(expectedStock));

        // When
        Stock result = stockService.findByName(stockName);

        // Then
        assertEquals(expectedStock, result);
    }

    @Test
    public void find_stock_by_name_failure_stock_not_found() {
        // Given
        String stockName = "AAPL";

        when(stockRepository.findByName(stockName)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            stockService.findByName(stockName);
        });
    }

    @Test
    public void find_stocks_by_names_success() {
        // Given
        List<String> stockNames = List.of("AAPL", "GOOGL");
        List<Stock> expectedStocks = List.of(new Stock(), new Stock());

        when(stockRepository.findAllByNameIn(stockNames)).thenReturn(expectedStocks);

        // When
        List<Stock> result = stockService.findByNames(stockNames);

        // Then
        assertEquals(expectedStocks, result);
    }

    @Test
    public void find_stocks_by_names_empty_list() {
        // Given
        List<String> stockNames = List.of("AAPL", "GOOGL");

        when(stockRepository.findAllByNameIn(stockNames)).thenReturn(List.of());

        // When
        List<Stock> result = stockService.findByNames(stockNames);

        // Then
        assertTrue(result.isEmpty());
    }


    @Test
    public void get_all_stocks_empty_list() {
        // Given
        when(stockRepository.findAll()).thenReturn(List.of());

        // When
        List<StockResource> result = stockService.getAllStocks();

        // Then
        assertTrue(result.isEmpty());
    }
}
