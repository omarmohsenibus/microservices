package com.omarmohseni.orderservice.service;

import com.omarmohseni.orderservice.dto.OrderLineItemRequest;
import com.omarmohseni.orderservice.dto.OrderRequest;
import com.omarmohseni.orderservice.model.Order;
import com.omarmohseni.orderservice.model.OrderLineItem;
import com.omarmohseni.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest request) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItemList = request
                .getOrderLineItemRequestList()
                .stream()
                .map(this::mapToOrderLineItemRequest)
                .toList();

        order.setOrderLineItemsList(orderLineItemList);

        orderRepository.save(order);
    }

    private OrderLineItem mapToOrderLineItemRequest(OrderLineItemRequest request) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(request.getPrice());
        orderLineItem.setQuantity(request.getQuantity());
        orderLineItem.setSkuCode(request.getSkuCode());

        return orderLineItem;
    }
}
