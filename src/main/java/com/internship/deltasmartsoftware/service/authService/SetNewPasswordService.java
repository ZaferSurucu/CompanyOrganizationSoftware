package com.internship.deltasmartsoftware.service.authService;

import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.requests.LoginRequest;
import com.internship.deltasmartsoftware.responses.AuthResponse;
import com.internship.deltasmartsoftware.security.TokenProvider;
import com.internship.deltasmartsoftware.service.AuthUserService;
import org.passay.*;
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
public class SetNewPasswordService {

    private AuthUserService userService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private TokenProvider tokenProvider;

    public SetNewPasswordService(AuthUserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public ResponseEntity<AuthResponse> setNewPassword(LoginRequest loginRequest) {
        AuthResponse authResponse = new AuthResponse();
        User user = userService.getOneUserByEmail(loginRequest.getEmail());
        if(!validatePassword(loginRequest.getPassword())){
            authResponse.setMessage("bad password");
            return ResponseEntity.badRequest().body(authResponse);
        }

        if(user != null){
            user.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
            userService.saveOneUser(user);

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
}
