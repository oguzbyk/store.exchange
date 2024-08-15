package com.inghub.stock.exchange.controller;

import com.inghub.stock.exchange.controller.dto.StockCreateDto;
import com.inghub.stock.exchange.controller.dto.StockUpdateDto;
import com.inghub.stock.exchange.controller.resource.StockResource;
import com.inghub.stock.exchange.entity.Stock;
import com.inghub.stock.exchange.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @Operation(summary = "Retrieve all Stocks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Stocks retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockResource.class))})
    })
    @GetMapping
    public ResponseEntity<List<StockResource>> getAllStocks() {
        List<StockResource> stockResources = stockService.getAllStocks();
        return ResponseEntity.ok(stockResources);
    }

    @Operation(summary = "Retrieve a Stock by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Stock.class))}),
            @ApiResponse(responseCode = "404", description = "Stock not found", content = @Content)
    })
    @GetMapping("/{name}")
    public ResponseEntity<Stock> getStockByName(@PathVariable String name) {
        Stock stock = stockService.findByName(name);
        return ResponseEntity.ok(stock);
    }

    @Operation(summary = "Create a new Stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stock created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockResource.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<StockResource> createStock(@RequestBody @Validated StockCreateDto stock) {
        StockResource createdStock = stockService.createStock(stock);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdStock.id())
                .toUri();
        return ResponseEntity.created(location).body(createdStock);
    }

    @Operation(summary = "Update a Stock by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Stock.class))}),
            @ApiResponse(responseCode = "404", description = "Stock not found", content = @Content)
    })
    @PutMapping("/{name}")
    public ResponseEntity<Stock> updateStock(@PathVariable String name, @RequestBody @Validated StockUpdateDto stockDetails) {
        Stock updatedStock = stockService.updateStock(name, stockDetails);
        return ResponseEntity.ok(updatedStock);
    }

    @Operation(summary = "Delete a Stock by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Stock deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Stock not found", content = @Content)
    })
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteStock(@PathVariable String name) {
        stockService.deleteStock(name);
        return ResponseEntity.noContent().build();
    }
}
