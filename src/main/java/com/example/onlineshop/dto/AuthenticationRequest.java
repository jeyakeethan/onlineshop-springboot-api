package com.example.onlineshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
    private String userId;
    private String password;
}
