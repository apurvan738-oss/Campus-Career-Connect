package com.college.placement.controller;

import com.college.placement.dto.PasswordChangeDto;
import com.college.placement.dto.UserRegistrationDto;
import com.college.placement.service.StudentProfileService;
import com.college.placement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AuthController {

    private final UserService userService;
    private final StudentProfileService profileService;

    public AuthController(UserService userService, StudentProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "registered", required = false) String registered,
                            Model model,
                            Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            boolean isOfficer = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_PLACEMENT_OFFICER"));
            return isOfficer ? "redirect:/officer/dashboard" : "redirect:/student/dashboard";
        }

        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password. Please verify your credentials.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        if (registered != null) {
            model.addAttribute("successMessage", "Registration completed! Please log in with your credentials.");
        }

        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/student/dashboard";
        }

        if (!model.containsAttribute("registrationDto")) {
            model.addAttribute("registrationDto", new UserRegistrationDto());
        }
        model.addAttribute("branches", List.of("CSE", "ECE", "IT", "ME", "EE", "CIVIL", "CHEM"));
        return "auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("registrationDto") UserRegistrationDto dto,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("branches", List.of("CSE", "ECE", "IT", "ME", "EE", "CIVIL", "CHEM"));
            return "auth/register";
        }

        try {
            userService.registerStudent(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Account created successfully! You can now log in.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("branches", List.of("CSE", "ECE", "IT", "ME", "EE", "CIVIL", "CHEM"));
            return "auth/register";
        }
    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model model) {
        if (!model.containsAttribute("passwordDto")) {
            model.addAttribute("passwordDto", new PasswordChangeDto());
        }
        return "auth/change-password";
    }

    @PostMapping("/change-password")
    public String handleChangePassword(@Valid @ModelAttribute("passwordDto") PasswordChangeDto dto,
                                       BindingResult bindingResult,
                                       Authentication authentication,
                                       RedirectAttributes redirectAttributes) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "New password and confirmation do not match");
        }

        if (bindingResult.hasErrors()) {
            return "auth/change-password";
        }

        try {
            userService.changePassword(authentication.getName(), dto);
            redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully!");
            boolean isOfficer = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_PLACEMENT_OFFICER"));
            return isOfficer ? "redirect:/officer/dashboard" : "redirect:/student/dashboard";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/change-password";
        }
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
}
