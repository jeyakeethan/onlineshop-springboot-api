package com.example.onlineshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {

    private Long cartItemId;
    private Long inventoryId;
    private int quantity;
    private double priceAtAdd;
}
