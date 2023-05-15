package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
