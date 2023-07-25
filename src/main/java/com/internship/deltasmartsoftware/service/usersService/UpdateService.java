package com.internship.deltasmartsoftware.service.usersService;

import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.payload.responses.Response;
import com.internship.deltasmartsoftware.repository.DepartmentRepository;
import com.internship.deltasmartsoftware.repository.RoleRepository;
import com.internship.deltasmartsoftware.repository.UserRepository;
import com.internship.deltasmartsoftware.payload.requests.UserCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateService {

    private UserRepository userRepository;
    private DepartmentRepository departmentRepository;
    private RoleRepository roleRepository;

    public UpdateService(UserRepository userRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<Response<User>> updateUser(UserCreateRequest request, int id){

        User user = userRepository.findOneActive(id).orElseThrow(() -> new ResourceNotFoundException("users.userNotFound"));
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setDepartment(departmentRepository.findOneActive(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found")));
        user.setRole(roleRepository.findOneActive(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
        userRepository.save(user);
        return Response.ok("users.userUpdated", user);
    }
}
