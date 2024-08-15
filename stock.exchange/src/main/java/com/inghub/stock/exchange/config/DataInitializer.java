package com.inghub.stock.exchange.config;

import com.inghub.stock.exchange.entity.AppUser;
import com.inghub.stock.exchange.entity.Stock;
import com.inghub.stock.exchange.entity.StockExchange;
import com.inghub.stock.exchange.repository.AppUserRepository;
import com.inghub.stock.exchange.repository.StockRepository;
import com.inghub.stock.exchange.repository.StockExchangeRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadUserData(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (appUserRepository.count() == 0) {
                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                appUserRepository.save(admin);

                AppUser appUser = new AppUser();
                appUser.setUsername("user");
                appUser.setPassword(passwordEncoder.encode("user123"));
                appUser.setRole("USER");
                appUserRepository.save(appUser);
            }
        };
    }

    @Bean
    public CommandLineRunner loadStockData(StockRepository stockRepository) {
        return args -> {
            if (stockRepository.count() == 0) {

                Stock stock1 = new Stock();
                stock1.setName("Apple Inc.");
                stock1.setDescription("Technology company");
                stock1.setCurrentPrice(new BigDecimal("150.00"));
                stock1.setModifiedBy("system");
                stockRepository.save(stock1);

                Stock stock2 = new Stock();
                stock2.setName("Microsoft Corp.");
                stock2.setDescription("Software company");
                stock2.setCurrentPrice(new BigDecimal("280.50"));
                stock2.setModifiedBy("system");
                stockRepository.save(stock2);

                Stock stock3 = new Stock();
                stock3.setName("Google LLC");
                stock3.setDescription("Internet services");
                stock3.setCurrentPrice(new BigDecimal("2750.00"));
                stock3.setModifiedBy("system");
                stockRepository.save(stock3);

                Stock stock4 = new Stock();
                stock4.setName("Amazon.com Inc.");
                stock4.setDescription("E-commerce company");
                stock4.setCurrentPrice(new BigDecimal("3400.00"));
                stock4.setModifiedBy("system");
                stockRepository.save(stock4);

                Stock stock5 = new Stock();
                stock5.setName("Tesla Inc.");
                stock5.setDescription("Electric vehicle company");
                stock5.setCurrentPrice(new BigDecimal("700.00"));
                stock5.setModifiedBy("system");
                stockRepository.save(stock5);
            }
        };
    }

    @Bean
    public CommandLineRunner loadStockExchangeData(StockExchangeRepository stockExchangeRepository,StockRepository stockRepository) {
        return args -> {
            if (stockExchangeRepository.count() == 0) {

                StockExchange exchange1 = new StockExchange();
                exchange1.setName("New York Stock Exchange");
                exchange1.setDescription("The largest stock exchange in the world by market capitalization.");
                exchange1.setLiveInMarket(false);
                exchange1.setModifiedBy("system");
                stockExchangeRepository.save(exchange1);

                StockExchange exchange2 = new StockExchange();
                exchange2.setName("NASDAQ");
                exchange2.setDescription("The second-largest stock exchange in the world by market capitalization.");
                exchange2.setLiveInMarket(false);
                exchange2.setModifiedBy("system");
                stockExchangeRepository.save(exchange2);

                StockExchange exchange3 = new StockExchange();
                exchange3.setName("London Stock Exchange");
                exchange3.setDescription("The primary stock exchange in the United Kingdom.");
                exchange3.setLiveInMarket(false);
                exchange3.setModifiedBy("system");
                stockExchangeRepository.save(exchange3);

                StockExchange exchange4 = new StockExchange();
                exchange4.setName("Tokyo Stock Exchange");
                exchange4.setDescription("The largest stock exchange in Japan.");
                exchange4.setLiveInMarket(false);
                exchange4.setModifiedBy("system");
                stockExchangeRepository.save(exchange4);

                StockExchange exchange5 = new StockExchange();
                exchange5.setName("Shanghai Stock Exchange");
                exchange5.setDescription("One of the two stock exchanges operating independently in mainland China.");
                exchange5.setLiveInMarket(true);
                exchange5.setModifiedBy("system");
                Set<Stock> stockSet = new HashSet<>(stockRepository.findAll());
                exchange5.setStocks(stockSet);
                stockExchangeRepository.save(exchange5);
            }
        };
    }
}
