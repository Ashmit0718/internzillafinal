package com.internzilla.internzilla.controller;

import com.internzilla.internzilla.dto.AuthRequest;
import com.internzilla.internzilla.dto.AuthResponse;
import com.internzilla.internzilla.dto.RegisterRequest;
import com.internzilla.internzilla.model.User;
import com.internzilla.internzilla.repository.UserRepository;
import com.internzilla.internzilla.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager, UserDetailsService userDetailsService, UserRepository userRepo, JwtUtil jwtUtil, PasswordEncoder encoder) {
        this.authenticationManager = authManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepo;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = encoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        System.out.println("\n[DEBUG] --- /register endpoint hit ---");
        System.out.println("[DEBUG] Received registration request for email: " + req.getEmail());

        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            System.out.println("[DEBUG] ERROR: Email is already in use.");
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }
        System.out.println("[DEBUG] Email is available. Creating new user object.");

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setFullName(req.getFullName());

        try {
            System.out.println("[DEBUG] Attempting to set role from string: '" + req.getRole() + "'");
            user.setRole(User.Role.valueOf(req.getRole().toUpperCase()));
            System.out.println("[DEBUG] Successfully set role to: " + user.getRole());
        } catch (Exception e) {
            System.out.println("[DEBUG] !!! CRITICAL ERROR setting role !!!");
            e.printStackTrace(); // This will print the exact error to the console
            return ResponseEntity.badRequest().body("Error: Invalid role specified. Must be STUDENT or RECRUITER.");
        }

        try {
            System.out.println("[DEBUG] Attempting to save user to database...");
            userRepository.save(user);
            System.out.println("[DEBUG] User saved successfully!");
        } catch (Exception e) {
            System.out.println("[DEBUG] !!! CRITICAL ERROR saving user to database !!!");
            e.printStackTrace(); // This will print the exact error to the console
            return ResponseEntity.status(500).body("Error: Could not save user to the database.");
        }
        
        System.out.println("[DEBUG] --- Registration process successful ---");
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(req.getEmail());
        final User user = userRepository.findByEmail(req.getEmail()).get();
        final String jwt = jwtUtil.generateToken(userDetails, user);
        return ResponseEntity.ok(new AuthResponse(jwt, user.getRole().toString()));
    }
}

