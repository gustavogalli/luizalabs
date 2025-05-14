package com.galli.luizalabs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private Long userId;
    private String userName;
    private Long orderId;
    private Long productId;
    private BigDecimal productValue;
    private LocalDate purchaseDate;
}

