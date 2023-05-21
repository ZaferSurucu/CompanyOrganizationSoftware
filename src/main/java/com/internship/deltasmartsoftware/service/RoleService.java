package com.internship.deltasmartsoftware.service;

import com.internship.deltasmartsoftware.model.Role;
import com.internship.deltasmartsoftware.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleByRole(String role) {
        return roleRepository.findByName(role);
    }
}
