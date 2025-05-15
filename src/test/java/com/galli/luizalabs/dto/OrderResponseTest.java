package com.galli.luizalabs.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderResponseTest {

    @Test
    void testOrderResponseBuilder_shouldBuildCorrectly() {
        Long orderId = 1L;
        String total = "1000.00";
        String date = "2023-01-01";
        ProductResponse product1 = ProductResponse.builder()
                .productId(101L)
                .value("500.00")
                .build();
        ProductResponse product2 = ProductResponse.builder()
                .productId(102L)
                .value("500.00")
                .build();
        List<ProductResponse> products = Arrays.asList(product1, product2);

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(orderId)
                .total(total)
                .date(date)
                .products(products)
                .build();

        assertNotNull(orderResponse);
        assertEquals(orderId, orderResponse.getOrderId());
        assertEquals(total, orderResponse.getTotal());
        assertEquals(date, orderResponse.getDate());
        assertNotNull(orderResponse.getProducts());
        assertEquals(2, orderResponse.getProducts().size());
        assertEquals("500.00", orderResponse.getProducts().get(0).getValue());
        assertEquals("500.00", orderResponse.getProducts().get(1).getValue());
    }

    @Test
    void testOrderResponse_shouldHandleEmptyProductList() {
        Long orderId = 2L;
        String total = "0.00";
        String date = "2023-01-01";
        List<ProductResponse> products = Arrays.asList();

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(orderId)
                .total(total)
                .date(date)
                .products(products)
                .build();

        assertNotNull(orderResponse);
        assertEquals(orderId, orderResponse.getOrderId());
        assertEquals(total, orderResponse.getTotal());
        assertEquals(date, orderResponse.getDate());
        assertNotNull(orderResponse.getProducts());
        assertTrue(orderResponse.getProducts().isEmpty());
    }
}
