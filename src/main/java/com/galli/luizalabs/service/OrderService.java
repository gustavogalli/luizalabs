package com.galli.luizalabs.service;

import com.galli.luizalabs.dto.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<UserResponse> processFile(MultipartFile file) throws IOException;

    List<UserResponse> getFilteredOrders(Long orderId, LocalDate startDate, LocalDate endDate, String username, BigDecimal minValue, BigDecimal maxValue);

    List<UserResponse> getAllOrders();
}