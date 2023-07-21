package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Company;
import com.internship.deltasmartsoftware.repository.SoftDelete.SoftDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends SoftDeleteRepository<Company, Integer> {
}
