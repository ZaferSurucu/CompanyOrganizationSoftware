package com.internship.deltasmartsoftware.repository;

import com.internship.deltasmartsoftware.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

abstract class UserRepositoryImpl implements UserRepository {

    @Override
    public Optional<User> findByEmail(String email) {
        Specification<User> spec = (root, query, cb)
                -> cb.equal(root.get("email"), email);
        return findOneActive(spec);
    }

    @Override
    public Page<User> findByNameContainingIgnoreCase(String keyword, Pageable pageable) {
        Specification<User> spec = (root, query, cb)
                -> cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
        return findAllActive(spec, pageable);
    }
}
