package com.galli.luizalabs.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderBuilder() {
        Long userId = 1L;
        String userName = "Fulano da Silva";
        Long orderId = 100L;
        Long productId = 200L;
        BigDecimal productValue = new BigDecimal("100.00");
        LocalDate purchaseDate = LocalDate.of(2023, 1, 1);

        Order order = Order.builder()
                .userId(userId)
                .userName(userName)
                .orderId(orderId)
                .productId(productId)
                .productValue(productValue)
                .purchaseDate(purchaseDate)
                .build();

        assertEquals(userId, order.getUserId());
        assertEquals(userName, order.getUserName());
        assertEquals(orderId, order.getOrderId());
        assertEquals(productId, order.getProductId());
        assertEquals(productValue, order.getProductValue());
        assertEquals(purchaseDate, order.getPurchaseDate());
    }

    @Test
    void testOrderSettersAndGetters() {
        Order order = new Order();

        order.setUserId(1L);
        order.setUserName("Fulano da Silva");
        order.setOrderId(100L);
        order.setProductId(200L);
        order.setProductValue(new BigDecimal("100.00"));
        order.setPurchaseDate(LocalDate.of(2023, 1, 1));

        assertEquals(1L, order.getUserId());
        assertEquals("Fulano da Silva", order.getUserName());
        assertEquals(100L, order.getOrderId());
        assertEquals(200L, order.getProductId());
        assertEquals(new BigDecimal("100.00"), order.getProductValue());
        assertEquals(LocalDate.of(2023, 1, 1), order.getPurchaseDate());
    }

    @Test
    void testOrderNoArgsConstructor() {
        Order order = new Order();

        assertNull(order.getUserId());
        assertNull(order.getUserName());
        assertNull(order.getOrderId());
        assertNull(order.getProductId());
        assertNull(order.getProductValue());
        assertNull(order.getPurchaseDate());
    }

    @Test
    void testOrderAllArgsConstructor() {
        Long userId = 1L;
        String userName = "Fulano da Silva";
        Long orderId = 100L;
        Long productId = 200L;
        BigDecimal productValue = new BigDecimal("100.00");
        LocalDate purchaseDate = LocalDate.of(2023, 1, 1);

        Order order = new Order(userId, userName, orderId, productId, productValue, purchaseDate);

        assertEquals(userId, order.getUserId());
        assertEquals(userName, order.getUserName());
        assertEquals(orderId, order.getOrderId());
        assertEquals(productId, order.getProductId());
        assertEquals(productValue, order.getProductValue());
        assertEquals(purchaseDate, order.getPurchaseDate());
    }

    @Test
    void testOrderToString() {
        Long userId = 1L;
        String userName = "Fulano da Silva";
        Long orderId = 100L;
        Long productId = 200L;
        BigDecimal productValue = new BigDecimal("100.00");
        LocalDate purchaseDate = LocalDate.of(2023, 1, 1);

        Order order = new Order(userId, userName, orderId, productId, productValue, purchaseDate);

        String orderString = order.toString();

        assertTrue(orderString.contains("userId=" + userId));
        assertTrue(orderString.contains("userName=" + userName));
        assertTrue(orderString.contains("orderId=" + orderId));
        assertTrue(orderString.contains("productId=" + productId));
        assertTrue(orderString.contains("productValue=" + productValue));
        assertTrue(orderString.contains("purchaseDate=" + purchaseDate));
    }
}
