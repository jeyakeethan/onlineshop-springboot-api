package com.example.onlineshop.repository;

import com.example.onlineshop.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByNameContaining(String keyword);

//    Optional<Product> findByOnSaleTrue();

    List<Product> findByCategory_Subcategory(String subcategory);

    List<Product> findByCategory_SubcategoryAndNameContaining(String subcategory, String name);

    // Use @EntityGraph to eagerly load skus and inventories when fetching a product
    @EntityGraph(attributePaths = {"skus", "skus.inventories"})
    Optional<Product> findById(Long productId);

    @EntityGraph(attributePaths = {"skus", "skus.inventories"})
    List<Product> findAll();

    List<Product> findByCategoryIn(List<String> subcategoryNames);

}
