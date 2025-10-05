package com.internzilla.internzilla.controller;

import com.internzilla.internzilla.dto.AnalyticsDTO;
import com.internzilla.internzilla.dto.UserDTO;
import com.internzilla.internzilla.model.User;
import com.internzilla.internzilla.repository.InternshipRepository;
import com.internzilla.internzilla.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;

    public AdminController(UserRepository userRepository, InternshipRepository internshipRepository) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream().map(UserDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return userRepository.findById(id).map(user -> {
            user.setFullName(userDTO.getFullName());
            user.setRole(User.Role.valueOf(userDTO.getRole().toUpperCase()));
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // This is the new endpoint for analytics
    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsDTO> getAnalytics() {
        AnalyticsDTO analytics = new AnalyticsDTO();
        analytics.setTotalUsers(userRepository.count());
        analytics.setTotalStudents(userRepository.countByRole(User.Role.STUDENT));
        analytics.setTotalRecruiters(userRepository.countByRole(User.Role.RECRUITER));
        analytics.setTotalInternships(internshipRepository.count());
        return ResponseEntity.ok(analytics);
    }
}