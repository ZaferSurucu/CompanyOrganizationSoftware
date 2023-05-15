package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
