package com.college.placement.service;

import com.college.placement.entity.Company;
import com.college.placement.exception.ResourceNotFoundException;
import com.college.placement.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    public List<Company> getAllActiveCompanies() {
        return companyRepository.findByActiveTrueOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Company> searchCompanies(String keyword) {
        return companyRepository.searchCompanies(keyword != null ? keyword.trim() : null);
    }

    @Transactional(readOnly = true)
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
    }

    @Transactional
    public Company saveCompany(Company company) {
        if (company.getId() == null && companyRepository.existsByNameIgnoreCase(company.getName())) {
            throw new IllegalArgumentException("Company with name '" + company.getName() + "' already exists.");
        }
        return companyRepository.save(company);
    }

    @Transactional
    public Company updateCompany(Long id, Company details) {
        Company company = getCompanyById(id);

        if (!company.getName().equalsIgnoreCase(details.getName()) &&
                companyRepository.existsByNameIgnoreCase(details.getName())) {
            throw new IllegalArgumentException("Company with name '" + details.getName() + "' already exists.");
        }

        company.setName(details.getName().trim());
        company.setDescription(details.getDescription());
        company.setWebsite(details.getWebsite());
        company.setIndustry(details.getIndustry());
        company.setLocation(details.getLocation());
        company.setContactPerson(details.getContactPerson());
        company.setContactEmail(details.getContactEmail());
        company.setContactPhone(details.getContactPhone());

        return companyRepository.save(company);
    }

    @Transactional
    public void toggleActive(Long id) {
        Company company = getCompanyById(id);
        company.setActive(!company.isActive());
        companyRepository.save(company);
    }
}
