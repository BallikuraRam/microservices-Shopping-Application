package com.developer.service;

import com.developer.dto.InventoryResponse;
import com.developer.dto.OrderLineItemsDTO;
import com.developer.dto.OrderRequest;
import com.developer.model.Order;
import com.developer.model.OrderLineItems;
import com.developer.repository.OrderRepository;
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

    private final OrderRepository orderRepository ;
    private final WebClient.Builder webClientBuilder ;

    public void placeOrder(OrderRequest orderRequest)
    {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList =orderRequest.getOrderLineItemsDTOList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode).toList();

        // call inventory service , and place order if product is in stock
        InventoryResponse[] inventoryResponsesArray =webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                        .retrieve()
                .bodyToMono(InventoryResponse[].class)
                        .block();
        boolean allProductsinStack = Arrays.stream(inventoryResponsesArray)
                .anyMatch(inventoryResponse -> inventoryResponse.isInStock());
        if (allProductsinStack) {
            orderRepository.save(order);
        }else {
            throw new IllegalArgumentException("product is not in stock , try agian later ");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        return orderLineItems ;
    }
}
