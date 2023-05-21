package com.internship.deltasmartsoftware.controller;

import com.internship.deltasmartsoftware.model.Role;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.requests.UserRequest;
import com.internship.deltasmartsoftware.responses.AuthResponse;
import com.internship.deltasmartsoftware.security.RegistrationCompleteEvent;
import com.internship.deltasmartsoftware.security.TokenProvider;
import com.internship.deltasmartsoftware.model.VerificationToken;
import com.internship.deltasmartsoftware.repository.VerificationTokenRepository;
import com.internship.deltasmartsoftware.service.RoleService;
import com.internship.deltasmartsoftware.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class AuthController {
    Logger logger = LoggerFactory.getLogger(AuthController.class);
    private UserService userService;

    private RoleService roleService;

    private AuthenticationManager authenticationManager;

    private TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder;
    private ApplicationEventPublisher publisher;
    private VerificationTokenRepository tokenRepository;


    public AuthController(UserService userService, RoleService roleService
            , AuthenticationManager authenticationManager, TokenProvider tokenProvider
            , PasswordEncoder passwordEncoder, ApplicationEventPublisher publisher
            , VerificationTokenRepository tokenRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.publisher = publisher;
        this.tokenRepository = tokenRepository;
    }

    @GetMapping("/test")
    public String getTest() {
        return "get test";
    }

    @PostMapping("/test")
    public String postTest() {
        return "post test";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody UserRequest loginRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = tokenProvider.generateToken(auth);
        User user = userService.getOneUserByEmail(loginRequest.getEmail());
        logger.info("user has this role: " + user.getRole().getName());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("User logged in"
                + loginRequest.getEmail()
                + " " + loginRequest.getPassword());
        authResponse.setAccessToken("Bearer " + jwtToken);
        authResponse.setUserId(user.getId());
        return authResponse;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody UserRequest userRequest) {
        AuthResponse authResponse = new AuthResponse();
        if(userService.getOneUserByEmail(userRequest.getEmail()) != null) {
            authResponse.setMessage("User already exists");
            return ResponseEntity.badRequest().body(authResponse);
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        Role role = roleService.getRoleByRole(userRequest.getRole());
        logger.info("role: " + userRequest.getRole() + " " + role.getName() + " " + roleService.getRoleByRole(userRequest.getRole()).getName());
        user.setRole(role);
        userService.saveOneUser(user);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = tokenProvider.generateToken(auth);

        authResponse.setMessage("User created" + userRequest.getEmail()
                + " " + userRequest.getPassword()
                + " " + userRequest.getName()
                + " " + userRequest.getRole());
        authResponse.setAccessToken("Bearer " + token);
        authResponse.setUserId(user.getId());
        return ResponseEntity.ok(authResponse);

    }

    @PostMapping("/activateAccount")
    public ResponseEntity<AuthResponse> activateAccount(@RequestBody UserRequest userRequest, HttpServletRequest request) {
        AuthResponse authResponse = new AuthResponse();
        if(userService.getOneUserByEmail(userRequest.getEmail()) != null) {
            if(userService.getOneUserByEmail(userRequest.getEmail()).getEnabled()) {
                authResponse.setMessage("User already activated");
                return ResponseEntity.badRequest().body(authResponse);
            } else {
                User user = userService.getOneUserByEmail(userRequest.getEmail());
                publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
                authResponse.setMessage("Activation email sent");
                return ResponseEntity.ok(authResponse);
            }
        }
        else {
            authResponse.setMessage("User does not exist");
            return ResponseEntity.badRequest().body(authResponse);
        }
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().getEnabled()){
            return "This account has already been verified, please, login.";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            return "Email verified successfully. Now you can login to your account";
        }
        return "Invalid verification token";
    }
}
