package com.intern.intern_portal.controller;

import com.intern.intern_portal.model.internship;
import com.intern.intern_portal.repository.internshiprepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/internships")
public class internshipcontroller 
{

    @Autowired
    private internshiprepository internshiprepository;

    @GetMapping
    public List<internship> getAllInternships() 
    {
        return internshiprepository.findAll();
    }
}






// eg: http://localhost:8080/api/internships