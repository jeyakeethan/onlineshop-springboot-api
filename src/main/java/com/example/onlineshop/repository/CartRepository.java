package com.example.onlineshop.repository;

import com.example.onlineshop.dto.CartDTO;
import com.example.onlineshop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserEmail(String email);

    CartDTO getCartByUserId(String userId);
}
