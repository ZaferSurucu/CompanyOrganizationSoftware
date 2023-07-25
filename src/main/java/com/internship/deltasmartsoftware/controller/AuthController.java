package com.internship.deltasmartsoftware.controller;

import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.payload.requests.LoginRequest;
import com.internship.deltasmartsoftware.payload.responses.Response;
import com.internship.deltasmartsoftware.service.AuthService;
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

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test")
    public ResponseEntity<Response<Object>> test() {
        return Response.ok("auth.test", null);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<Object>> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<Response<User>> forgotPassword(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        return authService.forgotPassword(loginRequest,request);
    }

    @PostMapping("/activateAccount")
    public ResponseEntity<Response<User>> activateAccount(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        return authService.activateAccount(loginRequest,request);
    }

    @PostMapping("/setNewPassword") //
    public ResponseEntity<Response<Object>> resetPassword(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        return authService.setNewPassword(loginRequest,request);
    }

    @PostMapping("/verifyResetPasswordToken")
    public ResponseEntity<Response<User>> verifyResetPasswordToken(@RequestParam("token") String token, HttpServletRequest request) {
        return authService.verifyResetPasswordEmailToken(token,request);
    }


    @PostMapping("/verifyActivationEmailToken")
    public ResponseEntity<Response<User>> verifyEmail(@RequestParam("token") String token, HttpServletRequest request) {
        return authService.verifyActivationEmailToken(token,request);
    }
}
