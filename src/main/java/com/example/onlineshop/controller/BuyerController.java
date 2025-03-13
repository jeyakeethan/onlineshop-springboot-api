package com.example.onlineshop.controller;

import com.example.onlineshop.dto.CartDTO;
import com.example.onlineshop.dto.OrderDTO;
import com.example.onlineshop.dto.ProductDTO;
import com.example.onlineshop.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buyer")
public class BuyerController {

    @Autowired
    private BuyerService buyerService;

    // Add product to cart
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam String userId, @RequestParam Long inventoryId, @RequestParam int quantity) {
        buyerService.addToCart(userId, inventoryId, quantity);
        return "Product added to cart successfully.";
    }

    // Checkout and place an order
    @PostMapping("/checkout")
    public OrderDTO checkout(@RequestParam String userId) {
        return buyerService.checkout(userId);
    }

    // Get all products
    @GetMapping("/products")
    public List<ProductDTO> getAllProducts() {
        return buyerService.getAllProducts();
    }
}
