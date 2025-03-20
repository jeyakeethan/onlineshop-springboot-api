package com.example.onlineshop.controller;

import com.example.onlineshop.dto.AuthenticationRequest;
import com.example.onlineshop.dto.AuthenticationResponse;

import com.example.onlineshop.security.JwtUtil;
import com.example.onlineshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Authenticate user and return JWT token for Buyer")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/authenticate")
    public AuthenticationResponse authenticateUser(@RequestBody AuthenticationRequest request) {
        return new AuthenticationResponse(userService.authenticateUser(request.getUserId(), request.getPassword()));
    }}

