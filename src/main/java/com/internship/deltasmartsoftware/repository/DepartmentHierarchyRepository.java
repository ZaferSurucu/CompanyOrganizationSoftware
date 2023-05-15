package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentHierarchyRepository extends JpaRepository<Department, Integer> {
}
