package com.inghub.stock.exchange.controller.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record StockUpdateDto(

        @NotBlank(message = "{description.not-blank}")
        @Size(max = 1024, message = "{description.size}")
        String description,

        @NotNull(message = "{currentprice.not-null}")
        @DecimalMin(value = "0.0", inclusive = false, message = "{currentprice.positive}")
        @Digits(integer = 15, fraction = 2, message = "{currentprice.digits}")
        BigDecimal currentPrice
) {
}
