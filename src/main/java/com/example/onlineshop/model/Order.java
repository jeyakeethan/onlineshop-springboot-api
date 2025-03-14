package com.example.onlineshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private LocalDate orderDate;
    private String orderStatus;
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "email")
    private User user;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

}
