package com.internship.deltasmartsoftware.service;

import com.internship.deltasmartsoftware.exceptions.ResourceNotFoundException;
import com.internship.deltasmartsoftware.exceptions.TokenExpiredException;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.repository.UserRepository;
import com.internship.deltasmartsoftware.model.VerificationToken;
import com.internship.deltasmartsoftware.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class VerificationTokenService {

    private VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository tokenRepository) {
        this.verificationTokenRepository = tokenRepository;
    }

    public void saveUserVerificationToken(User theUser, String token) {
        VerificationToken verificationToken = new VerificationToken(token, theUser);
        verificationTokenRepository.save(verificationToken);
    }

    public VerificationToken validateToken(String theToken) throws ResourceNotFoundException, TokenExpiredException{
        try{
            VerificationToken token = findByToken(theToken);
            Calendar calendar = Calendar.getInstance();
            if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
                verificationTokenRepository.delete(token);
                throw new TokenExpiredException("Token already expired");
            }
            return token;
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Token not found");
        }
    }

    public VerificationToken findByToken(String token){
        return verificationTokenRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }
}
