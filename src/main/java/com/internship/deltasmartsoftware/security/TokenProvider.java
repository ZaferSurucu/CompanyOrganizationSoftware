package com.internship.deltasmartsoftware.security;

import com.internship.deltasmartsoftware.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.Logger;

@Component
public class TokenProvider {

    Logger log = Logger.getLogger(TokenProvider.class.getName());

    @Value("${jwt.signing.key.secret}")
    private String APP_SECRET;

    @Value("${jwt.token.expiration.in.seconds}")
    private long EXPIRES_IN;

    public String generateToken(Authentication authentication){
        User userDetails = (User) authentication.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + EXPIRES_IN);
        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, APP_SECRET)
                .compact();
    }
    String getUserEmailFromTokenProvider(String token){
        Claims claims =  Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    boolean validateToken(String authToken){
        try{
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(authToken);
            return !isTokenExpired(authToken);
        }catch (ExpiredJwtException exception) {
            log.info("Request to parse expired JWT : " +authToken+ " failed : " + exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.info("Request to parse unsupported JWT " +authToken+ " failed : " + exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.info("Request to parse invalid JWT : " +authToken+ " failed : " + exception.getMessage());
        } catch (SignatureException exception) {
            log.info("Request to parse JWT with invalid signature : " +authToken+ " failed : " + exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.info("Request to parse empty or null JWT : " +authToken+ " failed : " + exception.getMessage());
        }
        return false;
    }

    private boolean isTokenExpired(String token){
        Date expireDate = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token).getBody().getExpiration();
        return expireDate.before(new Date());
    }
}
