package com.internship.deltasmartsoftware.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    Logger logger = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = getTokenFromRequest(request);
            if(token == null){
                logger.info("Token is null");
                filterChain.doFilter(request, response);
                return;
            }
            logger.info("Token: " + token);
            if(tokenProvider.validateToken(token)) {
                logger.info("Token is valid");
                String email = tokenProvider.getUserEmailFromTokenProvider(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }catch (Exception e) {
            logger.error("Could not set user authentication in security context", e);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization");
        if(authToken != null && authToken.startsWith("Bearer ")) {
            return authToken.substring(7);
        }
        return null;
    }
}
