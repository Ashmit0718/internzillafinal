package com.internzilla.internzilla.controller;

import com.internzilla.internzilla.model.StudentProfile;
import com.internzilla.internzilla.service.StudentProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    public StudentProfileController(StudentProfileService studentProfileService) {
        this.studentProfileService = studentProfileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return studentProfileService.getProfile(userDetails.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/profile")
    public ResponseEntity<?> createOrUpdateProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody StudentProfile profile) {
        StudentProfile savedProfile = studentProfileService.createOrUpdateProfile(userDetails.getUsername(), profile);
        return ResponseEntity.ok(savedProfile);
    }
}
