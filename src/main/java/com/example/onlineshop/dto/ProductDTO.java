package com.example.onlineshop.dto;

import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.SKU;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ProductDTO {

    private Long productId;
    private String name;
    private String description;
    private CategoryDTO categoryDTO;  // CategoryDTO for mapping the subcategory
    private List<SKUDTO> skuDTOs;    // List of SKUs for the product
    private boolean isAvailable;

    public ProductDTO(Product product) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.categoryDTO = new CategoryDTO(product.getCategory());
        this.skuDTOs = product.getSkus().stream().map(SKUDTO::new).toList();
        this.isAvailable = product.isAvailable();
    }

    // No price or stock here, as it's handled in Inventory and SKU entities
}
