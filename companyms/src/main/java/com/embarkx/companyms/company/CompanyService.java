package com.embarkx.companyms.company;


import com.embarkx.companyms.company.dto.ReviewMessage;

import java.util.List;


public interface CompanyService {

    List<Company> getAllCompanies();
    Company getCompanyById(Long id);
    boolean updateCompany( Long id, Company company);
    void createCompany(Company company);
    boolean deleteCompanyById(Long id);
    public void updateCompanyRating(ReviewMessage reviewMessage);
}
