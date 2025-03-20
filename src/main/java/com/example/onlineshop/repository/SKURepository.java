package com.example.onlineshop.repository;

import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.SKU;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SKURepository extends JpaRepository<SKU, Long> {

    void deleteByProduct_ProductId(Long productId);

    List<SKU> findByProduct(Product product);
}
