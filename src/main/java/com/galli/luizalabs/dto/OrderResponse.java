package com.galli.luizalabs.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long orderId;
    private String total;
    private String date;
    private List<ProductResponse> products;
}

