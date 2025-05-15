package com.galli.luizalabs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    @NotNull(message = "O ID do usuário não pode ser nulo")
    private Long userId;

    @NotBlank(message = "O nome do usuário não pode ser vazio")
    private String name;

    @NotNull(message = "A lista de pedidos não pode ser nula")
    private List<OrderResponse> orders;
}
