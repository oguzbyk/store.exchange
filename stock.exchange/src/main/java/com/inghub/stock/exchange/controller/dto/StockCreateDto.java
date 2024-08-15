package com.inghub.stock.exchange.controller.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record StockCreateDto(
        @NotBlank(message = "{name.not-blank}")
        @NotNull(message = "{name.not-null}")
        @Size(min = 1, max = 250, message = "{name.size}")
        String name,

        @NotBlank(message = "{description.not-blank}")
        @Size(min = 1,max = 1024, message = "{description.size}")
        String description,

        @NotNull(message = "{currentprice.not-null}")
        @DecimalMin(value = "0.0", inclusive = false, message = "{currentprice.positive}")
        @Digits(integer = 15, fraction = 2, message = "{currentprice.digits}")
        BigDecimal currentPrice
) {}
