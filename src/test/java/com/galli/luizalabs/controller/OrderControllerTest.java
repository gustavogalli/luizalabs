package com.galli.luizalabs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galli.luizalabs.dto.UserResponse;
import com.galli.luizalabs.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest {

    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        orderController = new OrderController(orderService, objectMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testUpload_ShouldReturnUserResponse() throws Exception {
        String fileContent = "0000000001Fulano da Silva                             00000000010000000001000000100020230101";
        MultipartFile file = new MockMultipartFile("file", "orders.txt", "text/plain", fileContent.getBytes());

        List<UserResponse> userResponses = List.of(new UserResponse(1L, "Fulano da Silva", null));

        when(orderService.processFile(file)).thenReturn(userResponses);

        mockMvc.perform(multipart("/api/orders/upload").file((MockMultipartFile) file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Fulano da Silva"));

        verify(orderService, times(1)).processFile(file);
    }

    @Test
    void testGetOrders_ShouldReturnFilteredOrders() throws Exception {
        Long orderId = 1L;
        List<UserResponse> userResponses = List.of(new UserResponse(1L, "Fulano da Silva", null));

        when(orderService.getFilteredOrders(orderId, null, null)).thenReturn(userResponses);

        mockMvc.perform(get("/api/orders")
                        .param("orderId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Fulano da Silva"));

        verify(orderService, times(1)).getFilteredOrders(orderId, null, null);
    }

    @Test
    void testGetOrders_WithDateRange_ShouldReturnFilteredOrders() throws Exception {
        Long orderId = null;
        String startDate = "2023-01-01";
        String endDate = "2023-12-31";
        List<UserResponse> userResponses = List.of(new UserResponse(1L, "Fulano da Silva", null));

        when(orderService.getFilteredOrders(orderId, LocalDate.parse(startDate), LocalDate.parse(endDate)))
                .thenReturn(userResponses);

        mockMvc.perform(get("/api/orders")
                        .param("startDate", startDate)
                        .param("endDate", endDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Fulano da Silva"));

        verify(orderService, times(1)).getFilteredOrders(orderId, LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @Test
    void testDownloadProcessedOrdersAsFile_ShouldReturnFile() throws Exception {
        List<UserResponse> userResponses = List.of(new UserResponse(1L, "Fulano da Silva", null));
        when(orderService.getAllOrders()).thenReturn(userResponses);

        mockMvc.perform(get("/api/orders/download"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("attachment; filename=orders.json")))
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));

        verify(orderService, times(1)).getAllOrders();
    }
}
