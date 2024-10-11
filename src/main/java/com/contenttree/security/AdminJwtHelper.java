package com.contenttree.security;

import com.contenttree.admin.Admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AdminJwtHelper extends JwtHelper {

    private UserDetails userDetails;


    public String generateToken(Admin admin) {
        Map<String, Object> claims = new HashMap<>();
        log.debug("ADMIN LOADED,{}", admin);
        claims.put("roles", admin.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        log.info("THE USER DETAILS ARE AS FOLLOWS {}",admin.getUsername());
        return doGenerateToken(claims, admin.getUsername());
    }



    public boolean validateToken(String token, Admin userDetails) {
        String username = getUsernameFromToken(token);
        System.out.println("***********************************************");
        log.warn("User info {}", username);
        System.out.println("***********************************************");
        log.debug("Validating token for user: {}", username);
        log.debug("UserDetails Username: {}", userDetails.getUsername());

        boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);

        if (!isValid) {
            log.warn("Token is invalid for user: {}", username);
        }

        return isValid;
    }

}
