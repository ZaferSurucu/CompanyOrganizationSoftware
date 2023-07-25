package com.internship.deltasmartsoftware.service;

import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.payload.responses.Response;
import com.internship.deltasmartsoftware.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Service
public class UsersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersService.class);
    private final UserRepository userRepository;

    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // get users by pagination and sorting
    public ResponseEntity<Response<Object>> getAllUsers(String keyword, int pageNumber, int pageSize, String[] sortingParams) {
        Map<String,Object> data = new HashMap<>();
        String field = sortingParams[0];
        String order = sortingParams[1];
        // log field and order
        LOGGER.info("field: " + field + ", order: " + order);

        Sort.Direction direction = order.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(direction, field));
        Page<User> userPage;

        if(keyword == null) {
            userPage = userRepository.findAllActive(pageRequest);
        }
        else {
            userPage = userRepository.findByNameContainingIgnoreCase(keyword, pageRequest);
        }
        data.put("users", userPage.getContent());
        data.put("length", userPage.getTotalElements());

        return Response.ok("users.usersFound", data);
    }

    public ResponseEntity<Response<User>> getUser(int id){
        User user = userRepository.findOneActive(id).orElseThrow(() -> new ResourceNotFoundException("users.userNotFound"));
        return Response.ok("users.userFound", user);
    }
}
