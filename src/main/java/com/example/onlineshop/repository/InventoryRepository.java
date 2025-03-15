package com.example.onlineshop.repository;

import com.example.onlineshop.model.Inventory;
import com.example.onlineshop.model.SKU;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findBySku(SKU sku);
}
