package com.internship.deltasmartsoftware.service.authService;

import com.internship.deltasmartsoftware.events.model.RegistrationCompleteEvent;
import com.internship.deltasmartsoftware.events.model.ResetPasswordCompleteEvent;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.requests.LoginRequest;
import com.internship.deltasmartsoftware.responses.AuthResponse;
import com.internship.deltasmartsoftware.service.AuthUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class SendEmailService {

    private AuthUserService userService;
    private ApplicationEventPublisher publisher;

    public SendEmailService(AuthUserService userService, ApplicationEventPublisher publisher) {
        this.userService = userService;
        this.publisher = publisher;
    }

    public ResponseEntity<AuthResponse> forgotPassword(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        AuthResponse authResponse = new AuthResponse();
        if(userService.getOneUserByEmail(loginRequest.getEmail()) != null) {
            User user = userService.getOneUserByEmail(loginRequest.getEmail());
            publisher.publishEvent(new ResetPasswordCompleteEvent(user, applicationUrl(request)));
            authResponse.setMessage("Password reset email sent");
            return ResponseEntity.ok(authResponse);
        }
        else {
            authResponse.setMessage("User does not exist");
            return ResponseEntity.badRequest().body(authResponse);
        }
    }

    public ResponseEntity<AuthResponse> activateAccount(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        AuthResponse authResponse = new AuthResponse();
        if(userService.getOneUserByEmail(loginRequest.getEmail()) != null) {
            if(userService.getOneUserByEmail(loginRequest.getEmail()).getEnabled()) {
                authResponse.setMessage("User already activated");
                return ResponseEntity.badRequest().body(authResponse);
            } else {
                User user = userService.getOneUserByEmail(loginRequest.getEmail());
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

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
