package com.internship.deltasmartsoftware.service.usersService;

import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.model.*;
import com.internship.deltasmartsoftware.payload.responses.Response;
import com.internship.deltasmartsoftware.repository.*;
import com.internship.deltasmartsoftware.payload.requests.UserCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Service
public class CreateUserService {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CreateUserService.class);
    private CompanyRepository companyRepository;
    private DepartmentRepository departmentRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    public CreateUserService(CompanyRepository companyRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Response<Iterable<Department>>> getDepartments(int companyId){
        Iterable<Department> departments = departmentRepository.findAllByCompanyId(companyId);
        return Response.ok("users.departmentsFound", departments);
    }

    public ResponseEntity<Response<Object>> getRolesAndCompanies(){
        Map<String,Object> data = new HashMap<>();
        data.put("roles", roleRepository.findAllActive());
        data.put("companies", companyRepository.findAllActive());
        return Response.ok("users.rolesAndCompaniesFound", data);
    }

    public ResponseEntity<Response<User>> create(UserCreateRequest request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            return Response.badRequest("users.emailAlreadyExists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setDepartment(departmentRepository.findOneActive(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found")));
        user.setRole(roleRepository.findOneActive(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found")));
        userRepository.save(user);
        return Response.ok("users.userCreated", user);
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