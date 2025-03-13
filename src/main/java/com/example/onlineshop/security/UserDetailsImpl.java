package com.example.onlineshop.security;

import com.example.onlineshop.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert roles to GrantedAuthority
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Return the user's password
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Use email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Can be customized to check account expiration
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Can be customized to check if the account is locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Can be customized to check if credentials are expired
    }

    @Override
    public boolean isEnabled() {
        return true; // You can customize to check if the user is enabled or not
    }

    // Additional methods to expose user details
    public String getUserId() {
        return user.getEmail(); // Expose userId if necessary
    }

    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName(); // Combine first and last name
    }
}
