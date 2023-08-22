package com.internship.deltasmartsoftware.service;

import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.payload.responses.UserDTO;
import com.internship.deltasmartsoftware.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        Specification<User> spec = (root, query, cb)
                -> cb.equal(root.get("email"), email);
        return userRepository.findOneActive(spec).orElseThrow(() -> new ResourceNotFoundException("users.userNotFound"));
    }

    public Page<UserDTO> findAllUsersByKeyword(String keyword, Pageable pageable) throws ResourceNotFoundException {
        Specification<User> spec = (root, query, cb)
                -> cb.or(cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("surname")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%"));
        Page<User> users = userRepository.findAllActive(spec, pageable);
        return changeToUserDTO(users,pageable);
    }

    public Page<UserDTO> findAllUsersByKeywordAndCompanyId(String keyword, Pageable pageable, int companyId) throws ResourceNotFoundException{
        Specification<User> spec = (root, query, cb)
                -> cb.and(cb.equal(root.get("department").get("company").get("id"), companyId),
                cb.or(cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("surname")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%")));
        Page<User> users = userRepository.findAllActive(spec, pageable);
        return changeToUserDTO(users,pageable);
    }

    public Page<UserDTO> findAllUsersByKeywordAndDepartmentId(String keyword, Pageable pageable, int departmentId) throws ResourceNotFoundException{
        Specification<User> spec = (root, query, cb)
                -> cb.and(cb.equal(root.get("department").get("id"), departmentId),
                cb.or(cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("surname")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%")));
        Page<User> users = userRepository.findAllActive(spec, pageable);
        return changeToUserDTO(users,pageable);
    }

    public User save(User user) {
        return userRepository.save(user).orElseThrow(() -> new IllegalArgumentException("users.userNotSaved"));
    }

    public User findOneById(int id) {
        return userRepository.findOneActive(id).orElseThrow(() -> new ResourceNotFoundException("users.userNotFound"));
    }

    public Page<UserDTO> findAllUsers(Pageable pageable) throws ResourceNotFoundException{
        Page<User> users = userRepository.findAllActive(pageable);
        return changeToUserDTO(users,pageable);
    }

    public Page<UserDTO> findAllUsersByDepartmentId(Pageable pageable, int departmentId) throws ResourceNotFoundException{
        Specification<User> spec = (root, query, cb)
                -> cb.equal(root.get("department").get("id"), departmentId);
        Page<User> users = userRepository.findAllActive(spec, pageable);
        return changeToUserDTO(users,pageable);
    }

    public Page<UserDTO> findAllUsersByCompanyId(Pageable pageable, int companyId) throws ResourceNotFoundException{
        Specification<User> spec = (root, query, cb)
                -> cb.equal(root.get("department").get("company").get("id"), companyId);
        Page<User> users = userRepository.findAllActive(spec, pageable);
        return changeToUserDTO(users,pageable);
    }

    private Page<UserDTO> changeToUserDTO(Page<User> users, Pageable pageable) {
        Page<UserDTO> userDTOS = new PageImpl<>(
                users.getContent().stream().map(
                        UserDTO::new
                ).collect(Collectors.toList()),
                pageable,users.getTotalElements());
        if(userDTOS.isEmpty())
            throw new ResourceNotFoundException("users.usersNotFound");
        return userDTOS;
    }

    public boolean existsByEmail(String email) {
        Specification<User> spec = (root, query, cb)
                -> cb.equal(root.get("email"), email);
        return userRepository.findOneActive(spec).isPresent();
    }
}
