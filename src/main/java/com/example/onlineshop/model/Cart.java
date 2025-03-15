package com.example.onlineshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "email")
    private User user;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems;
}
