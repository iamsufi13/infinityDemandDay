//package com.contenttree.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//    private final JwtHelper jwtHelper;
//    private final UserDetailsService userDetailsService;
//    private static final String TOKEN_PREFIX = "Bearer ";
//
//    public JwtAuthenticationFilter(JwtHelper jwtHelper, UserDetailsService userDetailsService) {
//        this.jwtHelper = jwtHelper;
//        this.userDetailsService = userDetailsService;
//    }
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter(jwtHelper, userDetailsService);
//    }
//
//
////
//@Override
//protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//        throws ServletException, IOException {
//
//    String requestHeader = request.getHeader("Authorization");
//    if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
//        String token = requestHeader.substring(7); // Remove "Bearer " prefix
//        try {
//            String username = jwtHelper.getUsernameFromToken(token);
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//                if (jwtHelper.validateToken(token, userDetails)) {
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                            userDetails, null, userDetails.getAuthorities());
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
//        } catch (Exception e) {
//            logger.error("JWT token validation failed: ", e);
//        }
//    }
//    filterChain.doFilter(request, response);
//}
//
//}

package com.contenttree.security;

import com.contenttree.config.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private static final String TOKEN_PREFIX = "Bearer ";

    public JwtAuthenticationFilter(JwtHelper jwtHelper, UserDetailsService userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtHelper, userDetailsService);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestHeader = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", requestHeader); // Log the incoming Authorization header

        if (requestHeader != null && requestHeader.startsWith(TOKEN_PREFIX)) {
            String token = requestHeader.substring(TOKEN_PREFIX.length()); // Remove "Bearer " prefix
            logger.debug("Extracted Token: {}", token); // Log the extracted token

            try {
                String username = jwtHelper.getUsernameFromToken(token);
                logger.debug("Extracted Username: {}", username); // Log the username extracted from the token

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    logger.debug("Loaded UserDetails: {}", userDetails); // Log the loaded UserDetails

                    if (jwtHelper.validateToken(token, userDetails)) {
                        logger.debug("Token is valid for user: {}", username); // Log successful token validation
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        logger.warn("Invalid JWT token for user: {}", username); // Log invalid token
                    }
                } else {
                    logger.warn("Username not found in token or already authenticated"); // Log if username is not found
                }
            } catch (Exception e) {
                logger.error("JWT token validation failed: ", e); // Log any exception during token validation
            }
        } else {
            logger.warn("No JWT token found in the request"); // Log if no token is found
        }

        filterChain.doFilter(request, response);
    }
}

