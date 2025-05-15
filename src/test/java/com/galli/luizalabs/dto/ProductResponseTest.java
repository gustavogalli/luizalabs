package com.galli.luizalabs.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductResponseTest {

    @Test
    void testProductResponseBuilder_shouldBuildCorrectly() {
        Long productId = 101L;
        String value = "500.00";

        ProductResponse productResponse = ProductResponse.builder()
                .productId(productId)
                .value(value)
                .build();

        assertNotNull(productResponse);
        assertEquals(productId, productResponse.getProductId());
        assertEquals(value, productResponse.getValue());
    }

    @Test
    void testProductResponseBuilder_withNullValue() {
        Long productId = 102L;
        String value = null;

        ProductResponse productResponse = ProductResponse.builder()
                .productId(productId)
                .value(value)
                .build();

        assertNotNull(productResponse);
        assertEquals(productId, productResponse.getProductId());
        assertNull(productResponse.getValue());
    }
}
