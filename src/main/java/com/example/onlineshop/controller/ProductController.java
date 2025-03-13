package com.example.onlineshop.controller;

import com.example.onlineshop.dto.ProductDTO;
import com.example.onlineshop.service.BuyerService;
import com.example.onlineshop.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private BuyerService buyerService;

    @Autowired
    private AdminService adminService;

    // View all products
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return buyerService.getAllProducts();  // Convert entities to DTOs before returning
    }

    // Admin can add a new product
    @PostMapping("/admin")
    public void addProduct(@RequestBody ProductDTO productDTO) {
        // Convert DTO to entity and call admin service to add product
    }

    // Other product-related endpoints...
}
