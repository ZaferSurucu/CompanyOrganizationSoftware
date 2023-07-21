package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Role;
import com.internship.deltasmartsoftware.repository.SoftDelete.SoftDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends SoftDeleteRepository<Role, Integer> {
}
