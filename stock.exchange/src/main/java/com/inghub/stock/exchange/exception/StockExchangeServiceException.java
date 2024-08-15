package com.inghub.stock.exchange.exception;

public class StockExchangeServiceException extends RuntimeException {
    public StockExchangeServiceException(String message) {
        super(message);
    }

    public StockExchangeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
