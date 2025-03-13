package com.example.onlineshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email; // Primary Key

    private String firstName;
    private String lastName;
    private String password;
    private String dob;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_email", referencedColumnName = "email"), // Foreign key to User
            inverseJoinColumns = @JoinColumn(name = "role_id") // Foreign key to Role
    )
    private List<Role> roles = new ArrayList<>();
}
