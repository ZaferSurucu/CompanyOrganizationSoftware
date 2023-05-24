package com.internship.deltasmartsoftware.service.authService;

import com.internship.deltasmartsoftware.events.model.RegistrationCompleteEvent;
import com.internship.deltasmartsoftware.events.model.ResetPasswordCompleteEvent;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.requests.UserRequest;
import com.internship.deltasmartsoftware.responses.AuthResponse;
import com.internship.deltasmartsoftware.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class SendEmailService {

    private UserService userService;
    private ApplicationEventPublisher publisher;

    public SendEmailService(UserService userService, ApplicationEventPublisher publisher) {
        this.userService = userService;
        this.publisher = publisher;
    }

    public ResponseEntity<AuthResponse> forgotPassword(@RequestBody UserRequest userRequest, HttpServletRequest request) {
        AuthResponse authResponse = new AuthResponse();
        if(userService.getOneUserByEmail(userRequest.getEmail()) != null) {
            User user = userService.getOneUserByEmail(userRequest.getEmail());
            publisher.publishEvent(new ResetPasswordCompleteEvent(user, applicationUrl(request)));
            authResponse.setMessage("Password reset email sent");
            return ResponseEntity.ok(authResponse);
        }
        else {
            authResponse.setMessage("User does not exist");
            return ResponseEntity.badRequest().body(authResponse);
        }
    }

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

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
