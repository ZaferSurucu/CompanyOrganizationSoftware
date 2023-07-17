package com.internship.deltasmartsoftware.service.usersService;

import com.internship.deltasmartsoftware.model.*;
import com.internship.deltasmartsoftware.repository.*;
import com.internship.deltasmartsoftware.requests.UserCreateRequest;
import com.internship.deltasmartsoftware.responses.AuthResponse;
import com.internship.deltasmartsoftware.responses.RegisterResponse;
import com.internship.deltasmartsoftware.service.AuthUserService;
import org.passay.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreateUserService {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CreateUserService.class);
    private CompanyRepository companyRepository;
    private DepartmentRepository departmentRepository;
    private RoleRepository roleRepository;
    private AuthUserService userService;

    public CreateUserService(CompanyRepository companyRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository, AuthUserService userService) {
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    public ResponseEntity<List<Department>> getDepartments(int companyId){
        List<Department> departments = departmentRepository.findAllByCompanyId(companyId);
        return ResponseEntity.ok(departments);
    }

    public ResponseEntity<RegisterResponse> getRolesAndCompanies(){
        RegisterResponse registerResponse = new RegisterResponse();
        List<Role> roles = roleRepository.findAll();
        registerResponse.setRoles(roles);
        registerResponse.setCompanies(companyRepository.findAll());
        return ResponseEntity.ok(registerResponse);
    }

    public ResponseEntity<AuthResponse> create(UserCreateRequest request) {

        AuthResponse authResponse = new AuthResponse();
        if(userService.getOneUserByEmail(request.getEmail()) != null) {
            authResponse.setMessage("User already exists");
            return ResponseEntity.badRequest().body(authResponse);
        }

        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setDepartment(departmentRepository.findById(request.getDepartmentId()));
        user.setRole(roleRepository.findById(request.getRoleId()));
        userService.saveOneUser(user);
        authResponse.setUserId(user.getId());
        return ResponseEntity.ok(authResponse);
    }







/*
    private RegionRepository regionRepository;
    private TownRepository townRepository;

    public RegisterService(RegionRepository regionRepository, TownRepository townRepository) {
        this.regionRepository = regionRepository;
        this.townRepository = townRepository;
    }

    public ResponseEntity<RegionAndTownResponse> getRegionsAndTowns(int cityId){
        RegionAndTownResponse regionAndTownResponse = new RegionAndTownResponse();
        List<Region> regions = regionRepository.findAllByCityId(cityId);
        regionAndTownResponse.setRegions(regions);

        List<Town> towns = null;

        for (Region region: regions
             ) {
            towns.addAll(townRepository.findAllByRegionId(region.getId()));
        }
        regionAndTownResponse.setTowns(towns);

        return ResponseEntity.ok(regionAndTownResponse);
    }*/
}