package com.contenttree.vendor;

import com.contenttree.Jwt.JwtResponse;
import com.contenttree.security.JwtHelper;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class VendorController {

    @Autowired
    JwtHelper helper;
    @Autowired
    VendorsService vendorsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestParam String email,
//                                   @RequestParam String password){
//        Vendors vendors = vendorsService.getVendorsByEmail(email);
//        if (!password.equals(vendors.getPassword())){
//            ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null,"Email & Password Does Not Match", false);
//            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//        }
//        if (!passwordEncoder.matches(password, vendors.getPassword())){
//            ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null,"Email & Password Does Not Match", false);
//            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
//        }
//        String token = this.helper.generateTokenVendors(vendors);
//
//        JwtResponse jwtResponse = JwtResponse.builder().jwtToken(token).username(vendors.getEmail()).build();
//
//        HashMap response = new HashMap();
//        response.put("jwtToken", jwtResponse);
//        System.out.println("Vendor Logged in");
//        return new ResponseEntity<>(response,HttpStatus.OK);
//    }
@PostMapping("/login")
public ResponseEntity<?> login(@RequestParam String email,
                               @RequestParam String password) {
    Vendors vendors = vendorsService.getVendorsByEmail(email);

    if (vendors == null) {
        ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null, "Vendor not found", false);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    System.out.println("*******************************");
    System.out.println(vendors.getPassword());
    System.out.println("*******************************");
    if (!passwordEncoder.matches(password, vendors.getPassword())) {
        ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null, "Email & Password Does Not Match", false);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // Use 401 for unauthorized
    }

    // Generate JWT token
    String token = this.helper.generateTokenVendors(vendors);

    JwtResponse jwtResponse = JwtResponse.builder().jwtToken(token).username(vendors.getEmail()).build();

    HashMap<String, Object> response = new HashMap<>();
    response.put("jwtToken", jwtResponse);
    System.out.println("Vendor Logged in");
    return new ResponseEntity<>(response, HttpStatus.OK);
}

    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestParam String email,
                                                @RequestParam String password,
                                                @RequestParam String name){
        Vendors vendors = new Vendors();
        vendors.setName(name);
        String hashpassword = passwordEncoder.encode(password);
        vendors.setPassword(hashpassword);
        vendors.setEmail(email);


        vendorsService.registerVendors(vendors);
        return ResponseEntity.ok("Admin Registered SuccessFully");
    }
}
