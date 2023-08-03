package com.internship.deltasmartsoftware.service;

import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.model.Department;
import com.internship.deltasmartsoftware.repository.DepartmentRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {
    private DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department findOneById(int id) {
        return departmentRepository.findOneActive(id).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
    }

    public Iterable<Department> findAllByCompanyId(int companyId) {
        Specification<Department> spec = ((root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("company").get("id"), companyId));
        return departmentRepository.findAllActive(spec);
    }
}
