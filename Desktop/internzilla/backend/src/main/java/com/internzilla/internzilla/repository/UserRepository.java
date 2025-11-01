package com.internzilla.internzilla.repository;

import com.internzilla.internzilla.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    // This new method will count users based on their role
    long countByRole(User.Role role);
}