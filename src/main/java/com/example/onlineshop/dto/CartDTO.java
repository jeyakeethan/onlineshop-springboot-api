package com.example.onlineshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDTO {

    private Long cartId;
    private Long userId;
    private List<CartItemDTO> cartItems;
}
