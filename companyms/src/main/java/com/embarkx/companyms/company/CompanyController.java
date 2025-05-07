package com.embarkx.companyms.company;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/companies")
public class CompanyController {

private final CompanyService companyService;

     @GetMapping
     public ResponseEntity<List<Company>> getAllCompanies() {
         return new ResponseEntity<>(companyService.getAllCompanies(), HttpStatus.OK);
     }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable long id) {
        Company company = companyService.getCompanyById(id);
        if(company == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<String> createCompany(@RequestBody Company company) {
        companyService.createCompany(company);
        return new ResponseEntity<>("Company created successfully!", HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable Long id, @RequestBody Company company) {

        boolean isUpdated = companyService.updateCompany(id, company);

        if(!isUpdated) {
            return new ResponseEntity<>("Company not found!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Company updated successfully!", HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        boolean isDeleted = companyService.deleteCompanyById(id);
        if(isDeleted) {
            return new ResponseEntity<>("Company deleted successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Company not found!", HttpStatus.NOT_FOUND);
    }

}
