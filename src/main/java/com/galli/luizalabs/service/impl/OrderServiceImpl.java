package com.galli.luizalabs.service.impl;

import com.galli.luizalabs.dto.OrderResponse;
import com.galli.luizalabs.dto.ProductResponse;
import com.galli.luizalabs.dto.UserResponse;
import com.galli.luizalabs.model.Order;
import com.galli.luizalabs.repository.OrderRepository;
import com.galli.luizalabs.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<UserResponse> processFile(MultipartFile file) {
        try {
            List<Order> lines = new BufferedReader(new InputStreamReader(file.getInputStream()))
                    .lines()
                    .map(this::parseLine)
                    .collect(Collectors.toList());

            orderRepository.saveAll(lines);

            return groupOrders(lines);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar arquivo: " + e.getMessage());
        }
    }

    @Override
    public List<UserResponse> getFilteredOrders(Long orderId, LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findAll();

        return groupOrders(
                orders.stream()
                        .filter(line -> orderId == null || line.getOrderId().equals(orderId))
                        .filter(line -> {
                            if (startDate == null && endDate == null) return true;
                            if (startDate != null && endDate != null)
                                return !line.getPurchaseDate().isBefore(startDate) && !line.getPurchaseDate().isAfter(endDate);
                            if (startDate != null)
                                return !line.getPurchaseDate().isBefore(startDate);
                            return !line.getPurchaseDate().isAfter(endDate);
                        })
                        .collect(Collectors.toList())
        );
    }

    private Order parseLine(String line) {
        Long userId = Long.parseLong(line.substring(0, 10));
        String userName = line.substring(10, 55).trim();
        Long orderId = Long.parseLong(line.substring(55, 65));
        Long productId = Long.parseLong(line.substring(65, 75));
        BigDecimal value = new BigDecimal(line.substring(75, 87).trim());
        LocalDate date = LocalDate.parse(line.substring(87, 95), DateTimeFormatter.ofPattern("yyyyMMdd"));

        return Order.builder()
                .userId(userId)
                .userName(userName)
                .orderId(orderId)
                .productId(productId)
                .productValue(value)
                .purchaseDate(date)
                .build();
    }

    private List<UserResponse> groupOrders(List<Order> lines) {
        return lines.stream()
                .collect(Collectors.groupingBy(Order::getUserId))
                .entrySet().stream()
                .map(userEntry -> {
                    Long userId = userEntry.getKey();
                    String userName = userEntry.getValue().get(0).getUserName();

                    Map<Long, List<Order>> ordersGrouped = userEntry.getValue().stream()
                            .collect(Collectors.groupingBy(Order::getOrderId));

                    List<OrderResponse> orderResponses = ordersGrouped.entrySet().stream().map(orderEntry -> {
                        List<Order> orders = orderEntry.getValue();
                        BigDecimal total = orders.stream()
                                .map(Order::getProductValue)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        List<ProductResponse> products = orders.stream()
                                .map(ol -> ProductResponse.builder()
                                        .productId(ol.getProductId())
                                        .value(formatDecimal(ol.getProductValue()))
                                        .build())
                                .collect(Collectors.toList());

                        return OrderResponse.builder()
                                .orderId(orderEntry.getKey())
                                .total(formatDecimal(total))
                                .date(orders.get(0).getPurchaseDate().toString())
                                .products(products)
                                .build();

                    }).collect(Collectors.toList());

                    return UserResponse.builder()
                            .userId(userId)
                            .name(userName)
                            .orders(orderResponses)
                            .build();
                }).collect(Collectors.toList());
    }

    private String formatDecimal(BigDecimal value) {
        return value.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
    }

    @Override
    public List<UserResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return groupOrders(orders);
    }
}