package com.example.onlineshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "sku_id", nullable = false)
    private SKU sku;

    private int quantity;
    private double priceAtPurchase;  // Storing price at the time of purchase

    private boolean isReturned;

    public OrderItem(SKU sku, int quantity, double priceAtPurchase) {
        this.sku = sku;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }
}
