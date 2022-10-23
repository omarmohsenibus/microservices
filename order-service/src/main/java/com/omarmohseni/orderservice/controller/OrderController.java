package com.omarmohseni.orderservice.controller;

import com.omarmohseni.orderservice.dto.OrderRequest;
import com.omarmohseni.orderservice.repository.OrderRepository;
import com.omarmohseni.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest request) {
        orderService.placeOrder(request);
        return "Order placed";
    }

}
