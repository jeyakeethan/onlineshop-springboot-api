package com.example.onlineshop.controller;

import com.example.onlineshop.dto.ProductDTO;
import com.example.onlineshop.dto.OrderDTO;
import com.example.onlineshop.dto.UserDTO;
import com.example.onlineshop.dto.RoleDTO;
import com.example.onlineshop.service.AdminService;
import com.example.onlineshop.service.UserService;
import com.example.onlineshop.service.RoleService;
import com.example.onlineshop.service.ProductService;
import com.example.onlineshop.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin Operations", description = "APIs for managing admin operations such as product management, user management, and reports.")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReportService reportService;

    // Add a new product
    @Operation(summary = "Add a new product", description = "Add a new product to the catalog.")
    @ApiResponse(responseCode = "200", description = "Product added successfully.")
    @PostMapping("/product/add")
    public String addProduct(@RequestBody ProductDTO productDTO) {
        adminService.addProduct(productDTO);
        return "Product added successfully.";
    }

    // Update stock of a product
    @Operation(summary = "Update stock of a product", description = "Update the stock for a specific product.")
    @ApiResponse(responseCode = "200", description = "Stock updated successfully.")
    @PutMapping("/product/stock")
    public String updateStock(
            @Parameter(description = "Inventory ID of the product whose stock is to be updated", required = true)
            @RequestParam Long inventoryId,
            @Parameter(description = "New stock quantity for the product", required = true)
            @RequestParam int newStock) {
        adminService.updateStock(inventoryId, newStock);
        return "Stock updated successfully.";
    }

    // Get all orders
    @Operation(summary = "Get all orders", description = "Retrieve all orders placed in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all orders.")
    @GetMapping("/orders")
    public List<OrderDTO> getAllOrders() {
        return adminService.getAllOrders();
    }

    // Manage Users

    @Operation(summary = "Create a new user", description = "Create a new user in the system.")
    @ApiResponse(responseCode = "200", description = "User created successfully.")
    @PostMapping("/user/create")
    public String createUser(@RequestBody UserDTO userDTO) {
        userService.addUser(userDTO);
        return "User created successfully.";
    }

    @Operation(summary = "Update a user", description = "Update the details of an existing user.")
    @ApiResponse(responseCode = "200", description = "User updated successfully.")
    @PutMapping("/user/{userId}")
    public String updateUser(
            @Parameter(description = "ID of the user to be updated", required = true)
            @PathVariable String userId,
            @RequestBody UserDTO userDTO) {
        userService.updateUser(userId, userDTO);
        return "User updated successfully.";
    }

    @Operation(summary = "Delete a user", description = "Delete a user by their ID.")
    @ApiResponse(responseCode = "200", description = "User deleted successfully.")
    @DeleteMapping("/user/{userId}")
    public String deleteUser(
            @Parameter(description = "ID of the user to be deleted", required = true)
            @PathVariable String userId) {
        userService.deleteUserByEmail(userId);
        return "User deleted successfully.";
    }

    @Operation(summary = "Get user by ID", description = "Retrieve details of a user by their ID.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user details.")
    @GetMapping("/user/{userId}")
    public UserDTO getUserById(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all users.")
    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // Manage Roles

    @Operation(summary = "Create a new role", description = "Create a new role in the system.")
    @ApiResponse(responseCode = "200", description = "Role created successfully.")
    @PostMapping("/role/create")
    public String createRole(@RequestBody RoleDTO roleDTO) {
        roleService.createRole(roleDTO);
        return "Role created successfully.";
    }

    @Operation(summary = "Update a role", description = "Update the details of an existing role.")
    @ApiResponse(responseCode = "200", description = "Role updated successfully.")
    @PutMapping("/role/{roleId}")
    public String updateRole(
            @Parameter(description = "ID of the role to be updated", required = true)
            @PathVariable Long roleId,
            @RequestBody RoleDTO roleDTO) {
        roleService.updateRole(roleId, roleDTO);
        return "Role updated successfully.";
    }

    @Operation(summary = "Delete a role", description = "Delete a role by its ID.")
    @ApiResponse(responseCode = "200", description = "Role deleted successfully.")
    @DeleteMapping("/role/{roleId}")
    public String deleteRole(
            @Parameter(description = "ID of the role to be deleted", required = true)
            @PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return "Role deleted successfully.";
    }

    @Operation(summary = "Get role by ID", description = "Retrieve details of a role by its ID.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved role details.")
    @GetMapping("/role/{roleId}")
    public RoleDTO getRoleById(@PathVariable Long roleId) {
        return roleService.getRoleById(roleId);
    }

    @Operation(summary = "Get all roles", description = "Retrieve a list of all roles.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all roles.")
    @GetMapping("/roles")
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    // Manage Permissions

    @Operation(summary = "Assign role to user", description = "Assign a specific role to a user.")
    @ApiResponse(responseCode = "200", description = "Role assigned to user successfully.")
    @PostMapping("/user/{userId}/role/{roleId}")
    public String assignRoleToUser(
            @Parameter(description = "ID of the user to be assigned the role", required = true)
            @PathVariable String userId,
            @Parameter(description = "ID of the role to be assigned", required = true)
            @PathVariable Long roleId) {
        roleService.assignRoleToUser(userId, roleId);
        return "Role assigned to user successfully.";
    }

    @Operation(summary = "Remove role from user", description = "Remove a specific role from a user.")
    @ApiResponse(responseCode = "200", description = "Role removed from user successfully.")
    @DeleteMapping("/user/{userId}/role/{roleId}")
    public String removeRoleFromUser(
            @Parameter(description = "ID of the user to remove the role from", required = true)
            @PathVariable String userId,
            @Parameter(description = "ID of the role to be removed", required = true)
            @PathVariable Long roleId) {
        roleService.removeRoleFromUser(userId, roleId);
        return "Role removed from user successfully.";
    }

    // Manage Products

    @Operation(summary = "Approve product", description = "Approve a product after review.")
    @ApiResponse(responseCode = "200", description = "Product approved successfully.")
    @PutMapping("/product/approve")
    public String approveProduct(@RequestBody ProductDTO productDTO) {
        productService.approveProduct(productDTO);
        return "Product approved successfully.";
    }

    @Operation(summary = "Reject product", description = "Reject a product after review.")
    @ApiResponse(responseCode = "200", description = "Product rejected successfully.")
    @PutMapping("/product/reject/{productId}")
    public String rejectProduct(
            @Parameter(description = "ID of the product to be rejected", required = true)
            @PathVariable Long productId) {
        productService.rejectProduct(productId);
        return "Product rejected successfully.";
    }

    // System Metrics/Reports

    @Operation(summary = "Get sales report", description = "Retrieve a report on sales.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved sales report.")
    @GetMapping("/report/sales")
    public String getSalesReport() {
        return reportService.getSalesReport();
    }

    @Operation(summary = "Get user statistics", description = "Retrieve user statistics.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user statistics.")
    @GetMapping("/report/users")
    public String getUserStatistics() {
        return reportService.getUserStatistics();
    }

    @Operation(summary = "Get product stats", description = "Retrieve statistics about products.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved product statistics.")
    @GetMapping("/report/products")
    public String getProductStats() {
        return reportService.getProductStats();
    }
}
