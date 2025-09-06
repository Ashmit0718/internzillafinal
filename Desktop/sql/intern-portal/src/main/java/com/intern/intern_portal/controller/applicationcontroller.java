//package directories dakhavta java compiler la ki class kutchya file madhe stored aahe 
package com.intern.intern_portal.controller;


import com.intern.intern_portal.model.application;
import com.intern.intern_portal.repository.applicationrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
//import java.util.List;

// Cross origin resource sharing
@CrossOrigin(origins = "http://localhost:5500")
@RestController     
// restapicontroller mahnun mark karta yaa class la
@RequestMapping("/api/applications")
public class applicationcontroller 
{
    @Autowired
    private applicationrepository applicationrepository;
    // spring framework cha object use kartoy mahnun @Autowired use kartoy
    @PostMapping
    public application createApplication(@RequestBody application app) {
        return applicationrepository.save(app);
    }
    // post request use kartoy karan user zoo data deil toh save karyacha aahe database madhe
    //requestbody use kartoy karan user cha data json format madhe yeil toh object madhe convert karnyasathi
    
    @GetMapping("/ping")
    public String ping() 
    {
        return "Application controller is alive!";
    }

}
// eg: http://localhost:8080/api/applications/ping





