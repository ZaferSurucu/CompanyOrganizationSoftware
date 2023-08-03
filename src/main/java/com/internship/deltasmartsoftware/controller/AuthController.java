package com.internship.deltasmartsoftware.controller;

import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.payload.requests.EmailRequest;
import com.internship.deltasmartsoftware.payload.requests.LoginRequest;
import com.internship.deltasmartsoftware.payload.requests.SetNewPasswordRequest;
import com.internship.deltasmartsoftware.payload.responses.Response;
import com.internship.deltasmartsoftware.service.AuthControllerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthControllerService authControllerService;

    public AuthController(AuthControllerService authControllerService) {
        this.authControllerService = authControllerService;
    }

    @GetMapping("/test")
    public ResponseEntity<Response<Object>> test() {
        return Response.ok("auth.test", null);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<Object>> login(@RequestBody LoginRequest loginRequest,@RequestParam(value = "oneSignalId",required = false) String oneSignalId) {
        return authControllerService.login(loginRequest,oneSignalId);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<Response<Object>> forgotPassword(@RequestBody EmailRequest emailRequest, HttpServletRequest request) {
        return authControllerService.forgotPassword(emailRequest,request);
    }

    @PostMapping("/activateAccount")
    public ResponseEntity<Response<Object>> activateAccount(@RequestBody EmailRequest emailRequest, HttpServletRequest request) {
        return authControllerService.activateAccount(emailRequest,request);
    }

    @PostMapping("/setNewPassword")
    public ResponseEntity<Response<Object>> resetPassword(@RequestBody SetNewPasswordRequest setNewPasswordRequest) {
        return authControllerService.setNewPassword(setNewPasswordRequest);
    }

    @PostMapping("/verifyResetPasswordToken")
    public ResponseEntity<Response<Object>> verifyResetPasswordToken(@RequestParam("token") String token) {
        return authControllerService.verifyResetPasswordEmailToken(token);
    }


    @PostMapping("/verifyActivationEmailToken")
    public ResponseEntity<Response<Object>> verifyEmail(@RequestParam("token") String token) {
        return authControllerService.verifyActivationEmailToken(token);
    }
}
