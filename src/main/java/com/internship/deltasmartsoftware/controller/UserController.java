package com.internship.deltasmartsoftware.controller;

import com.internship.deltasmartsoftware.model.Department;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.requests.UserCreateRequest;
import com.internship.deltasmartsoftware.responses.AuthResponse;
import com.internship.deltasmartsoftware.responses.RegisterResponse;
import com.internship.deltasmartsoftware.responses.UsersAndLengthResponse;
import com.internship.deltasmartsoftware.service.UserService;
import com.internship.deltasmartsoftware.service.usersService.DeleteService;
import com.internship.deltasmartsoftware.service.usersService.CreateUserService;
import com.internship.deltasmartsoftware.service.usersService.UpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private CreateUserService createUserService;
    private UserService userService;
    private UpdateService updateService;
    private DeleteService deleteService;

    public UserController(CreateUserService createUserService, UserService userService, UpdateService updateService, DeleteService deleteService) {
        this.createUserService = createUserService;
        this.userService = userService;
        this.updateService = updateService;
        this.deleteService = deleteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") int id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<UsersAndLengthResponse> getAllUsers(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = true) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = true) int pageSize,
            @RequestParam(value = "sort", defaultValue = "id, asc", required = true) String[] sortingParams
    ) {
        return userService.getAllUsers(keyword, pageNumber, pageSize, sortingParams);
    }

    @GetMapping("/create")
    public ResponseEntity<RegisterResponse> getRolesAndCompanies(){
        return createUserService.getRolesAndCompanies();
    }

    @GetMapping("/departments/{companyId}")
    public ResponseEntity<List<Department>> getDepartments(@PathVariable(value = "companyId") int companyId){
        return createUserService.getDepartments(companyId);
    }

    @PostMapping("/create")
    public ResponseEntity<AuthResponse> createUser(@RequestBody UserCreateRequest userRequest){
        return createUserService.create(userRequest);
    }
/*
    @GetMapping("/update/{id}")
    public ResponseEntity<User> getUserProps(@PathVariable("id") int id){
        return updateService.getUserPropsById(id);
    }
*/
    @PutMapping("/{id}")
    public ResponseEntity<AuthResponse> updateUser(UserCreateRequest request, @PathVariable("id") int id){
        return updateService.updateUser(request, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int userId){
        deleteService.deleteUser(userId);
    }
}
