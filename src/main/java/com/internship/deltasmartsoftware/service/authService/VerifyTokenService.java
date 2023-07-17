package com.internship.deltasmartsoftware.service.authService;

import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.model.VerificationToken;
import com.internship.deltasmartsoftware.repository.VerificationTokenRepository;
import com.internship.deltasmartsoftware.responses.AuthResponse;
import com.internship.deltasmartsoftware.service.AuthUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class VerifyTokenService {

    private VerificationTokenRepository tokenRepository;
    private AuthUserService userService;

    public VerifyTokenService(VerificationTokenRepository tokenRepository, AuthUserService userService) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }

    public ResponseEntity<AuthResponse> verifyResetPasswordEmailToken(String token){
        AuthResponse authResponse = new AuthResponse();
        VerificationToken theToken = tokenRepository.findByToken(token);
        String verificationResult = userService.validateToken(token);
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
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().getEnabled()){
            authResponse.setMessage("This account has already been verified, please, login.");
            return ResponseEntity.badRequest().body(authResponse);
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            User user = theToken.getUser();
            user.setEnabled(true);
            userService.saveOneUser(user);
            authResponse.setMessage("Verification successful, please, login.");
            authResponse.setEmail(user.getEmail());
            return ResponseEntity.ok(authResponse);
        }
        authResponse.setMessage("Invalid verification token");
        return ResponseEntity.badRequest().body(authResponse);
    }
}
