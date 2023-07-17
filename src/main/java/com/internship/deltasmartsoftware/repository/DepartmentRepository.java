package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    public List<Department> findAllByCompanyId(int companyId);
    public Department findById(int id);
}
