package com.example.onlineshop.dto;

import com.example.onlineshop.model.CartItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {

    private Long cartItemId;
    private Long inventoryId;
    private int quantity;
    private double priceAtAdd;

    public CartItemDTO (CartItem cartItem) {
        this.cartItemId = cartItem.getCartItemId();
        this.inventoryId = cartItem.getInventory().getInventoryId();
        this.quantity = cartItem.getQuantity();
        this.priceAtAdd = cartItem.getInventory().getSellingPrice();
    }
}
