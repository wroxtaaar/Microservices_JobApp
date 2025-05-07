package com.embarkx.companyms.company;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Custom query methods can be defined here if needed
    // For example:
    // List<Company> findByName(String name);
}
