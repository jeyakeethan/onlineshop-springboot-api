package com.example.onlineshop.dto;

import com.example.onlineshop.model.Inventory;
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

    public InventoryDTO(Inventory inventory) {
        this.inventoryId = inventory.getInventoryId();
        this.skuId = inventory.getSku().getSkuId();
        this.costPrice = inventory.getCostPrice();
        this.sellingPrice = inventory.getSellingPrice();
        this.stockAvailable = inventory.getStockAvailable();
    }
}
