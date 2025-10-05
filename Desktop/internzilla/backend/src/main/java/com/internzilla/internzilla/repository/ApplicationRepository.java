package com.internzilla.internzilla.repository;

import com.internzilla.internzilla.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    // Find all applications for a specific internship
    List<Application> findByInternshipId(Long internshipId);
}