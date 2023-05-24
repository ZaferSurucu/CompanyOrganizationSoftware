package com.internship.deltasmartsoftware.service.authService;

import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.requests.UserRequest;
import com.internship.deltasmartsoftware.responses.AuthResponse;
import com.internship.deltasmartsoftware.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class SetNewPasswordService {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public SetNewPasswordService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<AuthResponse> setNewPassword(@RequestBody UserRequest userRequest) {
        AuthResponse authResponse = new AuthResponse();
        User user = userService.getOneUserByEmail(userRequest.getEmail());
        if(user != null){
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            userService.saveOneUser(user);
            authResponse.setMessage("Password reset");
            return ResponseEntity.ok(authResponse);
        }
        else {
            authResponse.setMessage("User does not exist");
            return ResponseEntity.badRequest().body(authResponse);
        }
    }
}
