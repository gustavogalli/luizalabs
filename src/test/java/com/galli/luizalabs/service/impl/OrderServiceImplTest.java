package com.galli.luizalabs.service.impl;

import com.galli.luizalabs.dto.UserResponse;
import com.galli.luizalabs.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    private OrderServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new OrderServiceImpl();
    }

    @Test
    void testProcessFile_shouldParseFileAndGroupOrders() throws IOException {
        String content = "0000000001Fulano da Silva                              0000000001000000000100000000100020230101";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "orders.txt",
                "text/plain",
                new ByteArrayInputStream(content.getBytes())
        );

        List<UserResponse> responses = service.processFile(file);

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getUserId());
        assertEquals("Fulano da Silva", responses.get(0).getName());
        assertEquals(1, responses.get(0).getOrders().size());
        assertEquals("2023-01-01", responses.get(0).getOrders().get(0).getDate());
        assertEquals("1000.00", responses.get(0).getOrders().get(0).getTotal());
    }

    @Test
    void testGetFilteredOrders_shouldReturnFilteredByOrderIdAndDate() {
        Order line = Order.builder()
                .userId(1L)
                .userName("Fulano")
                .orderId(10L)
                .productId(100L)
                .productValue(new BigDecimal("123.45"))
                .purchaseDate(LocalDate.of(2023, 1, 1))
                .build();

        service.processFile(new MockMultipartFile(
                "file",
                "dummy.txt",
                "text/plain",
                "".getBytes()
        ));
        service.getClass().cast(service).cache = List.of(line);

        List<UserResponse> responses = service.getFilteredOrders(10L, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1));

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getUserId());
    }

    @Test
    void testProcessFile_invalidFormat_shouldThrowException() {
        String content = "0000000001NOMEINVÁLIDO";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "invalid.txt",
                "text/plain",
                content.getBytes()
        );

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.processFile(file));
        assertTrue(exception.getMessage().contains("Erro ao processar"));
    }
}
