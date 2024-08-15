package com.inghub.stock.exchange.annotation;

import com.inghub.stock.exchange.controller.resource.StockExchangeResource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class CacheStockExchangesAspect {

    @Autowired
    private CacheManager cacheManager;

    @Around("@annotation(cacheStockExchanges)")
    public Object cacheStockExchanges(ProceedingJoinPoint joinPoint, CacheStockExchanges cacheStockExchanges) throws Throwable {
        String cacheName = cacheStockExchanges.cacheName();
        String key = cacheStockExchanges.key();

        Object result = joinPoint.proceed();

        if (result instanceof List) {
            List<?> stockExchanges = (List<?>) result;
            Cache cache = cacheManager.getCache(cacheName);

            if (cache != null) {
                for (Object stockExchange : stockExchanges) {
                    if (stockExchange instanceof StockExchangeResource) {
                        StockExchangeResource exchange = (StockExchangeResource) stockExchange;
                        cache.put(exchange.name(), exchange);
                    }
                }
            }
        }

        return result;
    }
}
