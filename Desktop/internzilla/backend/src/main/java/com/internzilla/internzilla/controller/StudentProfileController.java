package com.internzilla.internzilla.controller;

import com.internzilla.internzilla.model.StudentProfile;
import com.internzilla.internzilla.model.User;
import com.internzilla.internzilla.repository.StudentProfileRepository;
import com.internzilla.internzilla.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentProfileController {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    public StudentProfileController(StudentProfileRepository studentProfileRepository, UserRepository userRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElse(null);
        if (user == null || user.getRole() != User.Role.STUDENT) {
            return ResponseEntity.status(403).body("Forbidden: Only students can access their profile.");
        }
        return studentProfileRepository.findByUserId(user.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/profile")
    public ResponseEntity<?> createOrUpdateProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody StudentProfile profile) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElse(null);
        if (user == null || user.getRole() != User.Role.STUDENT) {
            return ResponseEntity.status(403).body("Forbidden: Only students can update their profile.");
        }
        // Always associate the profile with the authenticated user
        profile.setUserId(user.getId());
        StudentProfile savedProfile = studentProfileRepository.findByUserId(user.getId())
            .map(existing -> {
                existing.setCgpa(profile.getCgpa());
                existing.setSkills(profile.getSkills());
                existing.setResumeUrl(profile.getResumeUrl());
                return studentProfileRepository.save(existing);
            })
            .orElseGet(() -> studentProfileRepository.save(profile));
        return ResponseEntity.ok(savedProfile);
    }
}
