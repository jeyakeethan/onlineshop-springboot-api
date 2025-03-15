package com.example.onlineshop.dto;

import com.example.onlineshop.model.OrderItem;
import com.example.onlineshop.model.SKU;
import lombok.Data;

@Data
public class OrderItemDTO {
    private Long orderId;
    private SKUDTO skuDTO;
    private int quantity;
    private double price;
    private boolean isReturned;

    public OrderItemDTO(OrderItem orderItem) {
        this.orderId = orderItem.getOrder().getOrderId();
        this.skuDTO = new SKUDTO(orderItem.getSku());
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPriceAtPurchase();
        this.isReturned = orderItem.isReturned();
    }
}
