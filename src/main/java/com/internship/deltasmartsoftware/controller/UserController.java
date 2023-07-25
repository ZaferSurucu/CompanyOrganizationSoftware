package com.internship.deltasmartsoftware.controller;

import com.internship.deltasmartsoftware.model.Department;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.payload.requests.UserCreateRequest;
import com.internship.deltasmartsoftware.payload.responses.Response;
import com.internship.deltasmartsoftware.service.UsersService;
import com.internship.deltasmartsoftware.service.usersService.DeleteService;
import com.internship.deltasmartsoftware.service.usersService.CreateUserService;
import com.internship.deltasmartsoftware.service.usersService.UpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private CreateUserService createUserService;
    private UsersService usersService;
    private UpdateService updateService;
    private DeleteService deleteService;

    public UserController(CreateUserService createUserService, UsersService usersService, UpdateService updateService, DeleteService deleteService) {
        this.createUserService = createUserService;
        this.usersService = usersService;
        this.updateService = updateService;
        this.deleteService = deleteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<User>> getUser(@PathVariable("id") int id){
        return usersService.getUser(id);
    }

    @GetMapping("/all")
    public ResponseEntity<Response<Object>> getAllUsers(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sort", defaultValue = "id, asc") String[] sortingParams
    ) {
        return usersService.getAllUsers(keyword, pageNumber, pageSize, sortingParams);
    }

    @GetMapping("/create")
    public ResponseEntity<Response<Object>> getRolesAndCompanies(){
        return createUserService.getRolesAndCompanies();
    }

    @GetMapping("/departments/{companyId}")
    public ResponseEntity<Response<Iterable<Department>>> getDepartments(@PathVariable(value = "companyId") int companyId){
        return createUserService.getDepartments(companyId);
    }

    @PostMapping("/create")
    public ResponseEntity<Response<User>> createUser(@RequestBody UserCreateRequest userRequest){
        return createUserService.create(userRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<User>> updateUser(UserCreateRequest request, @PathVariable("id") int id){
        return updateService.updateUser(request, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<User>> deleteUser(@PathVariable("id") int userId){
        return deleteService.deleteUser(userId);
    }
}
