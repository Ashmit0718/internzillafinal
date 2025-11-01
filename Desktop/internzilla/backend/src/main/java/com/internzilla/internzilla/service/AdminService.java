package com.internzilla.internzilla.service;

import com.internzilla.internzilla.dto.AnalyticsDTO;
import com.internzilla.internzilla.dto.UserDTO;
import com.internzilla.internzilla.exception.ResourceNotFoundException;
import com.internzilla.internzilla.model.User;
import com.internzilla.internzilla.repository.InternshipRepository;
import com.internzilla.internzilla.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;

    public AdminService(UserRepository userRepository, InternshipRepository internshipRepository) {
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setFullName(userDTO.getFullName());
        user.setRole(User.Role.valueOf(userDTO.getRole().toUpperCase()));
        User updatedUser = userRepository.save(user);
        return new UserDTO(updatedUser);
    }

    public AnalyticsDTO getAnalytics() {
        AnalyticsDTO analytics = new AnalyticsDTO();
        analytics.setTotalUsers(userRepository.count());
        analytics.setTotalStudents(userRepository.countByRole(User.Role.STUDENT));
        analytics.setTotalRecruiters(userRepository.countByRole(User.Role.RECRUITER));
        analytics.setTotalInternships(internshipRepository.count());
        return analytics;
    }
}