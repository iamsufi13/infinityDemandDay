package com.contenttree.security;

import com.contenttree.user.User;
import com.contenttree.vendor.Vendors;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserJwtHelper extends JwtHelper{

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", List.of("ROLE_VENDOR")); // Include vendor roles
        return doGenerateToken(claims, user.getEmail());
    }
}
