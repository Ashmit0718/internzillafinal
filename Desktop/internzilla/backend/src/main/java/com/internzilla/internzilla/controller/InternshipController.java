package com.internzilla.internzilla.controller;

import com.internzilla.internzilla.model.Application;
import com.internzilla.internzilla.model.Internship;
import com.internzilla.internzilla.model.User;
import com.internzilla.internzilla.model.StudentProfile;
import com.internzilla.internzilla.repository.ApplicationRepository;
import com.internzilla.internzilla.repository.InternshipRepository;
import com.internzilla.internzilla.repository.UserRepository;
import com.internzilla.internzilla.repository.StudentProfileRepository;
import com.internzilla.internzilla.dto.ApplicantDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internships")
public class InternshipController {

    private final InternshipRepository internshipRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final StudentProfileRepository studentProfileRepository;

    public InternshipController(
            InternshipRepository internshipRepository,
            UserRepository userRepository,
            ApplicationRepository applicationRepository,
            StudentProfileRepository studentProfileRepository
    ) {
        this.internshipRepository = internshipRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.studentProfileRepository = studentProfileRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Internship>> getAllInternships() {
        return ResponseEntity.ok(internshipRepository.findAll());
    }

    @PostMapping("/post")
    public ResponseEntity<?> postInternship(@RequestBody Internship internship, @AuthenticationPrincipal UserDetails userDetails) {
        User recruiter = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: Recruiter not found."));

        internship.setRecruiterId(recruiter.getId());
        Internship savedInternship = internshipRepository.save(internship);

        return ResponseEntity.ok(savedInternship);
    }

    // This is the new endpoint for a recruiter to get only their own postings
    @GetMapping("/my-postings")
    public ResponseEntity<List<Internship>> getMyPostings(@AuthenticationPrincipal UserDetails userDetails) {
        User recruiter = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: Recruiter not found."));

        List<Internship> myInternships = internshipRepository.findByRecruiterId(recruiter.getId());
        return ResponseEntity.ok(myInternships);
    }

    // --- ADD THIS ENDPOINT ---
    @PostMapping("/{id}/apply")
    public ResponseEntity<?> applyForInternship(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User student = userRepository.findByEmail(userDetails.getUsername())
                .orElse(null);
        if (student == null || student.getRole() != User.Role.STUDENT) {
            return ResponseEntity.status(403).body("Forbidden: Only students can apply.");
        }
        if (!internshipRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Internship not found.");
        }
        // Prevent duplicate applications
        boolean alreadyApplied = applicationRepository.findAll().stream()
                .anyMatch(app -> app.getInternshipId().equals(id) && app.getStudentId().equals(student.getId()));
        if (alreadyApplied) {
            return ResponseEntity.badRequest().body("You have already applied for this internship.");
        }
        Application application = new Application();
        application.setInternshipId(id);
        application.setStudentId(student.getId());
        applicationRepository.save(application);
        return ResponseEntity.ok("Application submitted successfully.");
    }

    // --- ADD THIS ENDPOINT ---
    @GetMapping("/{id}/applicants")
    public ResponseEntity<?> getApplicants(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User recruiter = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (recruiter == null || recruiter.getRole() != User.Role.RECRUITER) {
            return ResponseEntity.status(403).body("Forbidden: Only recruiters can view applicants.");
        }
        if (!internshipRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Internship not found.");
        }
        List<Application> applications = applicationRepository.findByInternshipId(id);
        List<ApplicantDTO> applicants = applications.stream().map(app -> {
            User student = userRepository.findById(app.getStudentId()).orElse(null);
            StudentProfile profile = studentProfileRepository.findByUserId(app.getStudentId()).orElse(null);
            return new ApplicantDTO(student, profile);
        }).toList();
        return ResponseEntity.ok(applicants);
    }
}