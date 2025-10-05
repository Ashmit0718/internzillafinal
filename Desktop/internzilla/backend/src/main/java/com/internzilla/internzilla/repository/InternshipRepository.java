package com.internzilla.internzilla.repository;

import com.internzilla.internzilla.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InternshipRepository extends JpaRepository<Internship, Long> {
    // This new method finds all internships posted by a specific recruiter's ID
    List<Internship> findByRecruiterId(Long recruiterId);
}