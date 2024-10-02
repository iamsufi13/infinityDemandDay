package com.contenttree.vendor;

import com.contenttree.Jwt.JwtResponse;
import com.contenttree.security.JwtHelper;
import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.solutionsets.SolutionSetsController;
import com.contenttree.solutionsets.SolutionSetsService;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.aspectj.weaver.Iterators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class VendorController {

    @Autowired
    JwtHelper helper;
    @Autowired
    VendorsService vendorsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    SolutionSetsService solutionSetsService;
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
    @GetMapping("/vendor/byid")
    public ResponseEntity<ApiResponse1<SolutionSets>> getVendorById(@RequestParam long id){
        SolutionSets solutionSets = solutionSetsService.getById(id);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(solutionSets,"SUCESS",true));
    }
    @PostMapping("/vendor/add-solutionset")
    public ResponseEntity<ApiResponse1<SolutionSets>> addSolutionSet(@RequestParam String name, @RequestParam MultipartFile file){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null || !(authentication.getPrincipal() instanceof UserDetails)){
            return ResponseEntity.status(401).body(null);
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Vendors vendors = vendorsService.getVendorsByEmail(userDetails.getUsername());

        if (vendors==null) {
        return ResponseEntity.notFound().build();
        }
        solutionSetsService.uploadSolutionSets(file,vendors.getId());

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"SUCCESS",true));
        }
    @GetMapping("/vendor/solutionsets-by-vendors")
    public ResponseEntity<ApiResponse1<List<SolutionSets>>> getSolutionSetsByVendor(@RequestParam long vendorId){
    List<SolutionSets> list = solutionSetsService.getSolutionSetsByVendorId(vendorId);
    return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS",true));
    }


    }

