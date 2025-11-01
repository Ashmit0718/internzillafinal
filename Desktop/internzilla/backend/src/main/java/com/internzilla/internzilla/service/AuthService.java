package com.internzilla.internzilla.service;

import com.internzilla.internzilla.dto.AuthRequest;
import com.internzilla.internzilla.dto.AuthResponse;
import com.internzilla.internzilla.dto.RegisterRequest;
import com.internzilla.internzilla.exception.ResourceNotFoundException;
import com.internzilla.internzilla.model.User;
import com.internzilla.internzilla.repository.UserRepository;
import com.internzilla.internzilla.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    public static final int MIN_PASSWORD_LENGTH = 8;

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                       UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest req) {
        if (req.getPassword() == null || req.getPassword().length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setFullName(req.getFullName());

        try {
            user.setRole(User.Role.valueOf(req.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error: Invalid role specified. Must be STUDENT or RECRUITER.");
        }

        userRepository.save(user);
    }

    public AuthResponse login(AuthRequest req) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(req.getEmail());
        final User user = userRepository.findByEmail(req.getEmail()).get(); // .get() is safe here because auth passed
        final String jwt = jwtUtil.generateToken(userDetails, user);
        return new AuthResponse(jwt, user.getRole().toString());
    }

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        // In a real application, you would email this token to the user.
        // For this project, we return it directly for simulation.
        return token;
    }

    public void resetPassword(String token, String newPassword) {
        if (newPassword == null || newPassword.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }

        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid reset token."));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null); // Invalidate the token after use
        userRepository.save(user);
    }
}
