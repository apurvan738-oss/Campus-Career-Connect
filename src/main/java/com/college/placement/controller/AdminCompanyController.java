package com.college.placement.controller;

import com.college.placement.entity.Company;
import com.college.placement.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/officer/companies")
public class AdminCompanyController {

    private final CompanyService companyService;

    public AdminCompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public String listCompanies(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Company> companies = companyService.searchCompanies(keyword);
        model.addAttribute("companies", companies);
        model.addAttribute("keyword", keyword);
        return "officer/companies/list";
    }

    @GetMapping("/new")
    public String newCompanyForm(Model model) {
        if (!model.containsAttribute("company")) {
            model.addAttribute("company", new Company());
        }
        return "officer/companies/form";
    }

    @PostMapping("/new")
    public String createCompany(@Valid @ModelAttribute("company") Company company,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "officer/companies/form";
        }
        try {
            companyService.saveCompany(company);
            redirectAttributes.addFlashAttribute("successMessage", "Company '" + company.getName() + "' created successfully!");
            return "redirect:/officer/companies";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "officer/companies/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editCompanyForm(@PathVariable("id") Long id, Model model) {
        Company company = companyService.getCompanyById(id);
        model.addAttribute("company", company);
        return "officer/companies/form";
    }

    @PostMapping("/{id}/edit")
    public String updateCompany(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("company") Company company,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "officer/companies/form";
        }
        try {
            companyService.updateCompany(id, company);
            redirectAttributes.addFlashAttribute("successMessage", "Company updated successfully!");
            return "redirect:/officer/companies";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "officer/companies/form";
        }
    }

    @PostMapping("/{id}/toggle")
    public String toggleCompany(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        companyService.toggleActive(id);
        redirectAttributes.addFlashAttribute("successMessage", "Company status toggled successfully.");
        return "redirect:/officer/companies";
    }
}
