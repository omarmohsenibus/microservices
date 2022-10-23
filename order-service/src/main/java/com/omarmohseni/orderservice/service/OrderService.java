package com.omarmohseni.orderservice.service;

import com.omarmohseni.orderservice.dto.InventoryResponse;
import com.omarmohseni.orderservice.dto.OrderLineItemRequest;
import com.omarmohseni.orderservice.dto.OrderRequest;
import com.omarmohseni.orderservice.model.Order;
import com.omarmohseni.orderservice.model.OrderLineItem;
import com.omarmohseni.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest request) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItemList = request
                .getOrderLineItemRequestList()
                .stream()
                .map(this::mapToOrderLineItemRequest)
                .toList();

        order.setOrderLineItemsList(orderLineItemList);

        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItem::getSkuCode)
                .toList();

        // call inventory service, and place order if product is in stock
        InventoryResponse[] inventoryResponses = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);




        if(allProductsInStock) {
            orderRepository.save(order);
        }
        else {
            throw new IllegalArgumentException(("Product is not in stock, please try later"));
        }
    }

    private OrderLineItem mapToOrderLineItemRequest(OrderLineItemRequest request) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(request.getPrice());
        orderLineItem.setQuantity(request.getQuantity());
        orderLineItem.setSkuCode(request.getSkuCode());

        return orderLineItem;
    }
}
