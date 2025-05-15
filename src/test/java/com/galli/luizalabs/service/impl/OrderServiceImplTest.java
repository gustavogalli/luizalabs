package com.galli.luizalabs.service.impl;

import com.galli.luizalabs.dto.UserResponse;
import com.galli.luizalabs.model.Order;
import com.galli.luizalabs.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessFile_shouldParseFileAndGroupOrders() throws Exception {
        String content = "0000000001Fulano da Silva                              0000000001000000000100000000100020230101";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "orders.txt",
                "text/plain",
                new ByteArrayInputStream(content.getBytes())
        );

        List<UserResponse> responses = service.processFile(file);

        verify(orderRepository, times(1)).saveAll(anyList());

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getUserId());
        assertEquals("Fulano da Silva", responses.get(0).getName());
        assertEquals(1, responses.get(0).getOrders().size());
        assertEquals("2023-01-01", responses.get(0).getOrders().get(0).getDate());
        assertEquals("1000.00", responses.get(0).getOrders().get(0).getTotal());
    }

    @Test
    void testGetFilteredOrders_shouldReturnFilteredByOrderIdAndDate() {
        Order order = Order.builder()
                .userId(1L)
                .userName("Fulano")
                .orderId(10L)
                .productId(100L)
                .productValue(new BigDecimal("123.45"))
                .purchaseDate(LocalDate.of(2023, 1, 1))
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<UserResponse> responses = service.getFilteredOrders(10L, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1));

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getUserId());
        assertEquals("Fulano", responses.get(0).getName());
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

    @Test
    void testGetAllOrders_shouldReturnGroupedOrders() {
        Order order = Order.builder()
                .userId(2L)
                .userName("Maria")
                .orderId(20L)
                .productId(200L)
                .productValue(new BigDecimal("999.99"))
                .purchaseDate(LocalDate.of(2024, 5, 10))
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<UserResponse> responses = service.getAllOrders();

        assertEquals(1, responses.size());
        assertEquals(2L, responses.get(0).getUserId());
        assertEquals("Maria", responses.get(0).getName());
        assertEquals("999.99", responses.get(0).getOrders().get(0).getTotal());
    }

    @Test
    void testGetFilteredOrders_withNullParams_shouldReturnAll() {
        Order order = Order.builder()
                .userId(3L)
                .userName("João")
                .orderId(30L)
                .productId(300L)
                .productValue(new BigDecimal("500.00"))
                .purchaseDate(LocalDate.of(2023, 6, 1))
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<UserResponse> responses = service.getFilteredOrders(null, null, null);

        assertEquals(1, responses.size());
        assertEquals("João", responses.get(0).getName());
    }

    @Test
    void testGetFilteredOrders_withOnlyStartDate() {
        Order order = Order.builder()
                .userId(4L)
                .userName("Carlos")
                .orderId(40L)
                .productId(400L)
                .productValue(new BigDecimal("100.00"))
                .purchaseDate(LocalDate.of(2023, 7, 15))
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<UserResponse> responses = service.getFilteredOrders(null, LocalDate.of(2023, 7, 1), null);

        assertEquals(1, responses.size());
        assertEquals("Carlos", responses.get(0).getName());
    }

    @Test
    void testGetFilteredOrders_withOnlyEndDate() {
        Order order = Order.builder()
                .userId(5L)
                .userName("Ana")
                .orderId(50L)
                .productId(500L)
                .productValue(new BigDecimal("250.00"))
                .purchaseDate(LocalDate.of(2023, 6, 10))
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<UserResponse> responses = service.getFilteredOrders(null, null, LocalDate.of(2023, 6, 15));

        assertEquals(1, responses.size());
        assertEquals("Ana", responses.get(0).getName());
    }

    @Test
    void testProcessFile_emptyFile_shouldReturnEmptyList() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.txt", "text/plain", new byte[0]
        );

        List<UserResponse> responses = service.processFile(emptyFile);

        verify(orderRepository, times(1)).saveAll(anyList());
        assertTrue(responses.isEmpty());
    }

    @Test
    void testProcessFile_multipleOrdersAndProducts_shouldGroupCorrectly() throws Exception {
        String content =
                "0000000001João da Silva                                0000000001000000000100000000100020230101\n" +
                        "0000000001João da Silva                                0000000001000000000200000000200020230101\n" +
                        "0000000001João da Silva                                0000000002000000000300000000300020230202";

        MockMultipartFile file = new MockMultipartFile(
                "file", "multi.txt", "text/plain", content.getBytes()
        );

        List<UserResponse> responses = service.processFile(file);

        assertEquals(1, responses.size());
        assertEquals(2, responses.get(0).getOrders().size());

        assertEquals(2, responses.get(0).getOrders().get(0).getProducts().size());
        assertEquals("3000.00", responses.get(0).getOrders().get(0).getTotal());

        assertEquals(1, responses.get(0).getOrders().get(1).getProducts().size());
        assertEquals("3000.00", responses.get(0).getOrders().get(1).getTotal());
    }

}
