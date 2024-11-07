package com.contenttree.user;

import com.contenttree.Jwt.JwtResponse;

import com.contenttree.admin.AdminService;
import com.contenttree.downloadlog.DownloadLogRepository;
import com.contenttree.downloadlog.DownloadLogService;
import com.contenttree.security.UserJwtHelper;
import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.solutionsets.SolutionSetsService;
import com.contenttree.userdatastorage.IpInfo;
import com.contenttree.userdatastorage.UserDataStorage;
import com.contenttree.userdatastorage.UserDataStorageService;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import com.contenttree.vendor.VendorsService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    com.contenttree.userdatastorage.UserDataStorageService userDataStorageService;

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
    @Autowired
    UserRepository userRepository;

    @Autowired
    DownloadLogRepository downloadLogRepository;

    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String name,
                                          @RequestParam String password,
                                          @RequestParam String email) throws MessagingException, IOException {

        System.out.println("+++++++++++++++++++++++");
        System.out.println(email);
        System.out.println(password);
        System.out.println(name);
        System.out.println("+++++++++++++++++++++++");
        User user = new User();
        user.setName(name);
        String hasCode = passwordEncoder.encode(password);
        user.setPassword(hasCode);
        user.setEmail(email);
        if (adminService.getAdminByEmailId(email)==null){
            System.out.println("inside admin by email");
            if (vendorsService.getVendorsByEmail(email)==null){
                System.out.println("inside vendor by email");
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
                                                   @AuthenticationPrincipal User user,
                                                   HttpServletRequest request) throws MessagingException, IOException {

    byte[] pdfData = solutionSetsService.downloadPdf(id);

    if (pdfData == null || pdfData.length == 0) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    String clientIp = getClientIp(request);

    SolutionSets solutionSets = solutionSetsService.getSolutionSetById(id);
    String categoryName = solutionSets.getCategory().getName();

    List<String> oldFavorites = user.getFavorites();
    List<String> updatedFavorites = new ArrayList<>();

    com.contenttree.userdatastorage.IpInfo info = getIpInfo(clientIp);


    if (oldFavorites == null) {
        oldFavorites = new ArrayList<>();
    }

    if (!oldFavorites.contains(categoryName)) {
        updatedFavorites.add(categoryName);
    }

    updatedFavorites.addAll(oldFavorites);

    user.setFavorites(updatedFavorites);

    downloadLogService.logPdfDownload(id, user.getId(),clientIp);
    System.out.println("Downloading Done " + id + " " + user.getName());
    System.out.println(updatedFavorites);
    System.out.println("IP: " + clientIp);

    UserDataStorage userDataStorage = new UserDataStorage();
    userDataStorage.setUser_id(user.getId());
    userDataStorage.setIp(clientIp);
    System.out.println("City name " + info.getCity());
    userDataStorage.setCity(info.getCity() != null ? info.getCity() : "Unknown");
    userDataStorage.setCountry(info.getCountry());
    userDataStorage.setRegion(info.getRegion());
    userDataStorage.setOrg(info.getOrg());
    userDataStorage.setPostal(info.getPostal());

    System.out.println("UserDataStorage " + userDataStorage);


    userDataStorageService.addUserDataStorage(userDataStorage);

    user.setIpAddress(clientIp);
    userService.updateUser(user);

    return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfData);
}

    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        } else {
            ipAddress = ipAddress.split(",")[0].trim();
        }

        return ipAddress;
    }





    private IpInfo getIpInfo(String clientIp) {
        String url = "https://ipinfo.io/" + clientIp + "/json?token=fac558d39300f3";

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<IpInfo> response = restTemplate.getForEntity(url, IpInfo.class);

            if (response.getBody() == null) {
                throw new RuntimeException("IP info response body is null.");
            }

            System.out.println("responseBody " + response.getBody());
            System.out.println("responseBody 1" + response);
            return response.getBody();

        } catch (Exception e) {
            System.out.println("Error while calling ipinfo.io: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve IP info: " + e.getMessage(), e);
        }
    }


    @GetMapping("/favbyid")
    public ResponseEntity<ApiResponse1<List<String>>> getAllFavorites(@RequestParam long id){
        User user = userService.getUserById(id);
        List <String> fav=user.getFavorites();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(fav,"SUCCESS",true));

    }





    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
//    @GetMapping("/confirm-account")
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        System.out.println("in the confirm account portal");

        return userService.confirmEmail(confirmationToken);
    }
    @RequestMapping("/login")
    public ResponseEntity<?> loginUserAccount(@RequestParam String email, @RequestParam String password) {
        System.out.println("+++++++++++++++++++++++");
        System.out.println(email);
        System.out.println(password);
        System.out.println("+++++++++++++++++++++++");
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
