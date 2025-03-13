package com.example.onlineshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "skus")
@Getter
@Setter
public class SKU {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skuId;  // Unique identifier for SKU

    private String size;  // Size of the SKU
    private String color;  // Color of the SKU
    private String skuCode;  // SKU code (unique identifier for the variant)

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)  // Link SKU to Product
    private Product product;  // The product this SKU belongs to

    @OneToMany(mappedBy = "sku", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventory> inventories = new ArrayList<>();
}
