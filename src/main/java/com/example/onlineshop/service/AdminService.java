package com.example.onlineshop.service;

import com.example.onlineshop.dto.*;
import com.example.onlineshop.model.*;
import com.example.onlineshop.repository.*;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductService productService;

    // Add a new product along with SKUs
    public void addProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        productRepository.save(product);

        for (SKUDTO skuDTO : productDTO.getSkuDTOs()) {
            SKU sku = new SKU();
            sku.setSize(skuDTO.getSize());
            sku.setColor(skuDTO.getColor());
            sku.setSkuCode(skuDTO.getSkuCode());
            sku.setProduct(product);
            skuRepository.save(sku);
        }
    }

    public void addInventory(Long skuId, InventoryDTO inventoryDTO) {
        SKU sku = skuRepository.findById(skuId)
                .orElseThrow(() -> new RuntimeException("SKU not found"));

        Inventory inventory = new Inventory();
        inventory.setSku(sku);
        inventory.setCostPrice(inventoryDTO.getCostPrice());
        inventory.setSellingPrice(inventoryDTO.getSellingPrice());
        inventory.setStockAvailable(inventoryDTO.getStockAvailable());
        inventoryRepository.save(inventory);
    }

    public void updateStock(Long inventoryId, int newStock) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        inventory.setStockAvailable(newStock);
        inventoryRepository.save(inventory);
    }

    public void updateProductPricing(Long inventoryId, double newSellingPrice, double newCostPrice) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new RuntimeException("Inventory not found"));
        inventory.setSellingPrice(newSellingPrice);
        inventory.setCostPrice(newCostPrice);
        inventoryRepository.save(inventory);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderDTO::new).collect(Collectors.toList());
    }

    public void createUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDob(userDTO.getDob());
        userRepository.save(user);
    }

    public void updateUser(String userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDob(userDTO.getDob());
        userRepository.save(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public UserDTO getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public void createRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        roleRepository.save(role);
    }

    public void updateRole(Long roleId, RoleDTO roleDTO) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(roleDTO.getName());
        roleRepository.save(role);
    }

    public void deleteRole(Long roleId) {
        roleRepository.deleteById(roleId);
    }

    public RoleDTO getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        return new RoleDTO(role);
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream().map(RoleDTO::new).collect(Collectors.toList());
    }

    public void assignRoleToUser(String userId, Long roleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);
        userRepository.save(user);
    }

    public void removeRoleFromUser(String userId, Long roleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().remove(role);
        userRepository.save(user);
    }

    public void approveProduct(ProductDTO productDTO) {
        Product product = productRepository.findById(productDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setApproved(true);
        productRepository.save(product);
    }

    public void rejectProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public void deleteProduct(Long productId) {
        productService.deleteProduct(productId);
    }

    public void updateProduct(Long productId, ProductDTO productDTO) {
        productService.updateProduct(productId, productDTO);
    }
}
