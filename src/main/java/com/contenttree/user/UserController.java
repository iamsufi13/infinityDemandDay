package com.contenttree.user;

import com.contenttree.Jwt.JwtResponse;
import com.contenttree.admin.AdminService;
import com.contenttree.downloadlog.DownloadLogService;
import com.contenttree.security.UserJwtHelper;
import com.contenttree.solutionsets.SolutionSetsService;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import com.contenttree.vendor.Vendors;
import com.contenttree.vendor.VendorsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
   private PasswordEncoder passwordEncoder;
    @Autowired
    UserJwtHelper helper;
    @Autowired
    SolutionSetsService solutionSetsService;
    @Autowired
    DownloadLogService downloadLogService;
    @Autowired
    VendorsService vendorsService;
    @Autowired
    AdminService adminService;


    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String name,
                                          @RequestParam String password,
                                          @RequestParam String email) {

        User user = new User();
        user.setName(name);
        String hasCode = passwordEncoder.encode(password);
        user.setPassword(hasCode);
        user.setEmail(email);
        if (adminService.getAdminByEmailId(email)==null){
            if (vendorsService.getVendorsByEmail(email)==null){
                return userService.saveUser(user);
            }
        }
        else {
            return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Cannot Create Account With This Email Id Already Registered",false));
        }


        return ResponseEntity.ok().body(ResponseUtils.createResponse1(user,"Account Created SuccessFully",true));
    }

    @GetMapping("/download-pdf")
    public ResponseEntity<byte[]> downloadSolutionSets(@RequestParam long id,
                                                       @AuthenticationPrincipal User user){
        byte[] pdfData = solutionSetsService.downloadPdf(id);
        if (pdfData==null || pdfData.length ==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        downloadLogService.lodPdfDownloadUser(id, user.getId());
        System.out.println("Downloading Done " + id  + " " + user.getName());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfData);
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
//    @GetMapping("/confirm-account")
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        System.out.println("in the confirm account portal");

        return userService.confirmEmail(confirmationToken);
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUserAccount(@RequestParam String email, @RequestParam String password) {
        User user = userService.getUserByEmail(email);

        log.info("User fetched from service: {}", user);
        log.info("User enable status: {}", user.isEnabled());

        if (user == null) {
            log.info("User not found");
            ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null, "User not found", false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (!user.isEnabled()) {

            log.info("User account not enabled for email: {}", user.getEmail());
            ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null, "Please verify your email first", false);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // 403 Forbidden
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.info("Password mismatch for user: {}", user.getEmail());
            log.info("Password for user: {}", password);
            log.info("Password expected for user: {}", user.getPassword());
            ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null, "Email & Password Do Not Match", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        log.info("Generating token for user {}", user.getEmail());
        String token = this.helper.generateToken(user);
        log.info("Generated token: {}", token);

        JwtResponse jwtResponse = JwtResponse.builder()
                .jwtToken(token)
                .username(user.getEmail())
                .build();

        ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(jwtResponse, "Login Successful", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
