package com.example.onlineshop.service;

import com.example.onlineshop.dto.*;
import com.example.onlineshop.model.*;
import com.example.onlineshop.repository.*;
import com.example.onlineshop.util.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CategoryRepository categoryRepository;  // New repository to handle subcategories

    @Autowired
    private AuthenticationHelper authenticationHelper;

    // Product Browsing with Category and Subcategory Hierarchy
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return new ProductDTO(product);
    }

    public List<ProductDTO> searchProducts(String query) {
        return productRepository.findByNameContaining(query).stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductDetails(Long productId) {
        return getProductById(productId);
    }

    public List<ProductDTO> getProductsByCategory(String category) {
        // Find all subcategories under the given category
        List<Category> subcategories = categoryRepository.findByCategory(category);

        // Collect product IDs associated with those subcategories
        List<String> subcategoryNames = subcategories.stream()
                .map(Category::getSubcategory)
                .collect(Collectors.toList());

        // Find all products associated with the found subcategories
        return productRepository.findByCategoryIn(subcategoryNames).stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }


    public List<ProductDTO> getProductsBySubcategory(String subcategory) {
        return productRepository.findBySubcategory(subcategory).stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsOnSale() {
        return productRepository.findByOnSaleTrue().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    // Cart Operations (Updated to handle Inventory and SKU correctly)
    public CartDTO getCart(String userId) {
        Cart cart = cartRepository.findByUserEmail(userId);
        return new CartDTO(cart);
    }

    public void addProductToCart(Long inventoryId, String userId, Integer quantity) {
        Cart cart = cartRepository.findByUserEmail(userId);
        if (cart == null) {
            throw new RuntimeException("Cart not found for the user");
        }

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (inventory.getStockAvailable() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        CartItem cartItem = new CartItem(cart, inventory, quantity);
        cart.getCartItems().add(cartItem);
        cartRepository.save(cart);
    }

    public void removeProductFromCart(Long inventoryId) {
        String userId = authenticationHelper.getCurrentUserId();
        Cart cart = cartRepository.findByUserEmail(userId);
        cart.getCartItems().removeIf(item -> item.getInventory().getInventoryId().equals(inventoryId));
        cartRepository.save(cart);
    }

    public void updateCartProductQuantity(Long inventoryId, String userId, Integer quantity) {
        Cart cart = cartRepository.findByUserEmail(userId);
        cart.getCartItems().forEach(item -> {
            if (item.getInventory().getInventoryId().equals(inventoryId)) {
                item.setQuantity(quantity);
            }
        });
        cartRepository.save(cart);
    }

    public void clearCart(String userId) {
        Cart cart = cartRepository.findByUserEmail(userId);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    // Checkout process
    public OrderDTO checkout() {
        // Step 1: Retrieve the user's cart
        CartDTO cart = cartRepository.getCart(authenticationHelper.getCurrentUserId());
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Please add items to the cart before checking out.");
        }

        // Step 2: Place the order based on the cart contents
        List<Long> inventoryIds = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        for (CartItemDTO item : cart.getItems()) {
            inventoryIds.add(item.getInventoryId());
            quantities.add(item.getQuantity());
        }

        placeOrder(inventoryIds, quantities);

        // Step 3: Clear the user's cart after placing the order
        cartRepository.clearCart(authenticationHelper.getCurrentUserId());

        // Step 4: Return the order details as a DTO (could include more details as per your design)
        OrderDTO orderDTO = new OrderDTO();
        // Populate orderDTO with order details (e.g., order ID, status, etc.)
        // For now, return a basic message (you can enhance this further)
        return orderDTO;
    }

    // Place order logic
    public void placeOrder(List<Long> inventoryIds, List<Integer> quantities) {
        // Create and save order logic based on inventoryId
        if (inventoryIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Inventory IDs and quantities size must match");
        }

        // Retrieve inventories from database
        List<Inventory> inventories = inventoryRepository.findAllById(inventoryIds);
        if (inventories.size() != inventoryIds.size()) {
            throw new RuntimeException("One or more inventories not found");
        }

        // Retrieve user details
        String userId = authenticationHelper.getCurrentUserId();
        User user = userRepository.findByEmail(userId).orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + userId)
        );

        // Create a new order
        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus("NEW");
        order.setUser(user);

        // Set order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < inventoryIds.size(); i++) {
            Inventory inventory = inventories.get(i);
            Integer quantity = quantities.get(i);

            // Ensure enough stock exists
            if (inventory.getStockAvailable() < quantity) {
                throw new RuntimeException("Insufficient stock for product: " + inventory.getSku().getSkuCode());
            }

            // Create and add order items
            OrderItem orderItem = new OrderItem(inventory.getSku(), quantity, inventory.getSellingPrice());
            orderItems.add(orderItem);

            // Decrease stock in inventory
            inventory.setStockAvailable(inventory.getStockAvailable() - quantity);
            inventoryRepository.save(inventory);
        }

        // Set order items to the order
        order.setOrderItems(orderItems);

        List<Address> addresses = addressRepository.findByUserId(userId);
        // Set shipping address (you can extend the logic to handle the user's shipping address)
        // For simplicity, let's assume the shipping address is set via the user object
        order.setShippingAddress(addresses.get(0));

        // Save the order to the database
        orderRepository.save(order);
    }

    public OrderDTO getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return new OrderDTO(order);
    }

    public List<OrderDTO> getUserOrders(String userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus("Cancelled");
        orderRepository.save(order);
    }

    public void returnProduct(Long inventoryId, Long orderId) {
        // Implement return logic based on inventoryId
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Optional<CartItem> item = order.getOrderItems().stream()
                .filter(cartItem -> cartItem.getInventory().getInventoryId().equals(inventoryId))
                .findFirst();

        if (item.isPresent()) {
            // Logic to process return (update order status or create a return record)
            item.get().setReturned(true);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Product not found in the order");
        }
    }

    // WishList Management (No change)
    public void addProductToWishList(String userId, Long inventoryId) {
        WishList wishlist = getWishList();
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new RuntimeException("Inventory not found"));
        wishlist.getInventories().add(inventory);
    }

    public void removeProductFromWishList(Inventory inventory) {
        WishList wishlist = getWishList();
        wishlist.getInventories().remove(inventory);
    }

    public boolean isProductInWishList(Long inventoryId) {
        WishList wishlist = getWishList();
        return wishlist.getInventories().stream().anyMatch(inventory -> inventory.getInventoryId().equals(inventoryId));
    }

    private WishList getWishList() {
        String userId = authenticationHelper.getCurrentUserId();
        // Implement logic to retrieve wishlist by userId
        // This could be from a repository or cache
        return new WishList();
    }

    // Review and Rating Management (No change)
    public void leaveReview(String userId, Long productId, String reviewText, Integer rating) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = new Review(userId, product, reviewText, rating);
        product.getReviews().add(review);
        productRepository.save(product);
    }
}
