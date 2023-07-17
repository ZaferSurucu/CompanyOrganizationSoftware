package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    //List<Company> findAll();
}
