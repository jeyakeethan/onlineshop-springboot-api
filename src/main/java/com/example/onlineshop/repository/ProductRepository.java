package com.example.onlineshop.repository;

import com.example.onlineshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_Subcategory(String subcategory);

    Optional<Product> findByNameContaining(String keyword);

    Optional<Product> findBySubcategory(String subcategory);

    Optional<Product> findByOnSaleTrue();

    Optional<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    Optional<Product> findByCategoryIn(List<String> subcategoryNames);
}
