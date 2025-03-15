package com.example.onlineshop.repository;

import com.example.onlineshop.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByNameContaining(String keyword);

    Optional<Product> findByOnSaleTrue();

    Optional<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findByCategory(String subcategory);

    List<Product> findByCategoryAndNameContaining(String category, String keyword);

    // Use @EntityGraph to eagerly load skus and inventories when fetching a product
    @EntityGraph(attributePaths = {"skus", "skus.inventories"})
    Optional<Product> findByIdComplete(Long productId);

    @EntityGraph(attributePaths = {"skus", "skus.inventories"})
    List<Product> findAllProductsComplete();

    List<Product> findBySubcategory(String subcategory);

    List<Product> findBySubcategoryIn(List<String> subcategoryNames);
}
