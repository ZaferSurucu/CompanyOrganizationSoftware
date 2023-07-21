package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Department;
import com.internship.deltasmartsoftware.repository.SoftDelete.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends SoftDeleteRepository<Department, Integer> {
    Iterable<Department> findAllByCompanyId(int companyId);
}
