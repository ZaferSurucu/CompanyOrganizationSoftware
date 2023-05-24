package com.internship.deltasmartsoftware.service;

import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.repository.UserRepository;
import com.internship.deltasmartsoftware.model.VerificationToken;
import com.internship.deltasmartsoftware.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserService {

    private VerificationTokenRepository verificationTokenRepository;


    UserRepository userRepository;

    public UserService(VerificationTokenRepository tokenRepository, UserRepository userRepository) {
        this.verificationTokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public User getOneUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveOneUser(User user) {
        userRepository.save(user);
    }

    public void saveUserVerificationToken(User theUser, String token) {
        VerificationToken verificationToken = new VerificationToken(token, theUser);
        verificationTokenRepository.save(verificationToken);
    }

    public String validateToken(String theToken) {
        VerificationToken token = verificationTokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            verificationTokenRepository.delete(token);
            return "Token already expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }
}
