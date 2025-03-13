package com.example.onlineshop.repository;

import com.example.onlineshop.model.SeasonalDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SeasonalDiscountRepository extends JpaRepository<SeasonalDiscount, Long> {
}
