package com.internzilla.internzilla.service;

import com.internzilla.internzilla.exception.ResourceNotFoundException;
import com.internzilla.internzilla.model.StudentProfile;
import com.internzilla.internzilla.model.User;
import com.internzilla.internzilla.repository.StudentProfileRepository;
import com.internzilla.internzilla.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    public StudentProfileService(StudentProfileRepository studentProfileRepository, UserRepository userRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.userRepository = userRepository;
    }

    private User getUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));
    }

    public Optional<StudentProfile> getProfile(String username) {
        User user = getUserByUsername(username);
        if (user.getRole() != User.Role.STUDENT) {
            throw new AccessDeniedException("Forbidden: Only students can access their profile.");
        }
        return studentProfileRepository.findByUserId(user.getId());
    }

    public StudentProfile createOrUpdateProfile(String username, StudentProfile profile) {
        User user = getUserByUsername(username);
        if (user.getRole() != User.Role.STUDENT) {
            throw new AccessDeniedException("Forbidden: Only students can update their profile.");
        }

        profile.setUserId(user.getId());

        return studentProfileRepository.findByUserId(user.getId())
            .map(existing -> {
                existing.setCgpa(profile.getCgpa());
                existing.setSkills(profile.getSkills());
                existing.setResumeUrl(profile.getResumeUrl());
                return studentProfileRepository.save(existing);
            })
            .orElseGet(() -> studentProfileRepository.save(profile));
    }
}