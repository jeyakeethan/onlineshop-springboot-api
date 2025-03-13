package com.example.onlineshop.dto;

import com.example.onlineshop.model.SKU;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SKUDTO {
    private String size;
    private String color;
    private String skuCode;
    private Long productId; // Reference to the product

    public SKUDTO(SKU sku) {
        this.size = sku.getSize();
        this.color = sku.getColor();
        this.skuCode = sku.getSkuCode();
        this.productId = sku.getProduct().getProductId();
    }
}
