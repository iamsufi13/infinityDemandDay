package com.contenttree.admin;

import com.contenttree.Jwt.JwtResponse;
import com.contenttree.security.JwtHelper;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController
{
    @Autowired
    JwtHelper jwtHelper;

    @Autowired
    AdminService adminService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestParam String email,
                                                @RequestParam String password,
                                                @RequestParam String name,
                                                @RequestParam int isSuperAdmin){
        Admin admin = new Admin();
        admin.setName(name);
        admin.setEmail(email);
        String hashcode = passwordEncoder.encode(password);
        admin.setPassword(hashcode);
        List<Role> roles = new ArrayList<>();
        if (isSuperAdmin ==1){
            roles.add(Role.SUPERADMIN);
        } else if (isSuperAdmin==2) {
            roles.add(Role.EDITOR);
        }
        else {roles.add(Role.ADMIN);}

        admin.setRole(roles);
        adminService.registerAdmin(admin);
        return ResponseEntity.ok("Admin Registered SuccessFully");

    }
    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(@RequestParam String email,@RequestParam String password){
        Admin admin = adminService.getAdminByEmailId(email);

        if (admin == null){
            ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null,"Admin Not Found",false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null, "Email & Password Does Not Match", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        String token = this.jwtHelper.generateTokenAdmins(admin);
        JwtResponse jwtResponse = JwtResponse.builder().jwtToken(token).username(admin.getEmail()).build();
        HashMap<String, Object> response = new HashMap<>();
        response.put("jwtToken", jwtResponse);
        System.out.println("Admin Logged in");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
