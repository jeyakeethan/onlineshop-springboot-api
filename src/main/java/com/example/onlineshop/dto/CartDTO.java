package com.example.onlineshop.dto;

import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDTO {

    private Long cartId;
    private String userId;
    private List<CartItemDTO> cartItems;

    public CartDTO(Cart cart) {
        this.cartId = cart.getCartId();
        this.userId = cart.getUser().getEmail();
        this.cartItems = cart.getCartItems().stream().map(CartItemDTO::new).toList();
    }
}
