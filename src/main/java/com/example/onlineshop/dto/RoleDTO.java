package com.example.onlineshop.dto;

import com.example.onlineshop.model.Role;
import lombok.Data;

@Data
public class RoleDTO {
    private Long id;         // Unique ID for the role
    private String name;     // Name of the role (e.g., "ADMIN", "BUYER")

    public RoleDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public RoleDTO(Role role) {
        this.id = role.getRoleId();
        this.name = role.getName();
    }
}
