package com.example.onlineshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryDTO {

    private Long inventoryId;
    private Long skuId;  // SKU ID for this inventory record
    private double costPrice;
    private double sellingPrice;
    private int stockAvailable;
}
