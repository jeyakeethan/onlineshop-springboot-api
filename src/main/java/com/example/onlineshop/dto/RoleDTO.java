package com.example.onlineshop.dto;

import lombok.Data;

@Data
public class RoleDTO {
    private Long id;         // Unique ID for the role
    private String name;     // Name of the role (e.g., "ADMIN", "BUYER")

    // Constructor for RoleDTO
    public RoleDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Default constructor
    public RoleDTO() { }
}
