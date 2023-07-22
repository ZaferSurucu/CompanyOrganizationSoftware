package com.internship.deltasmartsoftware.service.usersService;

import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.model.*;
import com.internship.deltasmartsoftware.repository.*;
import com.internship.deltasmartsoftware.requests.UserCreateRequest;
import com.internship.deltasmartsoftware.responses.RegisterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

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

    public ResponseEntity<Iterable<Department>> getDepartments(int companyId){
        Iterable<Department> departments = departmentRepository.findAllByCompanyId(companyId);
        return ResponseEntity.ok(departments);
    }

    public ResponseEntity<RegisterResponse> getRolesAndCompanies(){
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setRoles(roleRepository.findAllActive());
        registerResponse.setCompanies(companyRepository.findAllActive());
        return ResponseEntity.ok(registerResponse);
    }

    public ResponseEntity<User> create(UserCreateRequest request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
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
        return ResponseEntity.ok(user);
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