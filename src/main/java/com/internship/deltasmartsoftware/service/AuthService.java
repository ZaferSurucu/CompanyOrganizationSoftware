package com.internship.deltasmartsoftware.service;

import com.internship.deltasmartsoftware.requests.LoginRequest;
import com.internship.deltasmartsoftware.responses.AuthResponse;
import com.internship.deltasmartsoftware.service.authService.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class AuthService {

    private LoginService loginService;

    private SendEmailService sendEmailService;

    private SetNewPasswordService setNewPasswordService;

    private VerifyTokenService verifyTokenService;

    public AuthService(LoginService loginService
            , SendEmailService sendEmailService
            , SetNewPasswordService setNewPasswordService
            , VerifyTokenService verifyTokenService) {
        this.loginService = loginService;
        this.sendEmailService = sendEmailService;
        this.setNewPasswordService = setNewPasswordService;
        this.verifyTokenService = verifyTokenService;
    }

    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }

    public ResponseEntity<AuthResponse> forgotPassword(LoginRequest loginRequest, HttpServletRequest request) {
        return sendEmailService.forgotPassword(loginRequest,request);
    }

    public ResponseEntity<AuthResponse> activateAccount(LoginRequest loginRequest, HttpServletRequest request) {
        return sendEmailService.activateAccount(loginRequest,request);
    }

    public ResponseEntity<AuthResponse> setNewPassword(LoginRequest loginRequest) {
        return setNewPasswordService.setNewPassword(loginRequest);
    }

    public ResponseEntity<AuthResponse> verifyResetPasswordEmailToken(String token) {
        return verifyTokenService.verifyResetPasswordEmailToken(token);
    }

    public ResponseEntity<AuthResponse> verifyActivationEmailToken(String token) {
        return verifyTokenService.verifyActivationEmailToken(token);
    }
}
