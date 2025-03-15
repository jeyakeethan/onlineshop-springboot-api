package com.example.onlineshop.service;

import com.example.onlineshop.dto.UserDTO;
import com.example.onlineshop.model.Role;
import com.example.onlineshop.model.User;
import com.example.onlineshop.repository.RoleRepository;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.security.JwtUtil;
import com.example.onlineshop.util.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationHelper authenticationHelper;

    // Register new user and return JWT token
    public String registerUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDob(userDTO.getDob());
        user.setLastLoginTime(LocalDate.now()); // Default to current date

        // Encode the password before saving it
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Convert role names (List<String>) to Role entities
        List<Role> userRoles = userDTO.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toList());

        user.setRoles(userRoles);

        userRepository.save(user);
        return jwtUtil.generateToken(user);
    }

    // Authenticate user and return JWT token
    public String authenticateUser(String userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user);
    }

    public void updateUser(String userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDob(userDTO.getDob());

        userRepository.save(user);
    }

    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

    public UserDTO getAuthenticatedUser() {
        String userId = authenticationHelper.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(user);
    }

    public UserDTO getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(user);
    }

    public User getUserDetailById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }
}
