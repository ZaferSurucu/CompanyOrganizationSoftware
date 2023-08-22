package com.internship.deltasmartsoftware.controller;

import com.internship.deltasmartsoftware.payload.requests.UserCreateRequest;
import com.internship.deltasmartsoftware.payload.responses.DepartmentDTO;
import com.internship.deltasmartsoftware.payload.responses.Response;
import com.internship.deltasmartsoftware.payload.responses.UserDTO;
import com.internship.deltasmartsoftware.service.UsersControllerService;
import com.internship.deltasmartsoftware.service.usersService.DeleteService;
import com.internship.deltasmartsoftware.service.usersService.CreateUserService;
import com.internship.deltasmartsoftware.service.usersService.UpdateService;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UsersController {

    Logger logger = org.slf4j.LoggerFactory.getLogger(UsersController.class);

    private CreateUserService createUserService;
    private UsersControllerService usersControllerService;
    private UpdateService updateService;
    private DeleteService deleteService;

    public UsersController(CreateUserService createUserService, UsersControllerService usersControllerService, UpdateService updateService, DeleteService deleteService) {
        this.createUserService = createUserService;
        this.usersControllerService = usersControllerService;
        this.updateService = updateService;
        this.deleteService = deleteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<UserDTO>> getUser(@PathVariable(value = "id") int id){
        return usersControllerService.getUser(id);
    }

    @GetMapping("/all")
    public ResponseEntity<Response<Page<UserDTO>>> getAllUsers(
            @RequestParam(value = "companyId", required = false) Integer companyId,
            @RequestParam(value = "departmentId", required = false) Integer departmentId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sort", defaultValue = "id") String field,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestHeader(value = "Authorization", required = false) String token
    ) {
        return usersControllerService.getAllUsers(keyword, pageNumber, pageSize, field, order, token, companyId, departmentId);
    }

    @GetMapping("/roles-and-companies")
    public ResponseEntity<Response<Object>> getRolesAndCompanies(){
        return createUserService.getRolesAndCompanies();
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<Response<Iterable<DepartmentDTO>>> getDepartments(@PathVariable(value = "id") int id){
        return createUserService.getDepartments(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Response<UserDTO>> createUser(@RequestBody UserCreateRequest userRequest
    ,@RequestHeader(value = "Authorization", required = false) String token){
        return createUserService.create(userRequest,token);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<UserDTO>> updateUser(@RequestBody UserCreateRequest request, @PathVariable(value = "id") int id){
        return updateService.updateUser(request, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<UserDTO>> deleteUser(@PathVariable(value = "id") int userId){
        return deleteService.deleteUser(userId);
    }
}
