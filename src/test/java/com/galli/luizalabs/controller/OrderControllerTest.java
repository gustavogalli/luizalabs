package com.galli.luizalabs.controller;

import com.galli.luizalabs.dto.UserResponse;
import com.galli.luizalabs.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testUpload_ShouldReturnUserResponse() throws Exception {
        // Arrange
        String fileContent = "0000000001Fulano da Silva                             00000000010000000001000000100020230101";
        MultipartFile file = new MockMultipartFile("file", "orders.txt", "text/plain", fileContent.getBytes());

        List<UserResponse> userResponses = List.of(new UserResponse(1L, "Fulano da Silva", null));

        when(orderService.processFile(file)).thenReturn(userResponses);

        // Act & Assert
        mockMvc.perform(multipart("/api/orders/upload").file((MockMultipartFile) file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Fulano da Silva"));

        verify(orderService, times(1)).processFile(file);
    }

    @Test
    void testGetOrders_ShouldReturnFilteredOrders() throws Exception {
        // Arrange
        Long orderId = 1L;
        List<UserResponse> userResponses = List.of(new UserResponse(1L, "Fulano da Silva", null));

        when(orderService.getFilteredOrders(orderId, null, null)).thenReturn(userResponses);

        // Act & Assert
        mockMvc.perform(get("/api/orders")
                        .param("orderId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Fulano da Silva"));

        verify(orderService, times(1)).getFilteredOrders(orderId, null, null);
    }

    @Test
    void testGetOrders_WithDateRange_ShouldReturnFilteredOrders() throws Exception {
        // Arrange
        Long orderId = null;
        String startDate = "2023-01-01";
        String endDate = "2023-12-31";
        List<UserResponse> userResponses = List.of(new UserResponse(1L, "Fulano da Silva", null));

        when(orderService.getFilteredOrders(orderId, LocalDate.parse(startDate), LocalDate.parse(endDate)))
                .thenReturn(userResponses);

        // Act & Assert
        mockMvc.perform(get("/api/orders")
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Fulano da Silva"));

        verify(orderService, times(1)).getFilteredOrders(orderId, LocalDate.parse(startDate), LocalDate.parse(endDate));
    }
}
