package com.galli.luizalabs.controller;

import com.galli.luizalabs.dto.UserResponse;
import com.galli.luizalabs.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/upload")
    public ResponseEntity<List<UserResponse>> upload(@RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(orderService.processFile(file));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getOrders(
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(orderService.getFilteredOrders(orderId, startDate, endDate));
    }
}
