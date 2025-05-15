package com.galli.luizalabs.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @Test
    void testUserResponseBuilder_shouldBuildCorrectly() {
        Long userId = 1L;
        String name = "Fulano da Silva";
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(1001L)
                .total("1500.00")
                .date("2023-01-01")
                .products(Arrays.asList(
                        ProductResponse.builder().productId(101L).value("500.00").build(),
                        ProductResponse.builder().productId(102L).value("1000.00").build()
                ))
                .build();

        UserResponse userResponse = UserResponse.builder()
                .userId(userId)
                .name(name)
                .orders(Arrays.asList(orderResponse))
                .build();

        assertNotNull(userResponse);
        assertEquals(userId, userResponse.getUserId());
        assertEquals(name, userResponse.getName());
        assertNotNull(userResponse.getOrders());
        assertEquals(1, userResponse.getOrders().size());
        assertEquals(orderResponse, userResponse.getOrders().get(0));
    }

    @Test
    void testUserResponseBuilder_withNullOrders() {
        Long userId = 2L;
        String name = "Maria Oliveira";

        UserResponse userResponse = UserResponse.builder()
                .userId(userId)
                .name(name)
                .orders(null)
                .build();

        assertNotNull(userResponse);
        assertEquals(userId, userResponse.getUserId());
        assertEquals(name, userResponse.getName());
        assertNull(userResponse.getOrders());
    }

    @Test
    void testUserResponseBuilder_withEmptyOrders() {
        Long userId = 3L;
        String name = "José Souza";

        UserResponse userResponse = UserResponse.builder()
                .userId(userId)
                .name(name)
                .orders(Arrays.asList())
                .build();

        assertNotNull(userResponse);
        assertEquals(userId, userResponse.getUserId());
        assertEquals(name, userResponse.getName());
        assertNotNull(userResponse.getOrders());
        assertTrue(userResponse.getOrders().isEmpty());
    }
}
