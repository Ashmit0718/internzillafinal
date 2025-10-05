package com.internzilla.internzilla.service;

import com.internzilla.internzilla.model.Internship;
import com.internzilla.internzilla.repository.InternshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InternshipService {

    @Autowired
    private InternshipRepository internshipRepository;

    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }

    public Optional<Internship> getInternshipById(Long id) {
        return internshipRepository.findById(id);
    }

    public Internship createInternship(Internship internship) {
        return internshipRepository.save(internship);
    }

    public Internship updateInternship(Long id, Internship updatedInternship) {
        return internshipRepository.findById(id)
                .map(internship -> {
                    internship.setTitle(updatedInternship.getTitle());
                    internship.setDescription(updatedInternship.getDescription());
                    internship.setStipend(updatedInternship.getStipend());
                    internship.setInternshipType(updatedInternship.getInternshipType());
                    internship.setEligibilityCriteria(updatedInternship.getEligibilityCriteria());
                    // We do not update recruiter here to keep data safe
                    return internshipRepository.save(internship);
                })
                .orElseThrow(() -> new RuntimeException("Internship not found"));
    }

    public void deleteInternship(Long id) {
        internshipRepository.deleteById(id);
    }
}
