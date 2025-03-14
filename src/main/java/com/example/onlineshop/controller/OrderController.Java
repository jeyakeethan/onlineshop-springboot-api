package com.example.onlineshop.controller;

import com.example.onlineshop.dto.GuestOrderDTO;
import com.example.onlineshop.dto.OrderDTO;
import com.example.onlineshop.dto.PaymentDTO;
import com.example.onlineshop.service.BuyerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final BuyerService buyerService;

    // Checkout and create an order for logged-in users
    @Operation(summary = "Create Order", description = "Create an order for logged-in users by processing checkout.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters.")
    })
    @PostMapping("/checkout")
    public OrderDTO checkout(@RequestParam String userId) {
        return buyerService.checkout(userId); // Convert the Order entity to DTO before returning
    }

    // Guest checkout (for users who are not logged in)
    @Operation(summary = "Guest Checkout", description = "Create an order for guest users by processing their guest checkout.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guest order created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid guest order data.")
    })
    @PostMapping("/guest-checkout")
    public OrderDTO guestCheckout(@RequestBody GuestOrderDTO guestOrderDTO) {
        // Process the guest order and get the created Order entity
        return buyerService.processGuestCheckout(guestOrderDTO);
    }

    // Get Order By ID
    @Operation(summary = "Get Order By ID", description = "Retrieve an order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    @GetMapping("/{orderId}")
    public OrderDTO getOrderById(@PathVariable Long orderId) {
        return buyerService.getOrderById(orderId);
    }

    // Get Orders By User
    @Operation(summary = "Get Orders By User", description = "Retrieve all orders placed by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "No orders found for the user.")
    })
    @GetMapping("/user/{userId}")
    public List<OrderDTO> getOrdersByUser(@PathVariable String userId) {
        return buyerService.getOrdersByUser(userId);
    }

    // Update Order Status
    @Operation(summary = "Update Order Status", description = "Update the status of an existing order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid order status.")
    })
    @PutMapping("/{orderId}/status")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        buyerService.updateOrderStatus(orderId, status);
        return "Order status updated successfully.";
    }

    // Process Order Payment
    @Operation(summary = "Order Payment", description = "Process the payment for an existing order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid payment details.")
    })
    @PostMapping("/{orderId}/payment")
    public String processPayment(@PathVariable Long orderId, @RequestBody PaymentDTO paymentDTO) {
        buyerService.processPayment(orderId, paymentDTO);
        return "Payment processed successfully.";
    }

    // Get Order History
    @Operation(summary = "Get Order History", description = "Retrieve all orders placed by a user, including past orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order history retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "No order history found.")
    })
    @GetMapping("/history/{userId}")
    public List<OrderDTO> getOrderHistory(@PathVariable String userId) {
        return buyerService.getOrderHistory(userId);
    }

    // Cancel Order
    @Operation(summary = "Cancel Order", description = "Cancel an existing order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order cancelled successfully."),
            @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    @DeleteMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId) {
        buyerService.cancelOrder(orderId);
        return "Order cancelled successfully.";
    }
}
