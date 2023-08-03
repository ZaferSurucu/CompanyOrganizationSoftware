package com.internship.deltasmartsoftware.service;

import com.internship.deltasmartsoftware.events.model.RegistrationCompleteEvent;
import com.internship.deltasmartsoftware.events.model.ResetPasswordCompleteEvent;
import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.exceptions.TokenExpiredException;
import com.internship.deltasmartsoftware.model.OneSignalPushNotification;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.model.VerificationToken;
import com.internship.deltasmartsoftware.payload.requests.EmailRequest;
import com.internship.deltasmartsoftware.payload.requests.SetNewPasswordRequest;
import com.internship.deltasmartsoftware.payload.responses.Response;
import com.internship.deltasmartsoftware.payload.responses.UserDTO;
import com.internship.deltasmartsoftware.payload.requests.LoginRequest;
import com.internship.deltasmartsoftware.repository.OneSignalPushNotificationRepository;
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
public class AuthControllerService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final ApplicationEventPublisher publisher;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService tokenService;
    private final OneSignalPushNotificationRepository oneSignalPushNotificationRepository;

    public AuthControllerService(AuthenticationManager authenticationManager, TokenProvider tokenProvider, ApplicationEventPublisher publisher, UserService userService, PasswordEncoder passwordEncoder, VerificationTokenService tokenService, OneSignalPushNotificationRepository oneSignalPushNotificationRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.publisher = publisher;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.oneSignalPushNotificationRepository = oneSignalPushNotificationRepository;
    }

    public ResponseEntity<Response<Object>> login(LoginRequest loginRequest,String oneSignalId) {
        try{
            User user = userService.findByEmail(loginRequest.getEmail());
            if(oneSignalPushNotificationRepository.findByOneSignalId(oneSignalId).isEmpty()){
                oneSignalPushNotificationRepository.save(new OneSignalPushNotification(user.getId(),oneSignalId));
            }
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwtToken = tokenProvider.generateToken(auth);
            UserDTO userDTO = new UserDTO(user);
            Map<String,Object> data = new HashMap<>();
            data.put("accessToken", "Bearer " + jwtToken);
            data.put("user", userDTO);
            return Response.ok("auth.loginSuccess", data);
        } catch (ResourceNotFoundException e) {
            return Response.badRequest(e.getMessage());
        }
    }

    public ResponseEntity<Response<Object>> forgotPassword(EmailRequest emailRequest, HttpServletRequest request) {
        try {
            User user = userService.findByEmail(emailRequest.getEmail());
            if (user.getEnabled()) {
                publisher.publishEvent(new ResetPasswordCompleteEvent(user, applicationUrl(request)));
                return Response.ok("auth.resetPasswordMailSend", null);
            } else {
                return Response.badRequest("auth.userNotActivated");
            }
        } catch (ResourceNotFoundException e) {
            return Response.notFound(e.getMessage());
        }
    }

    public ResponseEntity<Response<Object>> activateAccount(EmailRequest emailRequest, HttpServletRequest request) {
        try{
            User user = userService.findByEmail(emailRequest.getEmail());
            if(user.getEnabled()) {
                return Response.badRequest("auth.userAlreadyActivated");
            } else {
                publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
                return Response.ok("auth.activationMailSend",null);
            }
        } catch (ResourceNotFoundException e) {
            return Response.notFound(e.getMessage());
        }
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

    public ResponseEntity<Response<Object>> setNewPassword(SetNewPasswordRequest setNewPasswordRequest) {
        try{
            VerificationToken theToken = tokenService.validateToken(setNewPasswordRequest.getToken());
            User user = theToken.getUser();
            user.setPassword(passwordEncoder.encode(setNewPasswordRequest.getPassword()));
            user.setEnabled(true);
            userService.save(user);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getEmail(), setNewPasswordRequest.getPassword());
            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return Response.ok("auth.passwordReset", null);
        } catch (ResourceNotFoundException e) {
            return Response.badRequest("auth.userNotFound");
        } catch (TokenExpiredException e) {
            return Response.badRequest("auth.resetPasswordTokenExpired");
        } catch (IllegalArgumentException e) {
            return Response.badRequest("users.userNotSaved");
        }
    }

    public ResponseEntity<Response<Object>> verifyResetPasswordEmailToken(String token){
        try{
            tokenService.validateToken(token);
            return Response.ok("auth.resetPasswordTokenValid", null);
        } catch (ResourceNotFoundException e) {
            return Response.badRequest("auth.resetPasswordTokenInvalid");
        } catch (TokenExpiredException e) {
            return Response.badRequest("auth.tokenExpired");
        }
    }

    public ResponseEntity<Response<Object>> verifyActivationEmailToken(String token){
        try{
            VerificationToken theToken = tokenService.validateToken(token);
            if (theToken.getUser().getEnabled()){
                return Response.badRequest("auth.userAlreadyActivated");
            }
            return Response.ok("auth.activationTokenValid", null);
        } catch (ResourceNotFoundException e) {
            return Response.badRequest("auth.activationTokenInvalid");
        } catch (TokenExpiredException e) {
            return Response.badRequest("auth.tokenExpired");
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
