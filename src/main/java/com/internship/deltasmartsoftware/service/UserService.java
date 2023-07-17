package com.internship.deltasmartsoftware.service;

import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.repository.UserRepository;
import com.internship.deltasmartsoftware.responses.UsersAndLengthResponse;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // get users by pagination and sorting
    public ResponseEntity<UsersAndLengthResponse> getAllUsers(String keyword, int pageNumber, int pageSize, String[] sortingParams) {
        UsersAndLengthResponse usersAndLengthResponse = new UsersAndLengthResponse();
        String field = sortingParams[0];
        String order = sortingParams[1];
        // log field and order
        LOGGER.info("field: " + field + ", order: " + order);

        Sort.Direction direction = order.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(direction, field));
        Page<User> userPage;

        if(keyword == null) {
            userPage = userRepository.findAll(pageRequest);
        }
        else {
            userPage = userRepository.findByNameContainingIgnoreCase(keyword, pageRequest);
        }
        usersAndLengthResponse.setUsers(userPage.getContent());
        usersAndLengthResponse.setLength(userPage.getTotalElements());

        return ResponseEntity.ok(usersAndLengthResponse);
    }

    public User getUserById(int id) {
        return userRepository.findById(id);
    }
}
