package com.inghub.stock.exchange.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StockExchangeCreateDto(@NotBlank(message = "{name.not-blank}")
                                     @NotNull(message = "{name.not-null}")
                                     @Size(min = 1, max = 250, message = "{name.size}")
                                     String name,

                                     @NotBlank(message = "{description.not-blank}")
                                     @Size(max = 1024, message = "{description.size}")
                                     String description) {
}
