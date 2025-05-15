package com.galli.luizalabs.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderBuilder() {
        Order order = Order.builder()
                .id(1L)
                .orderId(100L)
                .userId(1L)
                .userName("Fulano da Silva")
                .productId(200L)
                .productValue(new BigDecimal("100.00"))
                .purchaseDate(LocalDate.of(2023, 1, 1))
                .build();

        assertEquals(1L, order.getId());
        assertEquals(100L, order.getOrderId());
        assertEquals(1L, order.getUserId());
        assertEquals("Fulano da Silva", order.getUserName());
        assertEquals(200L, order.getProductId());
        assertEquals(new BigDecimal("100.00"), order.getProductValue());
        assertEquals(LocalDate.of(2023, 1, 1), order.getPurchaseDate());
    }

    @Test
    void testOrderSettersAndGetters() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderId(100L);
        order.setUserId(1L);
        order.setUserName("Fulano da Silva");
        order.setProductId(200L);
        order.setProductValue(new BigDecimal("100.00"));
        order.setPurchaseDate(LocalDate.of(2023, 1, 1));

        assertEquals(1L, order.getId());
        assertEquals(100L, order.getOrderId());
        assertEquals(1L, order.getUserId());
        assertEquals("Fulano da Silva", order.getUserName());
        assertEquals(200L, order.getProductId());
        assertEquals(new BigDecimal("100.00"), order.getProductValue());
        assertEquals(LocalDate.of(2023, 1, 1), order.getPurchaseDate());
    }

    @Test
    void testOrderNoArgsConstructor() {
        Order order = new Order();

        assertNull(order.getId());
        assertNull(order.getOrderId());
        assertNull(order.getUserId());
        assertNull(order.getUserName());
        assertNull(order.getProductId());
        assertNull(order.getProductValue());
        assertNull(order.getPurchaseDate());
    }

    @Test
    void testOrderAllArgsConstructor() {
        Order order = new Order(
                1L,
                100L,
                1L,
                "Fulano da Silva",
                200L,
                new BigDecimal("100.00"),
                LocalDate.of(2023, 1, 1)
        );

        assertEquals(1L, order.getId());
        assertEquals(100L, order.getOrderId());
        assertEquals(1L, order.getUserId());
        assertEquals("Fulano da Silva", order.getUserName());
        assertEquals(200L, order.getProductId());
        assertEquals(new BigDecimal("100.00"), order.getProductValue());
        assertEquals(LocalDate.of(2023, 1, 1), order.getPurchaseDate());
    }

    @Test
    void testOrderToString() {
        Order order = new Order(
                1L,
                100L,
                1L,
                "Fulano da Silva",
                200L,
                new BigDecimal("100.00"),
                LocalDate.of(2023, 1, 1)
        );

        String orderString = order.toString();

        assertTrue(orderString.contains("id=1"));
        assertTrue(orderString.contains("orderId=100"));
        assertTrue(orderString.contains("userId=1"));
        assertTrue(orderString.contains("userName=Fulano da Silva"));
        assertTrue(orderString.contains("productId=200"));
        assertTrue(orderString.contains("productValue=100.00"));
        assertTrue(orderString.contains("purchaseDate=2023-01-01"));
    }
}
