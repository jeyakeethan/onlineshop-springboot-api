package com.example.onlineshop.service;

import com.example.onlineshop.dto.GuestOrderDTO;
import com.example.onlineshop.dto.OrderDTO;
import com.example.onlineshop.dto.ProductDTO;
import com.example.onlineshop.model.*;
import com.example.onlineshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BuyerService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private SKURepository skuRepository;

    @Autowired
    private AddressRepository addressRepository;

    // Add product to cart
    public void addToCart(String userId, Long inventoryId, int quantity) {
        Cart cart = cartRepository.findByUserEmail(userId);
        if (cart == null) {
            throw new RuntimeException("Cart not found for the user");
        }

        // Check if there's enough stock for the product variant (SKU)
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);
        if (inventory.get().getStockAvailable() < quantity) {
            throw new RuntimeException("Not enough stock available for this SKU");
        }

        // Add item to the cart or update quantity
        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getInventory().equals(inventory))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setInventory(inventory.get());
            newCartItem.setQuantity(quantity);
            newCartItem.setCart(cart);
            cart.getCartItems().add(newCartItem);
        }

        // Save the cart after adding/updating the item
        cartRepository.save(cart);
    }

    // Checkout cart and place an order
    public OrderDTO checkout(String userId) {
        Cart cart = cartRepository.findByUserEmail(userId);
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Calculate total price
        double totalPrice = calculateTotal(cart);

        // Create new Order
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setTotalPrice(totalPrice);
        order.setOrderStatus("PENDING");

        // Convert cart items to order items
        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    double price = cartItem.getInventory().getSellingPrice(); // SKU specific selling price
                    return new OrderItem(cartItem.getInventory().getSku(), cartItem.getQuantity(), price);
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        // Save the order and clear the cart
        orderRepository.save(order);
        cart.getCartItems().clear(); // Empty the cart after checkout
        cartRepository.save(cart);

        return new OrderDTO(order);
    }

    // Process guest checkout
    public OrderDTO processGuestCheckout(GuestOrderDTO guestOrderDTO) {
        Order order = new Order();
        order.setOrderStatus("PENDING");
        order.setTotalPrice(guestOrderDTO.getTotalPrice());

        // Save guest shipping address
        Address shippingAddress = new Address();
        shippingAddress.setStreet(guestOrderDTO.getShippingAddress().getStreet());
        shippingAddress.setCity(guestOrderDTO.getShippingAddress().getCity());
        shippingAddress.setState(guestOrderDTO.getShippingAddress().getState());
        shippingAddress.setCountry(guestOrderDTO.getShippingAddress().getCountry());
        shippingAddress.setPostalCode(guestOrderDTO.getShippingAddress().getPostalCode());
        addressRepository.save(shippingAddress);

        order.setShippingAddress(shippingAddress);

        // Convert guest order items to OrderItems
        List<OrderItem> orderItems = guestOrderDTO.getOrderItemDTOs().stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getSkuDTO().getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    // Retrieve SKU for product
                    SKU sku = product.getSkus().stream()
                            .filter(s -> s.getSkuId().equals(item.getSkuDTO().getSkuCode()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("SKU not found"));

                    double price = item.getPrice();

                    return new OrderItem(sku, item.getQuantity(), price * item.getQuantity());
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderRepository.save(order);

        return new OrderDTO(order);
    }

    private double calculateTotal(Cart cart) {
        // Calculate the total price of the items in the cart
        return cart.getCartItems().stream()
                .mapToDouble(cartItem -> cartItem.getInventory().getSellingPrice() * cartItem.getQuantity())
                .sum();
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(ProductDTO::new).toList();
    }
}
