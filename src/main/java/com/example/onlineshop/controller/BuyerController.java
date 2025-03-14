package com.example.onlineshop.controller;

import com.example.onlineshop.dto.*;
import com.example.onlineshop.service.BuyerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Buyer Operations", description = "APIs for buyers to browse and purchase products")
@RestController
@RequestMapping("/api/buyer")
public class BuyerController {

    @Autowired
    private BuyerService buyerService;

    // Product Browsing
    @Operation(summary = "Get all products", description = "Retrieve a list of all available products")
    @GetMapping("/products")
    public List<ProductDTO> getAllProducts() {
        return buyerService.getAllProducts();
    }

    @Operation(summary = "Get product by ID", description = "Retrieve product details by product ID")
    @GetMapping("/product/{productId}")
    public ProductDTO getProductById(@PathVariable Long productId) {
        return buyerService.getProductById(productId);
    }

    @Operation(summary = "Search products", description = "Search products based on a keyword")
    @GetMapping("/products/search")
    public List<ProductDTO> searchProducts(@RequestParam String keyword) {
        return buyerService.searchProducts(keyword);
    }

    @Operation(summary = "Filter products", description = "Filter products by category and subcategory")
    @GetMapping("/products/filter")
    public List<ProductDTO> filterProducts(@RequestParam String category, @RequestParam String subcategory) {
        return buyerService.filterProducts(category, subcategory);
    }

    // Cart Operations
    @Operation(summary = "Get cart", description = "Retrieve the user's shopping cart")
    @GetMapping("/cart")
    public CartDTO getCart(@RequestParam String userId) {
        return buyerService.getCart(userId);
    }

    @Operation(summary = "Add product to cart", description = "Add a product to the user's shopping cart")
    @PostMapping("/cart/add")
    public void addProductToCart(@RequestParam Long inventoryId, @RequestParam Integer quantity) {
        buyerService.addProductToCart(inventoryId, quantity);
    }

    @Operation(summary = "Remove product from cart", description = "Remove a product from the user's shopping cart")
    @DeleteMapping("/cart/remove")
    public void removeProductFromCart(@RequestParam Long productId) {
        buyerService.removeProductFromCart(productId);
    }

    @Operation(summary = "Update cart product quantity", description = "Update the quantity of a product in the user's shopping cart")
    @PutMapping("/cart/update")
    public void updateCartProductQuantity(@RequestParam Long productId, @RequestParam Integer quantity) {
        buyerService.updateCartProductQuantity(productId, quantity);
    }

    @Operation(summary = "Clear cart", description = "Remove all products from the user's shopping cart")
    @DeleteMapping("/cart/clear")
    public void clearCart(@RequestParam String userId) {
        buyerService.clearCart(userId);
    }

    @Operation(summary = "Checkout", description = "Proceed with the checkout process by placing an order")
    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkout(@RequestParam String userId) {
        try {
            // Delegate the checkout process to the service layer
            OrderDTO order = buyerService.checkout(userId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            // Handle errors such as empty cart, payment failure, etc.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(summary = "Get orders by user", description = "Retrieve all orders placed by a specific user")
    @GetMapping("/orders")
    public List<OrderDTO> getOrdersByUser(@RequestParam String userId) {
        return buyerService.getOrdersByUser(userId);
    }

    @Operation(summary = "Get order by ID", description = "Retrieve order details by order ID")
    @GetMapping("/order/{orderId}")
    public OrderDTO getOrderById(@PathVariable Long orderId) {
        return buyerService.getOrderById(orderId);
    }

    @Operation(summary = "Update order status", description = "Update the status of an existing order")
    @PutMapping("/order/update-status")
    public void updateOrderStatus(@RequestParam Long orderId, @RequestParam String status) {
        buyerService.updateOrderStatus(orderId, status);
    }

    // Profile Management
    @Operation(summary = "Get user profile", description = "Retrieve the profile details of a user")
    @GetMapping("/profile")
    public UserDTO getUserProfile(@RequestParam String userId) {
        return buyerService.getUserProfile(userId);
    }

    @Operation(summary = "Update user profile", description = "Update the profile details of a user")
    @PutMapping("/profile/update")
    public void updateUserProfile(@RequestParam String userId, @RequestBody UserDTO userDTO) {
        buyerService.updateUserProfile(userId, userDTO);
    }
}
