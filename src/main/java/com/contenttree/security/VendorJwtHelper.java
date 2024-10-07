package com.contenttree.security;

import com.contenttree.vendor.Vendors;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VendorJwtHelper extends JwtHelper {

    public String generateToken(Vendors vendors) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", List.of("ROLE_VENDOR")); // Include vendor roles
        return doGenerateToken(claims, vendors.getEmail());
    }

    public boolean validateToken(String token, Vendors userDetails) {
        System.out.println("****************************************");
        System.out.println("in vendor token validation ");
        return super.validateToken(token, userDetails);
    }
}
