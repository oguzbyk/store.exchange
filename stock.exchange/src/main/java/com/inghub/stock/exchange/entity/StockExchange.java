package com.inghub.stock.exchange.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class StockExchange extends BaseEntity {

    @Column(unique = true)
    private String name;
    private String description;
    private boolean liveInMarket=false;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "stock_exchange_stocks",
            joinColumns = @JoinColumn(name = "stock_exchange_id"),
            inverseJoinColumns = @JoinColumn(name = "stock_id")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Stock> stocks = new HashSet<>();


    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLiveInMarket() {
        return liveInMarket;
    }

    public void setLiveInMarket(boolean liveInMarket) {
        this.liveInMarket = liveInMarket;
    }

    public Set<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(Set<Stock> stocks) {
        this.stocks = stocks;
    }
}
