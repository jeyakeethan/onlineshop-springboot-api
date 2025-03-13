package com.example.onlineshop.repository;

import com.example.onlineshop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    // Custom query methods (if needed)
}
