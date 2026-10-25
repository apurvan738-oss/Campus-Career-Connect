package com.college.placement.service;

import com.college.placement.dto.PasswordChangeDto;
import com.college.placement.dto.UserRegistrationDto;
import com.college.placement.entity.Role;
import com.college.placement.entity.StudentProfile;
import com.college.placement.entity.User;
import com.college.placement.exception.DuplicateApplicationException;
import com.college.placement.exception.ResourceNotFoundException;
import com.college.placement.exception.UnauthorizedAccessException;
import com.college.placement.repository.StudentProfileRepository;
import com.college.placement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       StudentProfileRepository studentProfileRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerStudent(UserRegistrationDto dto) {
        if (userRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("An account with email " + dto.getEmail() + " already exists.");
        }
        if (studentProfileRepository.existsByRollNumberIgnoreCase(dto.getRollNumber())) {
            throw new IllegalArgumentException("A student with roll number " + dto.getRollNumber() + " already exists.");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and Confirm Password do not match.");
        }

        // Always force STUDENT role for public registrations
        User user = new User(dto.getEmail().toLowerCase().trim(), passwordEncoder.encode(dto.getPassword()), Role.STUDENT);
        user = userRepository.save(user);

        StudentProfile profile = new StudentProfile();
        profile.setUser(user);
        profile.setFullName(dto.getFullName().trim());
        profile.setRollNumber(dto.getRollNumber().trim().toUpperCase());
        profile.setBranch(dto.getBranch().trim().toUpperCase());
        profile.setGraduationYear(dto.getGraduationYear());
        profile.setCgpa(dto.getCgpa());
        profile.setPhone(dto.getPhone().trim());
        profile.setDepartment("School of Technology & Engineering");
        profile.checkCompleteness();

        studentProfileRepository.save(profile);
        return user;
    }

    @Transactional
    public void changePassword(String email, PasswordChangeDto dto) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirmation do not match.");
        }
        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from current password.");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void toggleUserActiveStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
