package com.inghub.stock.exchange.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CacheService {

    private final CacheManager cacheManager;
    private final ObjectMapper objectMapper;

    public CacheService(CacheManager cacheManager, ObjectMapper objectMapper) {
        this.cacheManager = cacheManager;
        this.objectMapper = objectMapper;
    }

    public Collection<String> getAllCacheNames() {
        return cacheManager.getCacheNames();
    }

    public String getAllCacheContents() {
String jsosn="";
List<String> cacheNames = cacheManager.getCacheNames().stream().collect(Collectors.toList());
        for (String cache :
                cacheNames) {
            jsosn = jsosn + " " + getCacheContent(cache);
        }

        try {
            return  objectMapper
                    .writerWithDefaultPrettyPrinter() // enable pretty print
                    .writeValueAsString(jsosn);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



    private String getCacheContent(String cacheName) {
        Map<Object, Object> nativeCache = (Map<Object, Object>) cacheManager.getCache(cacheName).getNativeCache();

        List<Object> cacheContents = nativeCache.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        try {
            String json = objectMapper
                    .writerWithDefaultPrettyPrinter() // enable pretty print
                    .writeValueAsString(cacheContents);
            System.out.println(json);
            return json;
        } catch (JsonProcessingException e) {
            return "Error converting cache content to JSON: " + e.getMessage();
        }
    }



}
