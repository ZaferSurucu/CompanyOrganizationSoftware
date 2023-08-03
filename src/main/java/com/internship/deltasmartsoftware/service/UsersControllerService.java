package com.internship.deltasmartsoftware.service;

import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.payload.responses.Response;
import com.internship.deltasmartsoftware.payload.responses.UserDTO;
import com.internship.deltasmartsoftware.security.TokenProvider;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.Objects;

@Service
public class UsersControllerService {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    public UsersControllerService(UserService userService, TokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    public ResponseEntity<Response<Page<UserDTO>>> getAllUsers(String keyword, int pageNumber, int pageSize, String field, String order, String token) {
        String email = tokenProvider.getUserEmailFromTokenProvider(token.substring(7));
        User user;
        try{
            user = userService.findByEmail(email);
        } catch (ResourceNotFoundException e) {
            return Response.badRequest(e.getMessage());
        }
        String role = user.getRole().getName();



        Sort.Direction direction = order.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(direction, field));
        Page<UserDTO> userPage;
        try{
            if(Objects.equals(keyword, null )) {
                if (role.equals("Admin"))
                    userPage = userService.findAllUsers(pageRequest);
                else
                    userPage = userService.findAllUsersByCompanyId(pageRequest, user.getDepartment().getCompany().getId());
            }
            else {
                if (role.equals("Admin"))
                    userPage = userService.findAllUsersByKeyword(keyword, pageRequest);
                else
                    userPage = userService.findAllUsersByKeywordAndCompanyId(keyword, pageRequest, user.getDepartment().getCompany().getId());
            }
        } catch (ResourceNotFoundException e) {
            return Response.notFound(e.getMessage());
        }


        return Response.ok("users.usersFound", userPage);
    }

    public ResponseEntity<Response<UserDTO>> getUser(int id){
        try{
            User user = userService.findOneById(id);
            UserDTO userDTO = new UserDTO(user);
            return Response.ok("users.userFound", userDTO);
        } catch (ResourceNotFoundException e) {
            return Response.notFound(e.getMessage());
        }
    }
}
