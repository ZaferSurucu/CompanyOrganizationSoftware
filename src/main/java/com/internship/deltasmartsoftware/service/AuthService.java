package com.internship.deltasmartsoftware.service;

import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.events.model.RegistrationCompleteEvent;
import com.internship.deltasmartsoftware.events.model.ResetPasswordCompleteEvent;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.model.VerificationToken;
import com.internship.deltasmartsoftware.payload.responses.Response;
import com.internship.deltasmartsoftware.repository.UserRepository;
import com.internship.deltasmartsoftware.payload.requests.LoginRequest;
import com.internship.deltasmartsoftware.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.passay.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final ApplicationEventPublisher publisher;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService tokenService;

    public AuthService(AuthenticationManager authenticationManager, TokenProvider tokenProvider, ApplicationEventPublisher publisher, UserRepository userRepository, PasswordEncoder passwordEncoder, VerificationTokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.publisher = publisher;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public ResponseEntity<Response<Object>> login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = tokenProvider.generateToken(auth);
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("users.userNotFound"));
        Map<String,Object> data = new HashMap<>();
        data.put("accessToken", "Bearer " + jwtToken);
        data.put("user", user);
        return Response.ok("auth.loginSuccess", data);
    }

    public ResponseEntity<Response<User>> forgotPassword(LoginRequest loginRequest, HttpServletRequest request) {
        if(userRepository.findByEmail(loginRequest.getEmail()).isPresent()) {
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("users.userNotFound"));
            publisher.publishEvent(new ResetPasswordCompleteEvent(user, applicationUrl(request)));
            return Response.ok("auth.resetPasswordMailSend",user);
        }
        else {
            return Response.badRequest("auth.userNotFound");
        }
    }

    public ResponseEntity<Response<User>> activateAccount(LoginRequest loginRequest, HttpServletRequest request) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("users.userNotFound"));
        if(user != null) {
            if(user.getEnabled()) {
                return Response.badRequest("auth.userAlreadyActivated");
            } else {
                publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
                return Response.ok("auth.activationMailSend",user);
            }
        }
        else {
            return Response.badRequest("auth.userNotFound");
        }
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

    public ResponseEntity<Response<Object>> setNewPassword(LoginRequest loginRequest, HttpServletRequest request) {
        Map<String,Object> data = new HashMap<>();
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new ResourceNotFoundException("users.userNotFound"));
        if(!validatePassword(loginRequest.getPassword())){
            return Response.badRequest("auth.passwordNotValid");
        }

        if(user != null){
            user.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
            userRepository.save(user);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwtToken = tokenProvider.generateToken(auth);
            data.put("accessToken", "Bearer " + jwtToken);
            return Response.ok("auth.passwordReset", data);
        }
        else {
            return Response.badRequest("auth.userNotFound");
        }
    }

    private boolean validatePassword(String password) {
        List<Rule> rules = new ArrayList<>();
        rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
        rules.add(new CharacterRule(EnglishCharacterData.Special, 1));
        rules.add(new LengthRule(8, 32));
        rules.add(new WhitespaceRule());

        PasswordValidator validator = new PasswordValidator(rules);
        RuleResult result = validator.validate(new PasswordData(password));
        return result.isValid();
    }

    public ResponseEntity<Response<User>> verifyResetPasswordEmailToken(String token, HttpServletRequest request){
        VerificationToken theToken = tokenService.findByToken(token);
        String verificationResult = tokenService.validateToken(token);
        User user = theToken.getUser();
        if (verificationResult.equalsIgnoreCase("valid")){
            return Response.ok("auth.resetPasswordTokenValid", user);
        }
        return Response.badRequest("auth.resetPasswordTokenInvalid");
    }

    public ResponseEntity<Response<User>> verifyActivationEmailToken(String token, HttpServletRequest request){
        VerificationToken theToken = tokenService.findByToken(token);
        User user = theToken.getUser();
        if (theToken.getUser().getEnabled()){
            return Response.badRequest("auth.userAlreadyActivated");
        }
        String verificationResult = tokenService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            user.setEnabled(true);
            userRepository.save(user);
            return Response.ok("auth.accountActivated", user);
        }
        return Response.badRequest("auth.activationTokenInvalid");
    }

}
