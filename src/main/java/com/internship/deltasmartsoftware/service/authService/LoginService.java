package com.internship.deltasmartsoftware.service.authService;

import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.requests.UserRequest;
import com.internship.deltasmartsoftware.responses.AuthResponse;
import com.internship.deltasmartsoftware.security.TokenProvider;
import com.internship.deltasmartsoftware.service.UserService;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(LoginService.class);
    private AuthenticationManager authenticationManager;
    private TokenProvider tokenProvider;
    private UserService userService;

    public LoginService(AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    public AuthResponse login(UserRequest loginRequest) {
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
}
