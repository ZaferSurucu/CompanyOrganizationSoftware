package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String role);
}
