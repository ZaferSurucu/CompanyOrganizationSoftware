package com.internship.deltasmartsoftware.service;

import com.internship.deltasmartsoftware.events.model.RegistrationCompleteEvent;
import com.internship.deltasmartsoftware.events.model.ResetPasswordCompleteEvent;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.model.VerificationToken;
import com.internship.deltasmartsoftware.repository.UserRepository;
import com.internship.deltasmartsoftware.requests.LoginRequest;
import com.internship.deltasmartsoftware.responses.AuthResponse;
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
import java.util.List;

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

    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = tokenProvider.generateToken(auth);
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken("Bearer " + jwtToken);
        authResponse.setUserId(user.getId());
        return ResponseEntity.ok(authResponse);
    }

    public ResponseEntity<AuthResponse> forgotPassword(LoginRequest loginRequest, HttpServletRequest request) {
        AuthResponse authResponse = new AuthResponse();
        if(userRepository.findByEmail(loginRequest.getEmail()).isPresent()) {
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
            publisher.publishEvent(new ResetPasswordCompleteEvent(user, applicationUrl(request)));
            authResponse.setMessage("Password reset email sent");
            return ResponseEntity.ok(authResponse);
        }
        else {
            authResponse.setMessage("User does not exist");
            return ResponseEntity.badRequest().body(authResponse);
        }
    }

    public ResponseEntity<AuthResponse> activateAccount(LoginRequest loginRequest, HttpServletRequest request) {
        AuthResponse authResponse = new AuthResponse();
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        if(user != null) {
            if(user.getEnabled()) {
                authResponse.setMessage("User already activated");
                return ResponseEntity.badRequest().body(authResponse);
            } else {
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

    public ResponseEntity<AuthResponse> setNewPassword(LoginRequest loginRequest) {
        AuthResponse authResponse = new AuthResponse();
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        if(!validatePassword(loginRequest.getPassword())){
            authResponse.setMessage("bad password");
            return ResponseEntity.badRequest().body(authResponse);
        }

        if(user != null){
            user.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
            userRepository.save(user);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwtToken = tokenProvider.generateToken(auth);

            authResponse.setMessage("Password reset");
            authResponse.setAccessToken("Bearer " + jwtToken);
            return ResponseEntity.ok(authResponse);
        }
        else {
            authResponse.setMessage("User does not exist");
            return ResponseEntity.badRequest().body(authResponse);
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

    public ResponseEntity<AuthResponse> verifyResetPasswordEmailToken(String token){
        AuthResponse authResponse = new AuthResponse();
        VerificationToken theToken = tokenService.findByToken(token);
        String verificationResult = tokenService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            authResponse.setMessage("Verification successful, please, reset your password.");
            authResponse.setEmail(theToken.getUser().getEmail());
            return ResponseEntity.ok(authResponse);
        }
        authResponse.setMessage("Invalid verification token");
        return ResponseEntity.badRequest().body(authResponse);
    }

    public ResponseEntity<AuthResponse> verifyActivationEmailToken(String token){
        AuthResponse authResponse = new AuthResponse();
        VerificationToken theToken = tokenService.findByToken(token);
        if (theToken.getUser().getEnabled()){
            authResponse.setMessage("This account has already been verified, please, login.");
            return ResponseEntity.badRequest().body(authResponse);
        }
        String verificationResult = tokenService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            User user = theToken.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            authResponse.setMessage("Verification successful, please, login.");
            authResponse.setEmail(user.getEmail());
            return ResponseEntity.ok(authResponse);
        }
        authResponse.setMessage("Invalid verification token");
        return ResponseEntity.badRequest().body(authResponse);
    }
}
