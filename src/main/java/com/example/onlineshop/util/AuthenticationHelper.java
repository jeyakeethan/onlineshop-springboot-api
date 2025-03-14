package com.example.onlineshop.util;

import com.example.onlineshop.security.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    private final JwtUtil jwtUtil;

    public AuthenticationHelper(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Method to get the current authenticated user's ID from the JWT token.
     * @return the user ID if authenticated, otherwise throws an exception
     */
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is present and is of type UsernamePasswordAuthenticationToken
        if (authentication != null && authentication instanceof UsernamePasswordAuthenticationToken) {
            String token = (String) authentication.getCredentials();  // Extract the JWT token as a credential

            // Extract the user ID from the token claims using JwtUtil
            String userId = jwtUtil.extractClaim(token, claims -> claims.get("userId", String.class));

            return userId;
        }

        // If authentication is not available, throw an exception
        throw new RuntimeException("User not authenticated or invalid token");
    }
}
