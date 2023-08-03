package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.Role;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.repository.SoftDelete.SoftDeleteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends SoftDeleteRepository<User, Integer>{

    Optional<User> save(User user);
    Optional<List<User>> findAllByRole(Role role);
}