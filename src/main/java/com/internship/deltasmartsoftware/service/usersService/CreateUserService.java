package com.internship.deltasmartsoftware.service.usersService;

import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.model.*;
import com.internship.deltasmartsoftware.payload.responses.*;
import com.internship.deltasmartsoftware.repository.*;
import com.internship.deltasmartsoftware.payload.requests.UserCreateRequest;
import com.internship.deltasmartsoftware.security.TokenProvider;
import com.internship.deltasmartsoftware.service.DepartmentService;
import com.internship.deltasmartsoftware.service.PushNotificationService;
import com.internship.deltasmartsoftware.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CreateUserService {
    private CompanyRepository companyRepository;
    private DepartmentService departmentService;
    private RoleRepository roleRepository;
    private UserService userService;
    private final TokenProvider tokenProvider;
    private PushNotificationService pushNotificationService;

    public CreateUserService(CompanyRepository companyRepository, DepartmentService departmentService, RoleRepository roleRepository, UserService userService, TokenProvider tokenProvider, PushNotificationService pushNotificationService) {
        this.companyRepository = companyRepository;
        this.departmentService = departmentService;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.pushNotificationService = pushNotificationService;
    }

    public ResponseEntity<Response<Iterable<DepartmentDTO>>> getDepartments(int companyId){
        Iterable<Department> departments = departmentService.findAllByCompanyId(companyId);
        Iterable<DepartmentDTO> departmentDTOS = StreamSupport.stream(departments.spliterator(), false)
                .map(DepartmentDTO::new).collect(Collectors.toList());
        return Response.ok("users.departmentsFound", departmentDTOS);
    }

    public ResponseEntity<Response<Object>> getRolesAndCompanies(){
        Map<String,Object> data = new HashMap<>();
        Iterable<Role> roles = roleRepository.findAllActive();
        roles.forEach(RoleDTO::new);
        Iterator<Role> iterator = roles.iterator();
        while (iterator.hasNext()){
            if (iterator.next().getName().equals("Admin")){
                iterator.remove();
            }
        }
        Iterable<Company> companies = companyRepository.findAllActive();
        companies.forEach(CompanyDTO::new);

        data.put("roles", roles);
        data.put("companies", companies);
        return Response.ok("users.rolesAndCompaniesFound", data);
    }

    public ResponseEntity<Response<UserDTO>> create(UserCreateRequest request,String token) {

        String email = tokenProvider.getUserEmailFromTokenProvider(token.substring(7));
        User currentUser;
        try{
            currentUser = userService.findByEmail(email);
        } catch (ResourceNotFoundException e) {
            return Response.badRequest(e.getMessage());
        }
        String currentRole = currentUser.getRole().getName();

        if (userService.existsByEmail(request.getEmail())){
            return Response.badRequest("users.emailAlreadyExists");
        }
        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setDepartment(departmentService.findOneById(request.getDepartmentId()));
        Role role = roleRepository.findOneActive(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        user.setRole(role);
        try{
            User savedUser = userService.save(user);
            if(currentRole.equals("Manager")) {
                pushNotificationService.sendUserCreatedNotificationToAdmins(currentUser, savedUser);
            }
        } catch (Exception e){
            return Response.badRequest(e.getMessage());
        }

        UserDTO userDTO = new UserDTO(user);
        return Response.ok("users.userCreated", userDTO);
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