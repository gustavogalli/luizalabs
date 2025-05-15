package com.galli.luizalabs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galli.luizalabs.dto.UserResponse;
import com.galli.luizalabs.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @PostMapping("/upload")
    public ResponseEntity<List<UserResponse>> upload(@RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(orderService.processFile(file));
    }

    @GetMapping("/orders/filter")
    public List<UserResponse> getFilteredOrders(
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) BigDecimal minValue,
            @RequestParam(required = false) BigDecimal maxValue
    ) {
        return orderService.getFilteredOrders(orderId, startDate, endDate, username, minValue, maxValue);
    }

    @GetMapping("/download")
    public ResponseEntity<FileSystemResource> downloadAsFile() {
        try {
            List<UserResponse> orders = orderService.getAllOrders();

            File tempFile = File.createTempFile("orders", ".json");
            tempFile.deleteOnExit();

            try (FileWriter writer = new FileWriter(tempFile)) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(writer, orders);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.json")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new FileSystemResource(tempFile));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
