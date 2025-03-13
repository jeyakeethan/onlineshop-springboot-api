package com.example.onlineshop.controller;

import com.example.onlineshop.dto.ProductDTO;
import com.example.onlineshop.dto.OrderDTO;
import com.example.onlineshop.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Add a new product
    @PostMapping("/product/add")
    public String addProduct(@RequestBody ProductDTO productDTO) {
        adminService.addProduct(productDTO);
        return "Product added successfully.";
    }

    // Update stock of a product
    @PutMapping("/product/stock")
    public String updateStock(@RequestParam Long inventoryId, @RequestParam int newStock) {
        adminService.updateStock(inventoryId, newStock);
        return "Stock updated successfully.";
    }

    // Get all orders
    @GetMapping("/orders")
    public List<OrderDTO> getAllOrders() {
        return adminService.getAllOrders();
    }
}
