package com.example.onlineshop.repository;

import com.example.onlineshop.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    Optional<WishList> findByUserEmail(String userEmail);
}
