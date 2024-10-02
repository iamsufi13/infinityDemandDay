package com.contenttree.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (requestURI.equals("/api/login") || requestURI.equals("/api/register") ||
                requestURI.equals("/admin/register") || requestURI.equals("/admin/login")) {
            filterChain.doFilter(request, response);
            return;
        }


        String requestHeader = request.getHeader("Authorization");
        logger.info("Authorization Header: {}", requestHeader);

        String username = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith(TOKEN_PREFIX)) {
            token = requestHeader.substring(TOKEN_PREFIX.length()).trim();
            try {
                username = this.jwtHelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException e) {
                logger.error("JWT processing error: ", e);
            }
        } else {
            logger.warn("Invalid Authorization Header.");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (this.jwtHelper.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.warn("Token validation failed.");
            }
        }
        filterChain.doFilter(request, response);
    }
}
