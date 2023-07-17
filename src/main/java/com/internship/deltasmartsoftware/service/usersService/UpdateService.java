package com.internship.deltasmartsoftware.service.usersService;

import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.repository.DepartmentRepository;
import com.internship.deltasmartsoftware.repository.RoleRepository;
import com.internship.deltasmartsoftware.repository.UserRepository;
import com.internship.deltasmartsoftware.requests.UserCreateRequest;
import com.internship.deltasmartsoftware.responses.AuthResponse;
import com.internship.deltasmartsoftware.service.AuthUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateService {

    private UserRepository userRepository;
    private DepartmentRepository departmentRepository;
    private RoleRepository roleRepository;
    private AuthUserService userService;

    public UpdateService(UserRepository userRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository, AuthUserService userService) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    public ResponseEntity<AuthResponse> updateUser(UserCreateRequest request, int id){
        AuthResponse authResponse = new AuthResponse();

        User user = userRepository.findById(id);
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setDepartment(departmentRepository.findById(request.getDepartmentId()));
        user.setRole(roleRepository.findById(request.getRoleId()));
        userService.saveOneUser(user);
        authResponse.setUserId(user.getId());
        return ResponseEntity.ok(authResponse);
    }
}
