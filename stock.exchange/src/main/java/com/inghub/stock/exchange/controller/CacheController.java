package com.inghub.stock.exchange.controller;

import com.inghub.stock.exchange.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    private final CacheService cacheService;

    @Autowired
    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/names")
    public Collection<String> getCacheNames() {
        return cacheService.getAllCacheNames();
    }

    @GetMapping("/contents")
    public ResponseEntity<String> getCacheContents() {
        return ResponseEntity.ok(cacheService.getAllCacheContents());
    }
}
