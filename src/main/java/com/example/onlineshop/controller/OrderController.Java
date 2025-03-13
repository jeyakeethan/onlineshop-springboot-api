package com.example.onlineshop.controller;

import com.example.onlineshop.dto.GuestOrderDTO;
import com.example.onlineshop.dto.OrderDTO;
import com.example.onlineshop.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final BuyerService buyerService;

    // Checkout and create an order for logged-in users
    @PostMapping("/checkout")
    public OrderDTO checkout(@RequestParam String userId) {
        return buyerService.checkout(userId); // Convert the Order entity to DTO before returning
    }

    // Guest checkout (for users who are not logged in)
    @PostMapping("/guest-checkout")
    public OrderDTO guestCheckout(@RequestBody GuestOrderDTO guestOrderDTO) {
        // Process the guest order and get the created Order entity
        return buyerService.processGuestCheckout(guestOrderDTO);
    }

    // Other order-related endpoints...
}
