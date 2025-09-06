package com.intern.intern_portal.repository;

import com.intern.intern_portal.model.internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface internshiprepository extends JpaRepository<internship, Integer> {
}