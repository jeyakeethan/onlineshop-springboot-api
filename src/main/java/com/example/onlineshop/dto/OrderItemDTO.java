package com.example.onlineshop.dto;

import com.example.onlineshop.model.SKU;
import lombok.Data;

@Data
public class OrderItemDTO {
    private Long orderId;
    private SKUDTO skuDTO;
    private int quantity;
    private double price;

    public OrderItemDTO(Long orderId, SKU sku, int quantity, double price) {
        this.orderId = orderId;
        this.skuDTO = new SKUDTO(sku);
        this.quantity = quantity;
        this.price = price;
    }
}
