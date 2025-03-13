package com.example.onlineshop.service;

import com.example.onlineshop.dto.InventoryDTO;
import com.example.onlineshop.dto.OrderDTO;
import com.example.onlineshop.dto.ProductDTO;
import com.example.onlineshop.dto.SKUDTO;
import com.example.onlineshop.model.Inventory;
import com.example.onlineshop.model.Order;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.SKU;
import com.example.onlineshop.repository.OrderRepository;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.repository.SKURepository;
import com.example.onlineshop.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SKURepository skuRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Add a new product along with SKUs (Inventories will be added separately)
    public void addProduct(ProductDTO productDTO) {
        // Create and save the product
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        productRepository.save(product);

        // Add SKUs for the product
        for (SKUDTO skuDTO : productDTO.getSkuDTOs()) {
            SKU sku = new SKU();
            sku.setSize(skuDTO.getSize());
            sku.setColor(skuDTO.getColor());
            sku.setSkuCode(skuDTO.getSkuCode());
            sku.setProduct(product); // Link SKU to the product
            skuRepository.save(sku);
        }
    }

    // Add inventory for a specific SKU (Multiple inventories can be added for a SKU)
    public void addInventory(Long skuId, InventoryDTO inventoryDTO) {
        // Find SKU
        SKU sku = skuRepository.findById(skuId)
                .orElseThrow(() -> new RuntimeException("SKU not found"));

        // Create and save inventory
        Inventory inventory = new Inventory();
        inventory.setSku(sku);
        inventory.setCostPrice(inventoryDTO.getCostPrice());
        inventory.setSellingPrice(inventoryDTO.getSellingPrice());
        inventory.setStockAvailable(inventoryDTO.getStockAvailable());

        inventoryRepository.save(inventory);
    }

    // Update stock for a specific SKU
    public void updateStock(Long inventoryId, int newStock) {
        // Find the inventory and its corresponding SKU
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new RuntimeException("Inventory not found for inventory Id"));
        SKU sku = skuRepository.findById(inventory.getSku().getSkuId()).orElseThrow(() -> new RuntimeException("SKU not found"));
        // Update the stock available in the inventory
        inventory.setStockAvailable(newStock);
        inventoryRepository.save(inventory);
    }

    // Update pricing for a specific SKU
    public void updateProductPricing(Long skuId, double newSellingPrice, double newCostPrice) {
        SKU sku = skuRepository.findById(skuId).orElseThrow(() -> new RuntimeException("SKU not found"));
        Inventory inventory = inventoryRepository.findBySku(sku).orElseThrow(() -> new RuntimeException("Inventory not found for SKU"));

        // Update pricing in the inventory
        inventory.setSellingPrice(newSellingPrice);
        inventory.setCostPrice(newCostPrice);
        inventoryRepository.save(inventory);
    }

    // View all orders (admin only) and convert Order to OrderDTO
    public List<OrderDTO> getAllOrders() {
        // Fetch all orders from the repository (database)
        List<Order> orders = orderRepository.findAll();

        // Convert List of Order models to List of OrderDTOs
        return orders.stream()
                .map(order -> new OrderDTO(order)) // Convert each Order entity to OrderDTO
                .collect(Collectors.toList());
    }
}
