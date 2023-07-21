package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Department;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

abstract class DepartmentRepositoryImpl implements DepartmentRepository{

    @Override
    public Iterable<Department> findAllByCompanyId(int companyId) {
        Specification<Department> spec = ((root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("companyId"), companyId));
        return findAllActive(spec);
    }
}
