package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.repository.SoftDelete.SoftDeleteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends SoftDeleteRepository<User, Integer>{
    Optional<User> findByEmail(String email);
    Optional<User> save(User user);
    Page<User> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
