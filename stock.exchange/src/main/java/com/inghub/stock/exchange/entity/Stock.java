package com.inghub.stock.exchange.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Stock extends BaseEntity {

    @Column(unique = true)
    private String name;
    private String description;
    private BigDecimal currentPrice;
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(mappedBy = "stocks", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<StockExchange> stockExchanges = new HashSet<>();

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

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }


    public Set<StockExchange> getStockExchanges() {
        return stockExchanges;
    }

    public void setStockExchanges(Set<StockExchange> stockExchanges) {
        this.stockExchanges = stockExchanges;
    }
}
