package com.internship.deltasmartsoftware.controller;

import com.internship.deltasmartsoftware.requests.UserRequest;
import com.internship.deltasmartsoftware.responses.AuthResponse;
import com.internship.deltasmartsoftware.service.authService.LoginService;
import com.internship.deltasmartsoftware.service.authService.SendEmailService;
import com.internship.deltasmartsoftware.service.authService.SetNewPasswordService;
import com.internship.deltasmartsoftware.service.authService.VerifyTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    //The admin sets Tolgahan's role, department, company, name, surname, email.
    //'Password' by default is null and 'Enabled' by default is 0.
    //An email with a link(15 minute expiration duration

    private SendEmailService sendEmailService;
    private VerifyTokenService verifyTokenService;
    private LoginService loginService;
    private SetNewPasswordService setNewPasswordService;

    public AuthController(SendEmailService sendEmailService
            , VerifyTokenService verifyTokenService
            , LoginService loginService
            , SetNewPasswordService setNewPasswordService) {
        this.sendEmailService = sendEmailService;
        this.verifyTokenService = verifyTokenService;
        this.loginService = loginService;
        this.setNewPasswordService = setNewPasswordService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody UserRequest loginRequest) {
        return loginService.login(loginRequest);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<AuthResponse> forgotPassword(@RequestBody UserRequest userRequest, HttpServletRequest request) {
        return sendEmailService.forgotPassword(userRequest,request);
    }

    @PostMapping("/activateAccount")
    public ResponseEntity<AuthResponse> activateAccount(@RequestBody UserRequest userRequest, HttpServletRequest request) {
        return sendEmailService.activateAccount(userRequest,request);
    }

    @PostMapping("/setNewPassword")
    public ResponseEntity<AuthResponse> resetPassword(@RequestBody UserRequest userRequest) {
        return setNewPasswordService.setNewPassword(userRequest);
    }

    @PostMapping("/verifyResetPasswordToken")
    public ResponseEntity<AuthResponse> verifyResetPasswordToken(@RequestParam("token") String token) {
        return verifyTokenService.verifyResetPasswordEmailToken(token);
    }


    @PostMapping("/verifyActivationEmailToken")
    public ResponseEntity<AuthResponse> verifyEmail(@RequestParam("token") String token) {
        return verifyTokenService.verifyActivationEmailToken(token);
    }

    /*
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

    }*/
}
