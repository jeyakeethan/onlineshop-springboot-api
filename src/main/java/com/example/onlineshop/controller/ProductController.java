package com.example.onlineshop.controller;

import com.example.onlineshop.dto.ProductDTO;
import com.example.onlineshop.service.BuyerService;
import com.example.onlineshop.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get All Products", description = "Retrieve a list of all products available in the store.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return buyerService.getAllProducts();  // Convert entities to DTOs before returning
    }

    // Admin can add a new product
    @Operation(summary = "Add Product", description = "Add a new product to the store (Admin only).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid product data."),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin access required.")
    })
    @PostMapping("/admin")
    public void addProduct(@RequestBody ProductDTO productDTO) {
        adminService.addProduct(productDTO);  // Convert DTO to entity and call admin service to add product
    }

    // Admin can update an existing product
    @Operation(summary = "Update Product", description = "Update the details of an existing product (Admin only).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid product data."),
            @ApiResponse(responseCode = "404", description = "Product not found."),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin access required.")
    })
    @PutMapping("/admin/{productId}")
    public void updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        adminService.updateProduct(productId, productDTO); // Admin service updates the product
    }

    // Admin can delete a product
    @Operation(summary = "Delete Product", description = "Delete an existing product from the store (Admin only).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Product not found."),
            @ApiResponse(responseCode = "403", description = "Forbidden: Admin access required.")
    })
    @DeleteMapping("/admin/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        adminService.deleteProduct(productId); // Admin service deletes the product
    }

    // Get Product By ID
    @Operation(summary = "Get Product By ID", description = "Retrieve a product by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    @GetMapping("/{productId}")
    public ProductDTO getProductById(@PathVariable Long productId) {
        return buyerService.getProductById(productId); // Retrieve a single product by ID
    }

    // Get Products By Category
    @Operation(summary = "Get Products By Category", description = "Retrieve products based on their category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products by category retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "No products found for this category.")
    })
    @GetMapping("/category/{category}")
    public List<ProductDTO> getProductsByCategory(@PathVariable String category) {
        return buyerService.getProductsByCategory(category); // Retrieve products by category
    }

    // Get Products By Subcategory
    @Operation(summary = "Get Products By Subcategory", description = "Retrieve products based on their subcategory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products by subcategory retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "No products found for this subcategory.")
    })
    @GetMapping("/subcategory/{subcategory}")
    public List<ProductDTO> getProductsBySubcategory(@PathVariable String subcategory) {
        return buyerService.getProductsBySubcategory(subcategory); // Retrieve products by subcategory
    }

}
