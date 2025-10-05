package com.internzilla.internzilla.repository;

import com.internzilla.internzilla.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    // This allows us to find a profile using the user's ID
    Optional<StudentProfile> findByUserId(Long userId);
}