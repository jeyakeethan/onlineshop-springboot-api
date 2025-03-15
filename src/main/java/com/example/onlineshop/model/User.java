package com.example.onlineshop.model;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email; // Primary Key

    private String firstName;
    private String lastName;
    private String password;
    private String dob;
    private LocalDate lastLoginTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_email", referencedColumnName = "email"), // Foreign key to User
            inverseJoinColumns = @JoinColumn(name = "role_id") // Foreign key to Role
    )
    private List<Role> roles = new ArrayList<>();

    // Implementing UserDetails methods
    @Override
    public String getUsername() {
        return this.email; // Can use email as username
    }

    @Override
    public String getPassword() {
        return this.password; // Password for authentication
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())) // Assuming 'Role' has 'getName()'
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can change this based on your business logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can change this based on your business logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can change this based on your business logic
    }

    @Override
    public boolean isEnabled() {
        return true; // You can change this based on your business logic
    }

    // Optional: Custom method
    public String getUserId() {
        return this.email;
    }
}
