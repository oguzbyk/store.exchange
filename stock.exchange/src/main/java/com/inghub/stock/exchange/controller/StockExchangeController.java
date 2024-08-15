package com.inghub.stock.exchange.controller;

import com.inghub.stock.exchange.controller.dto.StockExchangeCreateDto;
import com.inghub.stock.exchange.controller.resource.StockExchangeResource;
import com.inghub.stock.exchange.entity.StockExchange;
import com.inghub.stock.exchange.service.StockExchangeService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stock-exchanges")
public class StockExchangeController {

    private final StockExchangeService stockExchangeService;

    @Autowired
    public StockExchangeController(StockExchangeService stockExchangeService) {
        this.stockExchangeService = stockExchangeService;
    }

    @Operation(summary = "Retrieve all StockExchanges")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of StockExchanges retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockExchange.class))})
    })
    @GetMapping
    public ResponseEntity<List<StockExchange>> getAllStockExchanges(
            @RequestParam(value = "retrieveStock", defaultValue = "true") boolean retrieveStock) {

        List<StockExchange> stockExchanges = stockExchangeService.getAllStockExchanges(retrieveStock);
        return ResponseEntity.ok(stockExchanges);
    }

    @GetMapping("/rate")
    public ResponseEntity<List<StockExchange>> getAllStockExchangesResilience4j() {
        List<StockExchange> stockExchanges = stockExchangeService.getAllStockExchangesResilience4j(true);
        return ResponseEntity.ok(stockExchanges);
    }

    @Operation(summary = "Retrieve a StockExchange by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StockExchange retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockExchange.class))}),
            @ApiResponse(responseCode = "404", description = "StockExchange not found", content = @Content)
    })
    @GetMapping("/{name}")
    public ResponseEntity<StockExchangeResource> getStockExchangeByName(@PathVariable String name) {
        StockExchangeResource stockExchange = stockExchangeService.findByName(name);
        return ResponseEntity.ok(stockExchange);
    }

    @Operation(summary = "Create a new StockExchange")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "StockExchange created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockExchangeResource.class))}),
            @ApiResponse(responseCode = "400", description = "Invalname input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<StockExchangeResource> createStockExchange(@RequestBody StockExchangeCreateDto stockExchangeDto) {
        StockExchangeResource stockExchange = stockExchangeService.createStockExchange(stockExchangeDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{name}")
                .buildAndExpand(stockExchange.name())
                .toUri();
        return ResponseEntity.created(location).body(stockExchange);
    }

    @Operation(summary = "Update Stocks in a StockExchange")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StockExchange updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockExchange.class))}),
            @ApiResponse(responseCode = "404", description = "StockExchange not found", content = @Content)
    })
    @PutMapping("/thread/{name}")
    public ResponseEntity<StockExchangeResource> putStockToStockExchangeMultiThread(@PathVariable("name") String name, @RequestParam List<String> stocknames) {
        StockExchangeResource updatedStockExchange = stockExchangeService.putStockToStockExchangeMultiThread(name, stocknames);
        return ResponseEntity.ok(updatedStockExchange);
    }

    @Operation(summary = "Update Stocks in a StockExchange")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StockExchange updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockExchange.class))}),
            @ApiResponse(responseCode = "404", description = "StockExchange not found", content = @Content)
    })
    @PutMapping("/{name}")
    public ResponseEntity<StockExchangeResource> putStockToStockExchange(@PathVariable("name") String name, @RequestParam List<String> stocknames) {
        StockExchangeResource updatedStockExchange = stockExchangeService.putStockToStockExchange(name, stocknames);
        return ResponseEntity.ok(updatedStockExchange);
    }

    @Operation(summary = "Patch Stocks in a StockExchange")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "StockExchange patched successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StockExchange.class))}),
            @ApiResponse(responseCode = "404", description = "StockExchange not found", content = @Content)
    })
    @PatchMapping("/{name}")
    public ResponseEntity<StockExchangeResource> patchStockToStockExchange(@PathVariable("name") String stockExchangeName, @RequestParam List<String> stockNames) {
        StockExchangeResource updatedStockExchange = stockExchangeService.updateStockToStockExchange(stockExchangeName, stockNames);
        return ResponseEntity.ok(updatedStockExchange);
    }

    @Operation(summary = "Remove all Stocks from a StockExchange")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All Stocks removed from StockExchange",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "StockExchange not found", content = @Content)
    })
    @DeleteMapping("remove/{name}")
    public ResponseEntity<Void> removeAllStocksFromStockExchange(@PathVariable("name") String name) {
        stockExchangeService.removeAllStocksFromStockExchange(name);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a StockExchange by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "StockExchange deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "StockExchange not found", content = @Content)
    })
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteStockExchange(@PathVariable String name) {
        stockExchangeService.deleteStockExchange(name);
        return ResponseEntity.noContent().build();
    }
}
