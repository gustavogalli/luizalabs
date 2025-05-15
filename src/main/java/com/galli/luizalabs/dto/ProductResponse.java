package com.galli.luizalabs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    @NotNull(message = "O ID do produto não pode ser nulo")
    private Long productId;

    @NotBlank(message = "O valor do produto não pode ser vazio")
    private String value;
}
