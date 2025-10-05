package com.internzilla.internzilla;

import com.internzilla.internzilla.model.User;
import com.internzilla.internzilla.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class InternzillaApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternzillaApplication.class, args);
    }

    // This code will run once on startup to ensure the admin user exists with a correct password.
    @Bean
    public CommandLineRunner createAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@internzilla.com";
            
            // Find if an admin user already exists
            userRepository.findByEmail(adminEmail).ifPresent(user -> {
                // If the user exists, we only update the password to ensure it's correct.
                // This prevents deleting and recreating the user, preserving its ID.
                System.out.println(">>> Admin user found. Resetting password to ensure correctness. <<<");
                user.setPassword(passwordEncoder.encode("adminpassword"));
                userRepository.save(user);
            });

            // If the admin user does not exist at all, create it.
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setFullName("Admin");
                admin.setRole(User.Role.ADMIN);
                admin.setPassword(passwordEncoder.encode("adminpassword"));
                userRepository.save(admin);
                System.out.println(">>> New admin user created successfully! <<<");
            }
        };
    }
}

