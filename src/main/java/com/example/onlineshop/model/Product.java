package com.example.onlineshop.model;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "subcategory", referencedColumnName = "subcategory", nullable = false)// Foreign key for category
    private Category category;  // Subcategory as a Category entity

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)  // Mapping to SKUs (one-to-many relationship)
    private List<SKU> skus;  // SKU details for the product

    private boolean isAvailable;

    // Other fields for product
}