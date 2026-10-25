package com.college.placement.service;

import com.college.placement.dto.StudentProfileDto;
import com.college.placement.entity.StudentProfile;
import com.college.placement.entity.User;
import com.college.placement.exception.ResourceNotFoundException;
import com.college.placement.repository.StudentProfileRepository;
import com.college.placement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class StudentProfileService {

    private static final long MAX_RESUME_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("pdf", "doc", "docx");
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );

    private final StudentProfileRepository profileRepository;
    private final UserRepository userRepository;

    public StudentProfileService(StudentProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public StudentProfile getProfileByEmail(String email) {
        return profileRepository.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for email: " + email));
    }

    @Transactional(readOnly = true)
    public StudentProfile getProfileById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found with ID: " + id));
    }

    @Transactional
    public StudentProfile updateProfile(String email, StudentProfileDto dto) {
        StudentProfile profile = getProfileByEmail(email);

        // Check if new roll number is taken by another student
        String trimmedRoll = dto.getRollNumber().trim().toUpperCase();
        profileRepository.findByRollNumberIgnoreCase(trimmedRoll)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(profile.getId())) {
                        throw new IllegalArgumentException("Roll number " + trimmedRoll + " is already registered.");
                    }
                });

        profile.setFullName(dto.getFullName().trim());
        profile.setRollNumber(trimmedRoll);
        profile.setPhone(dto.getPhone().trim());
        profile.setDepartment(dto.getDepartment());
        profile.setBranch(dto.getBranch().trim().toUpperCase());
        profile.setGraduationYear(dto.getGraduationYear());
        profile.setCgpa(dto.getCgpa());
        profile.setTenthPercentage(dto.getTenthPercentage());
        profile.setTwelfthPercentage(dto.getTwelfthPercentage());
        profile.setSkills(dto.getSkills() != null ? dto.getSkills().trim() : null);
        profile.setAddress(dto.getAddress());
        profile.setGender(dto.getGender());
        profile.setDateOfBirth(dto.getDateOfBirth());

        profile.checkCompleteness();
        return profileRepository.save(profile);
    }

    @Transactional
    public void uploadResume(String email, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select a file to upload.");
        }

        if (file.getSize() > MAX_RESUME_SIZE) {
            throw new IllegalArgumentException("File size exceeds 5MB limit. Please upload a smaller file.");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex + 1).toLowerCase();
        }

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Invalid file type. Only PDF and Word documents (.pdf, .doc, .docx) are allowed.");
        }

        String contentType = file.getContentType();
        if (contentType != null && !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            // Also allow octet-stream if extension matches PDF
            if (!("application/octet-stream".equals(contentType) && "pdf".equals(extension))) {
                throw new IllegalArgumentException("Invalid file content type: " + contentType);
            }
        }

        StudentProfile profile = getProfileByEmail(email);
        profile.setResumeFileName(originalFilename);
        profile.setResumeContentType(contentType != null ? contentType : "application/pdf");
        profile.setResumeData(file.getBytes());
        profile.checkCompleteness();
        profileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public List<StudentProfile> filterStudents(String keyword, String branch, Integer gradYear, Double minCgpa) {
        return profileRepository.filterStudents(
                (keyword != null && !keyword.isBlank()) ? keyword.trim() : null,
                (branch != null && !branch.isBlank()) ? branch.trim() : null,
                gradYear,
                minCgpa
        );
    }

    @Transactional(readOnly = true)
    public List<String> getAllBranches() {
        return profileRepository.findDistinctBranches();
    }

    @Transactional(readOnly = true)
    public List<Integer> getAllGraduationYears() {
        return profileRepository.findDistinctGraduationYears();
    }
}
