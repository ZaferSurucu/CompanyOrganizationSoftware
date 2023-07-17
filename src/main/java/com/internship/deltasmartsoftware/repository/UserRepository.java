package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    User findById(int id);
    void deleteById(int id);
    Page<User> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
