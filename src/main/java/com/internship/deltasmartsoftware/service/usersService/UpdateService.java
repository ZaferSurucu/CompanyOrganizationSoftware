package com.internship.deltasmartsoftware.service.usersService;

import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.payload.responses.Response;
import com.internship.deltasmartsoftware.payload.responses.UserDTO;
import com.internship.deltasmartsoftware.repository.DepartmentRepository;
import com.internship.deltasmartsoftware.repository.RoleRepository;
import com.internship.deltasmartsoftware.repository.UserRepository;
import com.internship.deltasmartsoftware.payload.requests.UserCreateRequest;
import com.internship.deltasmartsoftware.service.DepartmentService;
import com.internship.deltasmartsoftware.service.UserService;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateService {

    private UserService userService;
    private DepartmentService departmentService;
    private RoleRepository roleRepository;

    public UpdateService(UserService userService, DepartmentService departmentService, RoleRepository roleRepository) {
        this.userService = userService;
        this.departmentService = departmentService;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<Response<UserDTO>> updateUser(UserCreateRequest request, int id){
        try{
            User user = userService.findOneById(id);
            user.setName(request.getName());
            user.setSurname(request.getSurname());
            user.setEmail(request.getEmail());
            user.setDepartment(departmentService.findOneById(request.getDepartmentId()));
            user.setRole(roleRepository.findOneActive(request.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
            UserDTO userDTO = new UserDTO(userService.save(user));
            return Response.ok("users.userUpdated", userDTO);
        } catch (ResourceNotFoundException e){
            return Response.badRequest(e.getMessage());
        }

    }
}
