package com.contenttree.vendor;

import com.contenttree.Jwt.JwtResponse;
import com.contenttree.admin.AdminService;
import com.contenttree.category.Category;
import com.contenttree.category.CategoryService;
import com.contenttree.security.VendorJwtHelper;
import com.contenttree.solutionsets.*;
import com.contenttree.user.UserService;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    @Autowired
    VendorJwtHelper helper;

    @Autowired
    VendorsService vendorsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AdminService adminService;

    @Autowired
    UserService userService;

    @Autowired
    SolutionSetsService solutionSetsService;

    @Autowired
    CategoryService categoryService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,
                                   @RequestParam String password) {
        Vendors vendors = vendorsService.getVendorsByEmail(email);

        if (vendors == null) {
            ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null, "Vendor not found", false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (vendors.getStatus() == VendorStatus.PENDING) {
            ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null, "Vendor not Approved", false);
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }
        if (vendors.getStatus() == VendorStatus.REJECTED) {
            ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null, "Vendor Rejected", false);
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }

        if (!passwordEncoder.matches(password, vendors.getPassword())) {
            ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null, "Email & Password Does Not Match", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        String token = this.helper.generateToken(vendors);

        JwtResponse jwtResponse = JwtResponse.builder().jwtToken(token).username(vendors.getEmail()).build();

        HashMap<String, Object> response = new HashMap<>();
        response.put("jwtToken", jwtResponse);
        System.out.println("Vendor Logged in");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerVendor(@RequestParam String email,
                                                  @RequestParam String password,
                                                 @RequestParam String name) {
        Vendors vendors = new Vendors();
        vendors.setName(name);
        String hashPassword = passwordEncoder.encode(password);
        vendors.setPassword(hashPassword);
        vendors.setEmail(email);

        if (adminService.getAdminByEmailId(email) == null && userService.getUserByEmail(email) == null) {
            vendorsService.registerVendors(vendors);
            return ResponseEntity.ok("Vendor Registered Successfully. Waiting for Approval From Central Team");
        }

        return ResponseEntity.ok("Email Already Registered. Please Try Again");
    }
    @PostMapping("/register123")
    public ResponseEntity<String> registerVendor(@RequestBody VendorDto vendorDTO) {
        String email = vendorDTO.getEmail();
        String password = vendorDTO.getPassword();
        String name = vendorDTO.getName();

        Vendors vendors = new Vendors();
        vendors.setName(name);
        String hashPassword = passwordEncoder.encode(password);
        vendors.setPassword(hashPassword);
        vendors.setEmail(email);

        if (adminService.getAdminByEmailId(email) == null && userService.getUserByEmail(email) == null) {
            vendorsService.registerVendors(vendors);
            return ResponseEntity.ok("Vendor Registered Successfully. Waiting for Approval From Central Team");
        }

        return ResponseEntity.ok("Email Already Registered. Please Try Again");
    }


    @GetMapping("/byid")
    public ResponseEntity<ApiResponse1<SolutionSetDto>> getVendorById(@RequestParam long id) {
        SolutionSets solutionSets = solutionSetsService.getById(id);
        SolutionSetDto solutionSetDto = SolutionSetMapper.toSolutionSetDto(solutionSets); // Map to DTO
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(solutionSetDto, "SUCCESS", true));
    }

    @PostMapping("/add-solutionset")
    public ResponseEntity<ApiResponse1<SolutionSets>> addSolutionSet(@RequestParam MultipartFile file, @RequestParam String category) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(401).body(null);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Vendors vendors = vendorsService.getVendorsByEmail(userDetails.getUsername());

        if (vendors == null) {
            return ResponseEntity.notFound().build();
        }


        Category categoryEntity = categoryService.getCategoryByName(category);
        if (categoryEntity == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtils.createResponse1(null, "Invalid category name", false));
        }

        solutionSetsService.uploadSolutionSets(file, vendors.getId(), categoryEntity.getId());

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "SUCCESS", true));
    }

    @GetMapping("/solutionsets-by-vendor")
    public ResponseEntity<ApiResponse1<List<SolutionSetDto>>> getSolutionSetsByVendor(@RequestParam long vendorId) {
        List<SolutionSets> list = solutionSetsService.getSolutionSetsByVendorId(vendorId);
        List<SolutionSetDto> dtoList = list.stream().map(SolutionSetMapper::toSolutionSetDto).toList();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(dtoList, "SUCCESS", true));
    }
}
