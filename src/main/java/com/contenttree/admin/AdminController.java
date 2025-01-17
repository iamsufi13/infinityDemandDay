package com.contenttree.admin;

import com.contenttree.Blogs.Blogs;
import com.contenttree.Blogs.BlogsCategory;
import com.contenttree.Blogs.BlogsCategoryRepository;
import com.contenttree.Blogs.BlogsRepository;
import com.contenttree.CampaignManager.Campaign;
import com.contenttree.CampaignManager.CampaignReposiotry;
import com.contenttree.NewsLetter.NewsLetter;
import com.contenttree.NewsLetter.NewsLetterRepository;
import com.contenttree.StaticCount.Region;
import com.contenttree.StaticCount.RegionRepository;
import com.contenttree.StaticCount.StaticCount;
import com.contenttree.StaticCount.StaticCountRepository;
import com.contenttree.category.Category;
import com.contenttree.category.CategoryRepository;
import com.contenttree.category.CategoryService;
import com.contenttree.downloadlog.DownloadLog;
import com.contenttree.downloadlog.DownloadLogDto;
import com.contenttree.downloadlog.DownloadLogMapper;
import com.contenttree.downloadlog.DownloadLogRepository;
import com.contenttree.security.AdminJwtHelper;
import com.contenttree.solutionsets.*;
import com.contenttree.user.*;
import com.contenttree.userdatastorage.UserDataStorage;
import com.contenttree.userdatastorage.UserDataStorageRepository;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ApiResponse2;
import com.contenttree.utils.ResponseUtils;
import com.contenttree.vendor.VendorRepository;
import com.contenttree.vendor.VendorStatus;
import com.contenttree.vendor.Vendors;
import com.contenttree.vendor.VendorsService;
import io.swagger.annotations.Api;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Slf4j
//@CrossOrigin(origins = {"http://localhost:3000","https://infiniteb2b.com","https://admin.infiniteb2b.com","*"})
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController
{
    @Autowired
    AdminJwtHelper jwtHelper;
    @Autowired
    RegionRepository regionRepository;
    @Autowired
    StaticCountRepository staticCountRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    SolutionSetsRepository solutionSetsRepository;
    @Autowired
    DownloadLogRepository downloadLogRepository;
    @Autowired
    BlogsRepository blogsRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    BlogsCategoryRepository blogsCategoryRepository;
    @Autowired
    NewsLetterRepository newsLetterRepository;
    @Autowired
    CategoryRepository categoryRepo;
    @Autowired
    AdminService adminService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    VendorsService vendorsService;
    @Autowired
    UserService userService;


    @Autowired
    UserDataStorageRepository userDataStorageRepository;
    @GetMapping("/admin/hello-world")
    public ResponseEntity<String> helloWorld(){
        return ResponseEntity.ok("Hello World in Admin Controller");}

@PostMapping("/register/admin")
public ResponseEntity<Map<String, Object>> registerAdmin(
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam String location,
        @RequestParam long phone,
        @RequestParam String name,
        @RequestParam int isSuperAdmin) {

    if (vendorsService.getVendorsByEmail(email) != null || userService.getUserByEmail(email) != null) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Email Already Registered. Please Try With Another Email");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    Admin admin = new Admin();
    admin.setName(name);
    admin.setEmail(email);
    admin.setLocation(location);
    admin.setPhone(phone);
    admin.setStatus(Status.Active);
    admin.setPassword(passwordEncoder.encode(password));

    List<Role> roles = new ArrayList<>();
    if (isSuperAdmin == 1) {
        roles.add(Role.SUPERADMIN);
    } else if (isSuperAdmin == 2) {
        roles.add(Role.CAMPAIGNMANAGER);
    } else {
        roles.add(Role.ADMIN);
    }
    admin.setRole(roles);

    adminService.registerAdmin(admin);

    String token = jwtHelper.generateToken(admin);

    Map<String, Object> userDetails = new HashMap<>();
    userDetails.put("_id", admin.getId());
    userDetails.put("email", admin.getEmail());
    userDetails.put("first_name", admin.getName());
    userDetails.put("password", "********");
    userDetails.put("confirm_password", "********");

    Map<String, Object> response = new HashMap<>();
    response.put("message", "success");
    response.put("token", token);
    response.put("user", userDetails);

    return ResponseEntity.ok(response);
}
    @PutMapping("/update/admin/{id}")
    public ResponseEntity<Map<String, Object>> updateAdmin(
            @PathVariable Long id,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long phone,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer isSuperAdmin) {

        Admin admin = adminRepository.findById(id).orElse(null);
        if (admin == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Admin not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        if (email != null && !email.equals(admin.getEmail())) {
            if (vendorsService.getVendorsByEmail(email) != null || userService.getUserByEmail(email) != null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Email already registered. Please try with another email.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }
            admin.setEmail(email);
        }

        if (password != null && !password.isEmpty()) {
            admin.setPassword(passwordEncoder.encode(password));
        }

        if (location != null && !location.isEmpty()) {
            admin.setLocation(location);
        }

        if (phone != null) {
            admin.setPhone(phone);
        }

        if (name != null && !name.isEmpty()) {
            admin.setName(name);
        }

        if (isSuperAdmin != null) {
            List<Role> roles = new ArrayList<>();
            if (isSuperAdmin == 1) {
                roles.add(Role.SUPERADMIN);
            } else if (isSuperAdmin == 2) {
                roles.add(Role.CAMPAIGNMANAGER);
            } else {
                roles.add(Role.ADMIN);
            }
            admin.setRole(roles);
        }

        adminRepository.save(admin);

        String token = jwtHelper.generateToken(admin);

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("_id", admin.getId());
        userDetails.put("email", admin.getEmail());
        userDetails.put("first_name", admin.getName());
        userDetails.put("password", "********");
        userDetails.put("confirm_password", "********");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Admin updated successfully");
        response.put("token", token);
        response.put("user", userDetails);

        return ResponseEntity.ok(response);
    }

@PostMapping("/admin/update/status")
public ResponseEntity<ApiResponse1<Admin>> updateStatus(@RequestParam int status,@RequestParam long id){
        Admin admin = adminService.adminRepository.findById(id).orElse(null);
        if (admin!=null){
            if (status==1){
                admin.setStatus(Status.Active);
            }
            else {
                admin.setStatus(Status.Inactive);
            }
            adminService.adminRepository.save(admin);
        }
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(admin,"SUCCESS",true));
}

    @PostMapping("/login/admin")
    public ResponseEntity<Map<String, Object>> adminLogin(
            @RequestParam String email,
            @RequestParam String password) {

        // Retrieve admin based on email
        Admin admin = adminService.getAdminByEmailId(email);

        // Check if admin is null or password does not match
        if (admin == null || !passwordEncoder.matches(password, admin.getPassword())) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Email & Password Do Not Match");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

//        if (!"active".equals(admin.getStatus())) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("message", "Admin account is not active.");
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
//        }
        if ("Inactive".equals(admin.getStatus())) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Admin account is not active.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }


        String token = jwtHelper.generateToken(admin);

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("_id", admin.getId());
        userDetails.put("email", admin.getEmail());
        userDetails.put("first_name", admin.getName());
        List<Role> role = admin.getRole();
        userDetails.put("role", role.get(0));
        userDetails.put("password", "********");
        userDetails.put("confirm_password", "********");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "success");
        response.put("token", token);
        response.put("data", userDetails);

        return ResponseEntity.ok(response);
    }
@Autowired
    UserRepository userRepository;

    @PostMapping("/update-user-status")
    public ResponseEntity<ApiResponse1<User>> updateStatus(@RequestParam long id, @RequestParam int value) throws MessagingException, IOException {
        User user = userService.getUserById(id);
        if (value==1){
            user.setStatus(UserStatus.ACTIVE);
        }
        else
        {
            user.setStatus(UserStatus.INACTIVE);
            System.out.println("setting status as inactive");
        }
//        userService.saveUser(user);
        userRepository.save(user);
        System.out.println("saving user"+ user.getName());
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(user,"SUCCESS",true));
    }




    @PutMapping("/admin/approve-vendor")
    public ResponseEntity<ApiResponse1<String>> approveVendor(@RequestParam long vendorId) throws MessagingException {
        Vendors vendor = vendorRepository.findById(vendorId).orElseThrow
                (()-> new RuntimeException("Vendor not Found"));
        System.out.println(vendor);
        vendor.setStatus(VendorStatus.ACTIVE);
        vendorRepository.save(vendor);
        String emailContent = "<html>"
                + "<body>"
                + "<h2>Vendor Signup Approved</h2>"
                + "<p>Dear " + vendor.getName() + ",</p>"
                + "<p>We regret to inform you that your request for signup as a vendor has been approved. Please feel free to reach out to us if you have any questions or need further clarification.</p>"
                + "<p>Best regards,</p>"
                + "<p>The Vendor Management Team</p>"
                + "</body>"
                + "</html>";

        emailService.sendHtmlEmail(vendor.getEmail(),"Your req for signup as vendor is approved",emailContent);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Vendor approved SuccessFully",true));
    }
    @PutMapping("/admin/active-vendor")
    public ResponseEntity<ApiResponse1<String>> activeVendor(@RequestParam long vendorId){
        Vendors vendor = vendorRepository.findById(vendorId).orElseThrow
                (()-> new RuntimeException("Vendor not Found"));
        vendor.setStatus(VendorStatus.APPROVED);
        vendorRepository.save(vendor);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Vendor active SuccessFully",true));
    }
    @PutMapping("/admin/reject-vendor")
    public ResponseEntity<ApiResponse1<String>> rejectVendor(@RequestParam long vendorId) throws MessagingException {
        Vendors vendor = vendorRepository.findById(vendorId).orElseThrow(()-> new RuntimeException("Vendor Not Found"));
        vendor.setStatus(VendorStatus.REJECTED);
        vendorRepository.save(vendor);
        String emailContent = "<html>"
                + "<body>"
                + "<h2>Vendor Signup Rejected</h2>"
                + "<p>Dear " + vendor.getName() + ",</p>"
                + "<p>We regret to inform you that your request for signup as a vendor has been rejected. Please feel free to reach out to us if you have any questions or need further clarification.</p>"
                + "<p>Best regards,</p>"
                + "<p>The Vendor Management Team</p>"
                + "</body>"
                + "</html>";

        emailService.sendHtmlEmail(vendor.getEmail(),"Your req for signup as vendor is rejected",emailContent);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Vendor Rejected SuccessFully", true));
    }
    @PutMapping("/admin/inactive-vendor")
    public ResponseEntity<ApiResponse1<String>> inactiveVendor(@RequestParam long vendorId){
        Vendors vendor = vendorRepository.findById(vendorId).orElseThrow(()-> new RuntimeException("Vendor Not Found"));
        vendor.setStatus(VendorStatus.REJECTED);
        vendorRepository.save(vendor);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Vendor inactive SuccessFully", true));
    }
    private static final String logoUrl = "https://infiniteb2b.com/static/media/Infinite-b2b-1-scaled.f42a6998e6eac74721e6.png";

    private static final String whitepaperUrl = "https://infiniteb2b.com/";
    @Autowired
    CategoryService categoryService;
    @Autowired
    EmailService emailService;
    @PutMapping("/admin/approve-solutionset")
    public ResponseEntity<ApiResponse1<String>> approveSolution(@RequestParam Long solutionId) throws MessagingException {
        log.info("inside the Admin controller");
        SolutionSets solutionSet = solutionSetsRepository.findById(solutionId)
                .orElseThrow(() -> new RuntimeException("Solution Set not found"));
        solutionSet.setStatus(SolutionSetsStatus.APPROVED);
        solutionSetsRepository.save(solutionSet);
        Optional<Category> categoryEntity = categoryService.getCategoryBySolutionSet(solutionId);
        String baseUrl = "https://infiniteb2b.com";
        String whitepaperUrl = baseUrl + "/category/" + categoryEntity.get().getId();

        List<User> userList = userService.getAllUsers();
        userList = userList.stream().filter(a -> a.getIsSubscriber() == 1&&a.getFavorites().contains(categoryEntity.get().getName())).toList();

        String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
                "<div style=\"text-align: center; margin-bottom: 30px;\">" +
                "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
                "</div>" +
                "<h2 style=\"text-align: center; color: #007bff; font-size: 24px;\">Latest Whitepapers Released! " + solutionSet.getTitle() + "</h2>" +
                "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
                "We are excited to share the latest updates from <strong>InfiniteB2B</strong>! Our new whitepapers on <strong>" + categoryEntity.get().getName() + "</strong> are now available. Explore the latest insights and trends to stay ahead in your field." +
                "</p>" +
                "<div style=\"text-align: center; margin: 30px 0;\">" +
                "<a href=\"" + whitepaperUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">Explore Now</a>" +
                "</div>" +
                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
                "If the button above doesn’t work, copy and paste the following URL into your browser:" +
                "</p>" +
                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
                "<a href=\"" + whitepaperUrl + "\" style=\"color: #007bff; word-wrap: break-word;\">" + whitepaperUrl + "</a>" +
                "</p>" +
                "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
                "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
                "<p>&copy; 2024 InfiniteB2B. All rights reserved.</p>" +
                "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
                "</footer>" +
                "</div>";

        for (User user : userList) {
            emailService.sendHtmlEmail(user.getEmail(),
                    "New WhitePaper Added in your Favorite Category " + categoryEntity.get().getName(),
                    htmlMessage);
        }
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Solution set approved successfully",true));
    }

    @PutMapping("/admin/reject-solutionset")
    public ResponseEntity<ApiResponse1<String>> rejectSolution(@RequestParam Long solutionId) {
        SolutionSets solutionSet = solutionSetsRepository.findById(solutionId)
                .orElseThrow(() -> new RuntimeException("Solution Set not found"));
        solutionSet.setStatus(SolutionSetsStatus.REJECTED);
        solutionSetsRepository.save(solutionSet);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Solution set rejected successfully",true));
    }

    @PostMapping("/admin/newsletter/sharing")
    public ResponseEntity<ApiResponse1<String>> shareNewsLetterOnMail(@RequestParam (required = false) String newsLetterName){
        return null;
    }
    @GetMapping("/admin/getbyemailid")
    public ResponseEntity<ApiResponse1<Admin>> getAdminByEmailId(@RequestParam String adminEmailId){
        Admin admin = adminService.getAdminByEmailId(adminEmailId);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(admin,"SUCCESS",true));
    }
    @GetMapping("/admin/getbyid")
    public ResponseEntity<ApiResponse1<Admin>> getAdminById(@RequestParam long id){
        Admin admin = adminService.adminRepository.findById(id).orElse(null);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(admin,"SUCCESS",true));
    }
    @GetMapping("/api/admin/getsolutionsby-user")
    public ResponseEntity<ApiResponse1<List<?>>> getSolutionsSetByUser(@RequestParam long id){
        List<DownloadLog> list =downloadLogRepository.findAll();
        List<DownloadLog> list1 =list.stream().filter(downloadLog -> downloadLog.getUser().getId()==id).toList();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list1,"SUCCESS",true));
    }
    @GetMapping("/api/admin/getall-solutionset")
    public ResponseEntity<ApiResponse1<List<SolutionSets>>> getAllSolutionSets(){
        List<SolutionSets> list = solutionSetsRepository.findAll();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS",true));
    }
    @GetMapping("/api/admin/getuserby-downloads")
    public ResponseEntity<ApiResponse1<List<?>>> getUserBySolutionSet(@RequestParam long id){
        List<DownloadLog> list = downloadLogRepository.findAll();
        List<DownloadLog> list1 = list.stream().filter(downloadLog -> downloadLog.getPdfId()==id).toList();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list1,"SUCCESS",true));

    }
    @Autowired
    DownloadLogMapper downloadLogMapper;
    @GetMapping("/api/admin/getalluserdownload-data")
    public ResponseEntity<ApiResponse1<List<?>>> getAllUserDownloadData(){
        List<DownloadLog> list = downloadLogRepository.findAll();
        List<DownloadLogDto> list1 = list.stream()
                .map(downloadLogMapper::toDownloadLogDto)
                .collect(Collectors.toList());
        System.out.println(list1);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list1,"SUCCESS",true));
    }
    @GetMapping("/api/admin/getalluserdownload-databyuser-count")
    public ResponseEntity<ApiResponse1<Map<String,Long>>> getAllUserDownloadDataInCount(){
        List<DownloadLog> list = downloadLogRepository.findAll();
        Map<String, Long> userNameCount = list.stream().sorted(Comparator.comparing(downloadLog -> downloadLog.getUser().getName()))
                .collect(Collectors.groupingBy(downloadLog -> downloadLog.getUser().getName(),
                        Collectors.counting()));

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(userNameCount,"SUCCESS",true));
    }
    @GetMapping("/admin/total-whitepaper")
    public ResponseEntity<ApiResponse1<?>> getAllWhitePaperCounts()
    {
        Optional<StaticCount> staticCount = staticCountRepository.findTopByOrderByDt1Desc();
        List<SolutionSets> list = solutionSetsRepository.findAll();
        long count = list.size();
        count += staticCount.get().getWhitePaperCount();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(count,"SUCCESSS",true));
    }
    @GetMapping("/admin/total-whitepaperset")
    public ResponseEntity<ApiResponse1<?>> getAllWhitePaperSetsCounts()
    {
        List<Category> list = categoryRepo.findAll();
        long count = list.size();
        Optional<StaticCount> staticCount = staticCountRepository.findTopByOrderByDt1Desc();
        count+= staticCount.get().getCategoryCount();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(count,"SUCCESSS",true));
    }
    @GetMapping("/admin/total-user")
    public ResponseEntity<ApiResponse1<?>> getAllUsersCount()
    {
        List<User> list = userService.getAllUsers();
        long count = list.size();
        Optional<StaticCount> staticCount = staticCountRepository.findTopByOrderByDt1Desc();
        count+= staticCount.get().getUsersCount();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(count,"SUCCESS",true));
    }
    @GetMapping("/admin/total-vendor")
    public ResponseEntity<ApiResponse1<?>> getAllVendorsCount(){
        List<Vendors> list = vendorRepository.findAll();
        long count = list.size();
        Optional<StaticCount> staticCount = staticCountRepository.findTopByOrderByDt1Desc();
        count+= staticCount.get().getVendorsCount();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(count,"SUCCESS",true));
    }
    @PostMapping("/admin/add-static-count")
    public ResponseEntity<ApiResponse1<?>> addStaticData(@AuthenticationPrincipal Admin admin, @RequestParam long whitepaperCount, @RequestParam long userCount, @RequestParam long categoryCount, @RequestParam long vendorCount){
        StaticCount staticCount = new StaticCount();
        staticCount.setCategoryCount(categoryCount);
        staticCount.setUsersCount(userCount);
        staticCount.setWhitePaperCount(whitepaperCount);
        staticCount.setVendorsCount(vendorCount);
        staticCountRepository.save(staticCount);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(staticCount,"SUCCESS",true));
    }
    @GetMapping("/admin/total-subscribers")
    public ResponseEntity<ApiResponse1<?>> getAllSubscribersCount(){
        List<User> list = userService.getAllUsers();
        Optional<StaticCount> staticCount = staticCountRepository.findTopByOrderByDt1Desc();
        long count = list.stream().filter(user -> user.getIsSubscriber()==1).count();
        long addCount = (long)(staticCount.get().getUsersCount() *0.7) ;
        count+=addCount;
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(count,"SUCCESS",true));
    }
    @GetMapping("/admin/total-unsubscribers")
    public ResponseEntity<ApiResponse1<?>> getAllUnSubscribersCount(){
        List<User> list = userService.getAllUsers();
        long count = list.stream().filter(user -> user.getIsSubscriber()==0).count();
        Optional<StaticCount> staticCount = staticCountRepository.findTopByOrderByDt1Desc();
        long addCount = (long)(staticCount.get().getUsersCount() *0.3) ;
        count+=addCount;
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(count,"SUCCESS",true));
    }
    @GetMapping("/admin/most-users-by-region")
    public ResponseEntity<ApiResponse1<Map<?,?>>> getUsersByRegion(){
        List<User> users = userService.getAllUsers();
        Map<String, Long> map = users.stream()
                .collect(Collectors.groupingBy(
                        user -> Optional.ofNullable(user.getCountry()).orElse("Unknown"),
                        Collectors.counting()
                ));
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(map,"SUCCESS",true));
}
//    @GetMapping("/admin/most-users-by-region1")
//    public ResponseEntity<ApiResponse1<List<Map<String, Object>>>> getUsersByRegion2() {
//    long userCount = (long) Objects.requireNonNull(getAllUsersCount().getBody()).getData();
//
//        List<Region> regions = regionRepository.findAll();
//
//        List<Map<String, Object>> regionUserCounts = regions.stream()
//                .map(region -> {
//                    long usersFromRegion = Math.round((region.getPercent() / 100.0) * userCount);
//
//                    Map<String, Object> regionData = new HashMap<>();
//                    regionData.put("country", region.getCountryName());
//                    regionData.put("userCount", usersFromRegion);
//
//                    return regionData;
//                })
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(regionUserCounts, "SUCCESS", true));
//    }
@GetMapping("/admin/most-users-by-region1")
public ResponseEntity<ApiResponse2> getUsersByRegion2() {
    long userCount = (long) Objects.requireNonNull(getAllUsersCount().getBody()).getData();

    List<Region> regions = regionRepository.findAll();

    List<Map<String, Object>> regionUserCounts = regions.stream()
            .map(region -> {
                long usersFromRegion = Math.round((region.getPercent() / 100.0) * userCount);

                Map<String, Object> regionData = new HashMap<>();
                regionData.put("country", region.getCountryName());
                regionData.put("userCount", usersFromRegion);

                return regionData;
            })
            .collect(Collectors.toList());

    ApiResponse2 response = ResponseUtils.createResponse2(regionUserCounts, "SUCCESS", true);
    return ResponseEntity.ok().body(response);
}



//    @GetMapping("/admin/most-downloaded-whitepaper")
//    public ResponseEntity<ApiResponse1<Map<?,?>>> getMostDownloadedWhitePaperlist(){
//        List<DownloadLog> list = downloadLogRepository.findAll();
//        List<SolutionSets> list1 = solutionSetsRepository.findAll();
//        Map<Long, String> pdfIdToNameMap = list1.stream()
//                .collect(Collectors.toMap(SolutionSets::getId, SolutionSets::getName));
//
//        Map<Long, Long> pdfIdCountMap = list.stream()
//                .collect(Collectors.groupingBy(DownloadLog::getPdfId, Collectors.counting()));
//
//        Map<String, Long> nameCountMap = pdfIdCountMap.entrySet().stream()
//                .sorted(Map.Entry.<Long, Long>comparingByValue(Comparator.reverseOrder()))
//                .collect(Collectors.toMap(
//                        entry -> pdfIdToNameMap.getOrDefault(entry.getKey(), "Unknown"),
//                        Map.Entry::getValue,
//                        (e1, e2) -> e1,
//                        LinkedHashMap::new
//                ));
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(nameCountMap,"SUCCESS",true));
//    }
//@GetMapping("/admin/most-downloaded-whitepaper")
//public ResponseEntity<ApiResponse2> getMostDownloadedWhitePaperList() {
//    List<DownloadLog> downloadLogs = downloadLogRepository.findAll();
//    List<SolutionSets> solutionSets = solutionSetsRepository.findAll();
//
//    Map<Long, String> pdfIdToNameMap = solutionSets.stream()
//            .collect(Collectors.toMap(SolutionSets::getId, SolutionSets::getName));
//
//    Map<Long, Long> pdfDownloadCountMap = downloadLogs.stream()
//            .collect(Collectors.groupingBy(DownloadLog::getPdfId, Collectors.counting()));
//
//    Map<String, Long> sortedNameDownloadCountMap = pdfDownloadCountMap.entrySet().stream()
//            .sorted(Map.Entry.<Long, Long>comparingByValue(Comparator.reverseOrder()))
//            .collect(Collectors.toMap(
//                    entry -> pdfIdToNameMap.getOrDefault(entry.getKey(), "Unknown"),
//                    Map.Entry::getValue,
//                    (existing, replacement) -> existing,
//                    LinkedHashMap::new
//            ));
//
//    List<Map<String, Object>> responseData = sortedNameDownloadCountMap.entrySet().stream()
//            .map(entry -> {
//                Map<String, Object> map = new LinkedHashMap<>();
//                map.put("name", entry.getKey());
//                map.put("downloads", entry.getValue());
//                return map;
//            })
//            .collect(Collectors.toList());
//
//    ApiResponse2 response = new ApiResponse2(true, "SUCCESS", responseData);
//
//    return ResponseEntity.ok(response);
//}

    @GetMapping("/admin/allBlogs")
    public ResponseEntity<ApiResponse1> getAllBlogs() {
        List<Blogs> list = blogsRepository.findAll();

        List<Map<String, Object>> responseData = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


        for (Blogs blog : list) {
            Map<String, Object> map = new LinkedHashMap<>();

            map.put("name", blog.getBlogName());
            map.put("id",blog.getId());
            map.put("blogCategoryId",blog.getBlogsCategory().getId());
            String categoryName = (blog.getBlogsCategory() != null) ? blog.getBlogsCategory().getBlogCategoryName() : "Unknown";
            map.put("category", categoryName);
            map.put("content",blog.getBlogContent());
            map.put("imageContent","https://infiniteb2b.com/var/www/infiniteb2b/springboot/blogs/"+blog.getBlogImage());

            map.put("totalViews", 0);

            if (blog.getDt1() != null) {
                String formattedDate = null;
                try {
                    if (blog.getDt1() instanceof LocalDateTime) {
                        LocalDateTime dateTime = blog.getDt1();
                        formattedDate = dateTime.format(dateFormatter);
                    }
                } catch (Exception e) {
                    formattedDate = "Invalid Date";
                }
                map.put("publishDate", formattedDate);
            } else {
                map.put("publishDate", "Unknown");
            }

            responseData.add(map);
        }

        ApiResponse1 response = new ApiResponse1(true, "SUCCESS", responseData);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/admin/allBlogsCategory")
    public ResponseEntity<ApiResponse1> getAllBlogsCategory(@RequestParam(required = false)String name) {
        List<BlogsCategory> list = blogsCategoryRepository.findAll();
        if (name != null && !name.isEmpty()) {
            list = list.stream()
                    .filter(blog -> blog.getBlogCategoryName() != null && blog.getBlogCategoryName().startsWith(name))
                    .toList();
        }

        List<Map<String, Object>> responseData = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (BlogsCategory blog : list) {
            Map<String, Object> map = new LinkedHashMap<>();

            map.put("name", blog.getBlogCategoryName());
            map.put("blogCategoryId",blog.getId());

            String categoryName = (blog.getBlogCategoryDescp() != null) ? blog.getBlogCategoryDescp(): "Unknown";
            map.put("description", categoryName);

//            map.put("totalViews", blog.getViews() != null ? blog.getViews() : 0);
            map.put("totalBlogsPosted",  blog.getBlogs().stream().count());

            if (blog.getDt1() != null) {
                String formattedDate = null;
                try {
                    LocalDateTime dateTime = blog.getDt1();
                    formattedDate = dateTime.format(dateFormatter);
                } catch (Exception e) {
                    formattedDate = "Invalid Date";
                }
                map.put("publishDate", formattedDate);
            } else {
                map.put("publishDate", "Unknown");
            }

            responseData.add(map);
        }

        ApiResponse1 response = new ApiResponse1(true, "SUCCESS", responseData);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/admin/most-downloaded-whitepaper")
public ResponseEntity<ApiResponse2> getMostDownloadedWhitePaperList() {
    List<DownloadLog> downloadLogs = downloadLogRepository.findAll();
    List<SolutionSets> solutionSets = solutionSetsRepository.findAll();

    Map<Long, String> pdfIdToNameMap = solutionSets.stream()
            .collect(Collectors.toMap(SolutionSets::getId, SolutionSets::getName));

    Map<Long, Long> pdfDownloadCountMap = downloadLogs.stream()
            .collect(Collectors.groupingBy(DownloadLog::getPdfId, Collectors.counting()));

    Map<String, Long> sortedNameDownloadCountMap = pdfDownloadCountMap.entrySet().stream()
            .sorted(Map.Entry.<Long, Long>comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toMap(
                    entry -> pdfIdToNameMap.getOrDefault(entry.getKey(), "Unknown"),
                    Map.Entry::getValue,
                    (existing, replacement) -> existing,
                    LinkedHashMap::new
            ));

    List<Map<String, Object>> responseData = sortedNameDownloadCountMap.entrySet().stream()
            .map(entry -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("whitePaper", entry.getKey());
                map.put("totalDownloads", entry.getValue());

                SolutionSets solutionSet = solutionSets.stream()
                        .filter(s -> s.getName().equals(entry.getKey()))
                        .findFirst()
                        .orElse(null);

                if (solutionSet != null) {
                    map.put("whitePaperSet", solutionSet.getCategory() != null ? solutionSet.getCategory().getName() : "Unknown");
                } else {
                    map.put("whitePaperSet", "Unknown");
                }

                return map;
            })
            .collect(Collectors.toList());

    ApiResponse2 response = new ApiResponse2(true, "SUCCESS", responseData);

    return ResponseEntity.ok(response);
}

    @GetMapping("/admin/allnewsletters")
    public ResponseEntity<ApiResponse1> getAllNewsletters() {
        List<NewsLetter> list = newsLetterRepository.findAll();

        List<Map<String, Object>> responseData = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (NewsLetter newsletter : list) {
            Map<String, Object> map = new LinkedHashMap<>();

            map.put("name", newsletter.getTitle());
            map.put("id",newsletter.getId());

            List<User> userList = userService.getAllUsers();


            map.put("totalViews", 0);
            map.put("content",newsletter.getContent());
            map.put("previewLink",newsletter.getPreviewLink());
            map.put("image","https://infiniteb2b.com/var/www/infiniteb2b/springboot/newsletters/"+newsletter.getImagePath());
            map.put("viewPath","https://infiniteb2b.com/newsletters");

            if (newsletter.getDt1() != null) {
                String formattedDate = null;
                try {
                    if (newsletter.getDt1() instanceof LocalDateTime) {
                        LocalDateTime dateTime = newsletter.getDt1();
                        formattedDate = dateTime.format(dateFormatter);
                    }
                } catch (Exception e) {
                    formattedDate = "Invalid Date";
                }
                map.put("publishDate", formattedDate);
            } else {
                map.put("publishDate", "Unknown");
            }

            responseData.add(map);
        }

        ApiResponse1 response = new ApiResponse1(true, "SUCCESS", responseData);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/admin/get-all-pending-vendors")
    public ResponseEntity<ApiResponse1<List<Vendors>>> getAllPendingVendorList(){
    List<Vendors> list = vendorRepository.findAll();
    List<Vendors> updatedList = list.stream()
            .filter(vendor -> "INACTIVE".equalsIgnoreCase(String.valueOf(vendor.getStatus())))
            .filter(vendors -> vendors.getId()!=1)
            .collect(Collectors.toList());
    return ResponseEntity.ok().body(ResponseUtils.createResponse1(updatedList,"SUCCESS",true));
}
    @GetMapping("/admin/get-all-approved-vendors")
    public ResponseEntity<ApiResponse1<List<Vendors>>> getAllApprovedVendorList(){
        List<Vendors> list = vendorRepository.findAll();
        List<Vendors> updatedList = list.stream()
                .filter(vendor -> "ACTIVE".equalsIgnoreCase(String.valueOf(vendor.getStatus())))
                .filter(vendors -> vendors.getId()!=1)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(updatedList,"SUCCESS",true));
    }
    @GetMapping("/admin/getadmin")
public ResponseEntity<ApiResponse1<List<Admin>>> getAllAdminList() {
    List<Admin> list = adminService.adminRepository.findAll();

    List<Admin> updatedList = list.stream()
            .filter(admin -> admin.getAuthorities().stream()
                    .anyMatch(auth -> "SUPERADMIN".equalsIgnoreCase(auth.getAuthority())))
            .sorted(Comparator.comparing(Admin::getName))
            .collect(Collectors.toList());

    return ResponseEntity.ok().body(ResponseUtils.createResponse1(updatedList, "SUCCESS", true));
}


    @GetMapping("/admin/get-all-admin")
    public ResponseEntity<ApiResponse1<List<Admin>>> getAllAdmin(){
        List<Admin> list = adminService.adminRepository.findAll();

        List<Admin> updatedList = list.stream()
                .filter(admin -> admin.getAuthorities().stream()
                        .anyMatch(auth -> "SUPERADMIN".equalsIgnoreCase(auth.getAuthority())))
                .sorted(Comparator.comparing(Admin::getName))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(updatedList, "SUCCESS", true));
    }
//    @GetMapping("/admin/get-all-campaign-manager")
//    public ResponseEntity<ApiResponse1<List<Admin>>> getAllCampaignManager(){
//        List<Admin> list = adminService.adminRepository.findAll();
//        List<Admin> updatedList = list.stream()
//                .filter(admin -> admin.getAuthorities().stream()
//                        .anyMatch(auth -> "CAMPAIGNMANAGER".equalsIgnoreCase(auth.getAuthority())))
//                .sorted(Comparator.comparing(Admin::getName))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(updatedList,"SUCCESS",true));
//    }
@GetMapping("/admin/get-all-campaign-manager")
public ResponseEntity<ApiResponse1<List<AdminResponseDto>>> getAllCampaignManager(@AuthenticationPrincipal Admin admin1) {
    List<Admin> list = adminService.adminRepository.findAll();
    List<Campaign> campaignList = campaignReposiotry.findAll();
    campaignList.stream().filter(a->a.getAdmin().getId()== admin1.getId());

    List<UserDataStorage> userDataStorages = userDataStorageRepository.findAll();
    long userDatacount = userDataStorages.stream()
            .map(UserDataStorage::getDownload)
            .filter(obj -> true)
            .count();

    List<AdminResponseDto> updatedList = list.stream()
            .filter(admin -> admin.getAuthorities().stream()
                    .anyMatch(auth -> "CAMPAIGNMANAGER".equalsIgnoreCase(auth.getAuthority())))
            .sorted(Comparator.comparing(Admin::getName))
            .map(admin -> {
                AdminResponseDto dto = new AdminResponseDto();
                dto.setId(admin.getId());
                dto.setName(admin.getName());
                dto.setPhone(String.valueOf(admin.getPhone()));
                dto.setLocation(admin.getLocation());
                dto.setEmail(admin.getEmail());
                dto.setEnabled(admin.isEnabled());
                dto.setUsername(admin.getUsername());
                dto.setAuthorities(admin.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));
                dto.setAccountNonLocked(admin.isAccountNonLocked());
                dto.setAccountNonExpired(admin.isAccountNonExpired());
                dto.setCredentialsNonExpired(admin.isCredentialsNonExpired());
                dto.setCampaign(String.valueOf(campaignList.stream().count()));
                dto.setDownloads(String.valueOf(userDatacount));
                dto.setReport("0");
                dto.setBlogs("na");
                dto.setNewsletters("na");
                dto.setWhitepapers("na");
                return dto;
            })
            .collect(Collectors.toList());

    return ResponseEntity.ok().body(ResponseUtils.createResponse1(updatedList, "SUCCESS", true));
}


    @GetMapping("/admin/get-all-editor")
    public ResponseEntity<ApiResponse1<List<AdminResponseDto>>> getAllEditor(@AuthenticationPrincipal Admin admin1){
        List<Admin> list = adminService.adminRepository.findAll();
        List<Blogs> blogs = blogsRepository.findAll();
        blogs.stream().filter(a -> a.getAdmin().getId()== admin1.getId());
        List<NewsLetter> newsLetterList = newsLetterRepository.findAll();
        newsLetterList.stream().filter(a -> a.getAdmin().getId()== admin1.getId());
        List<SolutionSets> solutionSets = solutionSetsRepository.findAll();


        List<AdminResponseDto> updatedList = list.stream()
                .filter(admin -> admin.getAuthorities().stream()
                        .anyMatch(auth -> "ADMIN".equalsIgnoreCase(auth.getAuthority())))
                .sorted(Comparator.comparing(Admin::getName))
                .map(admin -> {
                    AdminResponseDto dto = new AdminResponseDto();
                    dto.setId(admin.getId());
                    dto.setName(admin.getName());
                    dto.setPhone(String.valueOf(admin.getPhone()));
                    dto.setLocation(admin.getLocation());
                    dto.setEmail(admin.getEmail());
                    dto.setEnabled(admin.isEnabled());
                    dto.setUsername(admin.getUsername());
                    dto.setAuthorities(admin.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()));
                    dto.setAccountNonLocked(admin.isAccountNonLocked());
                    dto.setAccountNonExpired(admin.isAccountNonExpired());
                    dto.setCredentialsNonExpired(admin.isCredentialsNonExpired());
                    dto.setCampaign("na");
                    dto.setDownloads("na");
                    dto.setReport("na");
                    dto.setBlogs(String.valueOf(blogs.size()));
                    dto.setNewsletters(String.valueOf(newsLetterList.size()));
                    dto.setWhitepapers(String.valueOf(solutionSets.stream().count()));
                    return dto;
                })
                .collect(Collectors.toList());
        Collections.reverse(updatedList);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(updatedList,"SUCCESS",true));
    }

    @GetMapping("/admin/get-allvendor")
    public ResponseEntity<ApiResponse1<List<Vendors>>> getAllVendorList(){
            List<Vendors> list = vendorRepository.findAll();
            List<Vendors> updatedList = list.stream()
                    .filter(vendor -> "ACTIVE".equalsIgnoreCase(String.valueOf(vendor.getStatus())))
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(ResponseUtils.createResponse1(updatedList,"SUCCESS",true));
        }

    @GetMapping("/admin/get-reviewvendor")
    public ResponseEntity<ApiResponse1<List<Vendors>>> reviewAllVendorList(){
        List<Vendors> list = vendorRepository.findAll();

        List<Vendors> filteredList = list.stream()
                .filter(vendor ->
                        "REJECTED".equalsIgnoreCase(String.valueOf(vendor.getStatus())) ||
                                "PENDING".equalsIgnoreCase(String.valueOf(vendor.getStatus()))
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(filteredList, "SUCCESS", true));
    }

    @GetMapping("/admin/get-alluser")
    public ResponseEntity<ApiResponse1<List<Map<String, Object>>>> getAllUserList() {
        List<User> list = userService.getAllUsers();
        List<Map<String, Object>> result = new ArrayList<>();


        for (User user : list) {
            List<UserDataStorage> userData = userDataStorageRepository.findByUserIdList(user.getId());
            List<String> userSubscribeCount = user.getFavorites()!= null ? user.getFavorites() : new ArrayList<>();
            int totalCount=0;
            for (String favorites : userSubscribeCount) {
                if (favorites != null && !favorites.isEmpty()) {
                    String[] splitFavorites = favorites.split(",");
                    totalCount += splitFavorites.length;
                }
            }

            long totalSaveCount = userData.stream().filter(count-> count.getSave()==1).count();
            long totalDownloadCount = userData.stream().filter(count-> count.getDownload()==1).count();
            long totalViewCount = userData.stream().filter(count-> count.getView()==1).count();

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("user", user);

            if (userData != null) {
                userMap.put("totalSaveCount", totalSaveCount);
                userMap.put("totalDownloadCount", totalDownloadCount);
                userMap.put("totalViewCount", totalViewCount);
                userMap.put("totalCategorySubscribedCount",totalCount);
                userMap.put("newsLetterSubscribed",0);
            } else {
                userMap.put("userData", "No data available for this user");
            }

            result.add(userMap);
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", result);

        ApiResponse1<List<Map<String, Object>>> apiResponse = ResponseUtils.createResponse1(result, "SUCCESS", true);
        return ResponseEntity.ok().body(apiResponse);
    }

    //    @GetMapping("/admin/get-allwhitepapers")
//    public ResponseEntity<ApiResponse1<List<SolutionSetDto>>> getAllWhitePapersList(){
//        List<SolutionSets> list = solutionSetsRepository.findAll();
//        List<SolutionSetDto> solutionSetDtos = list.stream()
//                .map(SolutionSetMapper::toSolutionSetDto)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(solutionSetDtos,"SUCCESS",true));
//    }
    @Autowired
    SolutionSetMapper solutionSetMapper;
//@GetMapping("/admin/get-allwhitepapers")
//public ResponseEntity<ApiResponse1<List<SolutionSetDto>>> getAllWhitePapersList(@RequestParam int value) {
//    try {
//        List<SolutionSets> list = solutionSetsRepository.findAll();
//        System.out.println("Fetched list: " + list.size());
//        list.forEach(System.out::println);
//
//        List<SolutionSetDto> solutionSetDtos = list.stream()
//                .map(solutionSetMapper::toSolutionSetDto)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(solutionSetDtos, "SUCCESS", true));
//    } catch (Exception e) {
//        e.printStackTrace();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ResponseUtils.createResponse1(null, "INTERNAL_SERVER_ERROR", false));
//    }
//}
//@GetMapping("/admin/get-allwhitepapers")
//public ResponseEntity<ApiResponse1<Map<?,?>>> getAllWhitePapersList(@RequestParam(required = false) Integer value) {
//    try {
//        String whitePaperPath = "https://infiniteb2b.com/whitepaper";
//        List<SolutionSets> list = solutionSetsRepository.findAll();
//        System.out.println("Fetched list: " + list.size());
//        list.forEach(System.out::println);
//
//        if (value == 1) {
//            list = list.stream()
//                    .filter(solutionSet -> solutionSet.getUploadedBy() == null)
//                    .collect(Collectors.toList());
//        } else if (value == 3) {
//            list = list.stream()
//                    .filter(solutionSet -> solutionSet.getUploadedBy() != null)
//                    .collect(Collectors.toList());
//        }
//        else if (value == 2) {
//            list = list.stream()
//                    .filter(solutionSet -> "PENDING".equalsIgnoreCase(String.valueOf(solutionSet.getStatus())))
//                    .collect(Collectors.toList());
//        }
//        else if (value == 4) {
//            list = list.stream()
//                    .filter(solutionSet -> "APPROVED".equalsIgnoreCase(String.valueOf(solutionSet.getStatus())))
//                    .collect(Collectors.toList());
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("whitePaperPath", whitePaperPath);
//        map.put("whitepapers", list);
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(map, "SUCCESS", true));
//    } catch (Exception e) {
//        e.printStackTrace();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ResponseUtils.createResponse1(null, "INTERNAL_SERVER_ERROR", false));
//    }
//}
@GetMapping("/admin/get-allwhitepapers")
public ResponseEntity<ApiResponse1<Map<?, ?>>> getAllWhitePapersList(@RequestParam(required = false) Integer value) {
    try {
        String baseWhitePaperPath = "https://infiniteb2b.com/whitepaper";
        List<SolutionSets> list = solutionSetsRepository.findAll();
        System.out.println("Fetched list: " + list.size());
        list.forEach(System.out::println);


        if (value == 1) {
            list = list.stream()
                    .filter(solutionSet -> solutionSet.getUploadedBy() == null)
                    .collect(Collectors.toList());
        } else if (value == 3) {
            list = list.stream()
                    .filter(solutionSet -> solutionSet.getUploadedBy() != null)
                    .collect(Collectors.toList());
        } else if (value == 2) {
            list = list.stream()
                    .filter(solutionSet -> "PENDING".equalsIgnoreCase(String.valueOf(solutionSet.getStatus())))
                    .collect(Collectors.toList());
        } else if (value == 4) {
            list = list.stream()
                    .filter(solutionSet -> "APPROVED".equalsIgnoreCase(String.valueOf(solutionSet.getStatus())))
                    .collect(Collectors.toList());
        }

        List<Map<String, Object>> responseData = new ArrayList<>();
        for (SolutionSets solutionSet : list) {
            Map<String, Object> whitepaperData = new HashMap<>();
            List<UserDataStorage> userDataStorageList = userDataStorageRepository.findBySolutionSetIdList(solutionSet.getId());
            long totalViews=userDataStorageList.stream().filter(userDataStorage -> userDataStorage.getView()==1).count();
            long totalDownloads=userDataStorageList.stream().filter(userDataStorage -> userDataStorage.getDownload()==1).count();
            String whitePaperUrl = baseWhitePaperPath + "/" + solutionSet.getId();
            whitepaperData.put("whitePaperUrl", whitePaperUrl);
            whitepaperData.put("solutionSet", solutionSet);
            whitepaperData.put("categoryName", solutionSet.getCategory().getName());
            whitepaperData.put("publishedDate", solutionSet.getDt1());
            whitepaperData.put("totalDownloads", totalDownloads);
            whitepaperData.put("totalViews", totalViews);
            whitepaperData.put("publishedBy",solutionSet.getUploadedBy().getName());


            responseData.add(whitepaperData);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("whitepapers", responseData);

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(map, "SUCCESS", true));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtils.createResponse1(null, "INTERNAL_SERVER_ERROR", false));
    }
}




    @GetMapping("/get-all-blogs-count")
    public ResponseEntity<ApiResponse1<Long>> getBlogsCount(){
        List<Blogs> list = blogsRepository.findAll();
       long count = list.stream().count();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(count,"SUCCESS",true));
    }
    @Autowired
    CampaignReposiotry campaignReposiotry;
    @GetMapping("/get-all-campaign-count")
    public ResponseEntity<ApiResponse1<Long>> getCampaignCount(){
        List<Campaign> list = campaignReposiotry.findAll();
        long count = list.stream().count();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(count,"SUCCESS",true));
    }

    @GetMapping("/get-all-newsletter-count")
    public ResponseEntity<ApiResponse1<Long>> getNewsLetterCount(){
        List<NewsLetter> list = newsLetterRepository.findAll();
        long count = list.stream().count();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(count,"SUCCESS",true));
    }

    @GetMapping("/get-widgets")
    public ResponseEntity<DashboardWidgetsResponse> getWidgets() {
        List<Widget> ecomWidgets = new ArrayList<>();
        List<Widget> totalecomWidgets = new ArrayList<>();

        long whitepapercount = (long) Objects.requireNonNull(getAllWhitePaperCounts().getBody()).getData();
        long whitepaperSetcount = (long) Objects.requireNonNull(getAllWhitePaperSetsCounts().getBody()).getData();
        long usercount = (long) Objects.requireNonNull(getAllUsersCount().getBody()).getData();
        long vendorscount = (long) Objects.requireNonNull(getAllVendorsCount().getBody()).getData();
        long campaigncount = (long) Objects.requireNonNull(getCampaignCount().getBody()).getData();
        long blogsCount = (long) Objects.requireNonNull(getBlogsCount().getBody()).getData();
        long newslettercount = (long) Objects.requireNonNull(getNewsLetterCount().getBody()).getData();
        long subs = (long) Objects.requireNonNull(getAllSubscribersCount().getBody()).getData();

        ecomWidgets.add(new Widget(1L, "primary", "TOTAL WHITEPAPERS", String.valueOf(whitepapercount), "View all whitepapers", "secondary", "bx bx-file", 0));
        ecomWidgets.add(new Widget(2L, "secondary", "TOTAL WHITEPAPERS SET", String.valueOf(whitepaperSetcount), "View all categories", "primary", "bx bx-book", 0));
        ecomWidgets.add(new Widget(3L, "success", " USERS", String.valueOf(usercount), "View all users", "success", "bx bx-user-circle", 0));
        ecomWidgets.add(new Widget(4L, "info", "TOTAL VENDERS", String.valueOf(vendorscount), "View all venders", "warning", "bx bxs-store", 0));

        totalecomWidgets.add(new Widget(1L, "primary", "TOTAL CAMPAIGINS", String.valueOf(campaigncount), "View all campaigins", "secondary", "bx bxs-megaphone", 0));
        totalecomWidgets.add(new Widget(2L, "secondary", "TOTAL BLOGS", String.valueOf(blogsCount), "View all blogs", "primary", "bx bx-news", 0));
        totalecomWidgets.add(new Widget(3L, "success", "TOTAL NEWSLETTERS", String.valueOf(newslettercount), "View all newsletters", "success", "bx bx-envelope", 0));
        totalecomWidgets.add(new Widget(4L, "warning", "TOTAL SUBSCRIBERS", String.valueOf(subs), "View all subscribers", "warning", "bx bxs-bell", 0));

        DashboardWidgetsResponse response = new DashboardWidgetsResponse();
        response.setEcomWidgets(ecomWidgets);
        response.setTotalecomWidgets(totalecomWidgets);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/admin/getcampaign-list")
    public ResponseEntity<ApiResponse1<List<Map<String, String>>>> getAllCampaignList() {
        List<Campaign> campaignList = campaignReposiotry.findAll();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        List<Map<String, String>> campaignResponseList = new ArrayList<>();

        for (Campaign camp : campaignList) {
            Map<String, String> map = new HashMap<>();
            map.put("name", camp.getName());
            map.put("category", camp.getCategory().getName());
            map.put("status", String.valueOf(camp.getStatus()));
            map.put("startDate", dateFormatter.format(camp.getStartDate()));
            map.put("endDate", dateFormatter.format(camp.getEndDate()));
            campaignResponseList.add(map);
        }
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaignResponseList, "SUCCESS", true));
    }

    @GetMapping("/admin/user-download-csv")
    public ResponseEntity<ByteArrayResource> downloadUserCV() {
        List<User> userList = userService.getAllUsers();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8)) {
            writer.write("User ID,First Name,Last Name,Email,Job Title,Organisation,Total Save Count,Total Download Count,Total View Count,IP Address,Registered At\n");

            for (User user : userList) {
                List<UserDataStorage> userData = userDataStorageRepository.findByUserIdList(user.getId());
                List<String> userSubscribeCount = user.getFavorites() != null ? user.getFavorites() : new ArrayList<>();
                int totalCategorySubscribedCount = 0;

                for (String favorites : userSubscribeCount) {
                    if (favorites != null && !favorites.isEmpty()) {
                        String[] splitFavorites = favorites.split(",");
                        totalCategorySubscribedCount += splitFavorites.length;
                    }
                }

                long totalSaveCount = userData.stream().filter(count -> count.getSave() == 1).count();
                long totalDownloadCount = userData.stream().filter(count -> count.getDownload() == 1).count();
                long totalViewCount = userData.stream().filter(count -> count.getView() == 1).count();

                String ipAddress = user.getIpAddress() != null ? user.getIpAddress() : "null";
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String formattedDate = null;
                LocalDateTime dateTime = null;
                if (user.getDt1() != null) {
                    try {
                        if (user.getDt1() instanceof LocalDateTime) {
                            dateTime = user.getDt1();
                            formattedDate = dateTime.format(dateFormatter);
                            System.out.println("Formated Date" + formattedDate);
                            System.out.println("Formated Date" + dateFormatter);
                        }
                    } catch (Exception e) {
                        formattedDate = "Invalid Date";
                    }

                }

                writer.write(user.getId() + "," +
                        user.getName() + "," +
                        user.getLastName() + "," +
                        user.getEmail() + "," +
                        user.getJobTitle() + "," +
                        user.getCompany() + "," +
                        totalSaveCount + "," +
                        totalDownloadCount + "," +
                        totalViewCount + "," +
                        ipAddress + "," +
                        dateTime.format(dateFormatter) + "," +
                        "0\n");
            }

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }

        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        String fileName = "users_"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) +".csv";
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
    @GetMapping("/admin/user-report")
    public ResponseEntity<Map<String, Object>> getUserReport() {
        List<User> userList = userService.getAllUsers();
        Map<String, Object> responseData = new HashMap<>();
        List<Map<String, Object>> userDataList = new ArrayList<>();

        for (User user : userList) {
            List<UserDataStorage> userData = userDataStorageRepository.findByUserIdList(user.getId());
            List<String> userSubscribeCount = user.getFavorites() != null ? user.getFavorites() : new ArrayList<>();
            int totalCategorySubscribedCount = 0;

            for (String favorites : userSubscribeCount) {
                if (favorites != null && !favorites.isEmpty()) {
                    String[] splitFavorites = favorites.split(",");
                    totalCategorySubscribedCount += splitFavorites.length;
                }
            }

            long totalSaveCount = userData.stream().filter(count -> count.getSave() == 1).count();
            long totalDownloadCount = userData.stream().filter(count -> count.getDownload() == 1).count();
            long totalViewCount = userData.stream().filter(count -> count.getView() == 1).count();

            String ipAddress = user.getIpAddress() != null ? user.getIpAddress() : "null";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = null;
            LocalDateTime dateTime = null;

            if (user.getDt1() != null) {
                try {
                    if (user.getDt1() instanceof LocalDateTime) {
                        dateTime = user.getDt1();
                        formattedDate = dateTime.format(dateFormatter);
                    }
                } catch (Exception e) {
                    formattedDate = "Invalid Date";
                }
            }

            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userId", user.getId());
            userMap.put("firstName", user.getName());
            userMap.put("lastName", user.getLastName());
            userMap.put("email", user.getEmail());
            userMap.put("jobTitle", user.getJobTitle());
            userMap.put("organisation", user.getCompany());
            userMap.put("totalSaveCount", totalSaveCount);
            userMap.put("totalDownloadCount", totalDownloadCount);
            userMap.put("totalViewCount", totalViewCount);
            userMap.put("ipAddress", ipAddress);
            userMap.put("registeredAt", formattedDate);

            userDataList.add(userMap);
        }

        responseData.put("users", userDataList);

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/admin/category-download-csv")
    public ResponseEntity<ByteArrayResource> downloadCategoryCV() {
        List<Category> categoryList = categoryRepo.findAll();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8)) {
            writer.write("Category ID,Category Name,WhitePaper Count,Total Download,Total Views,Total Save,Registered At\n");

            for (Category category : categoryList) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                List<UserDataStorage> userDataStorageList = userDataStorageRepository.findByCategoryIdList(category.getId()) ;
                long downloadCount=userDataStorageList.stream().filter(s->s.getDownload()==1).count();
                long viewCount=userDataStorageList.stream().filter(s->s.getView()==1).count();
                long saveCount=userDataStorageList.stream().filter(s->s.getSave()==1).count();
                String formattedDate = null;
                LocalDateTime dateTime = null;
                if (category.getDt1() != null) {
                    try {
                        if (category.getDt1() instanceof LocalDateTime) {
                            dateTime = category.getDt1();
                            formattedDate = dateTime.format(dateFormatter);
                            System.out.println("Formated Date" + formattedDate);
                            System.out.println("Formated Date" + dateFormatter);
                        }
                    } catch (Exception e) {
                        formattedDate = "Invalid Date";
                    }

                }

                writer.write(category.getId() + "," +
                        category.getName() + "," +
                        category.getSolutionSets().size() + "," +
                        downloadCount + "," +
                        viewCount + "," +
                        saveCount + ","+
                        dateTime.format(dateFormatter) + "," +
                        "0\n");
            }

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }

        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        String fileName = "whitepaperSet_"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".csv";
        String headerValue = "attachment; filename=" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
    @GetMapping("/admin/category-report")
    public ResponseEntity<Map<String, Object>> getCategoryReport() {
        List<Category> categoryList = categoryRepo.findAll();
        Map<String, Object> responseData = new HashMap<>();
        List<Map<String, Object>> categoryDataList = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (Category category : categoryList) {
            List<UserDataStorage> userDataStorageList = userDataStorageRepository.findByCategoryIdList(category.getId());
            long downloadCount = userDataStorageList.stream().filter(s -> s.getDownload() == 1).count();
            long viewCount = userDataStorageList.stream().filter(s -> s.getView() == 1).count();
            long saveCount = userDataStorageList.stream().filter(s -> s.getSave() == 1).count();
            String formattedDate = "Invalid Date";
            LocalDateTime dateTime = null;

            if (category.getDt1() != null) {
                try {
                    if (category.getDt1() instanceof LocalDateTime) {
                        dateTime = category.getDt1();
                        formattedDate = dateTime.format(dateFormatter);
                    }
                } catch (Exception e) {
                    formattedDate = "Invalid Date";
                }
            }

            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("categoryId", category.getId());
            categoryMap.put("categoryName", category.getName());
            categoryMap.put("whitePaperCount", category.getSolutionSets().size());
            categoryMap.put("totalDownload", downloadCount);
            categoryMap.put("totalViews", viewCount);
            categoryMap.put("totalSave", saveCount);
            categoryMap.put("registeredAt", formattedDate);

            categoryDataList.add(categoryMap);
        }

        responseData.put("categories", categoryDataList);

        return ResponseEntity.ok(responseData);
    }


    @GetMapping("/admin/category-by-id-download-csv")
    public ResponseEntity<ByteArrayResource> downloadCategoryCV(@RequestParam long id) {
        Category category = categoryRepo.findById(id).orElse(null);

        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8)) {
            writer.write("Category ID,Category Name,WhitePaper Name,Total Download,Total Views,Total Save,Registered At\n");

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = "Invalid Date";



            if (category.getSolutionSets() != null) {
                for (SolutionSets solutionSet : category.getSolutionSets()) {
                    if (solutionSet.getDt1() != null && category.getDt1() instanceof LocalDateTime) {
                        LocalDateTime dateTime = (LocalDateTime) category.getDt1();
                        formattedDate = dateTime.format(dateFormatter);
                    }

                    List<UserDataStorage> userDataStorageList = userDataStorageRepository.findBySolutionSetIdList(solutionSet.getId());

                    long downloadCount = userDataStorageList.stream().filter(s -> s.getDownload() == 1).count();
                    long viewCount = userDataStorageList.stream().filter(s -> s.getView() == 1).count();
                    long saveCount = userDataStorageList.stream().filter(s -> s.getSave() == 1).count();
                    String title = solutionSet.getTitle().replace(",", " ");

                    writer.write(String.format("%d,%s,%s,%d,%d,%s\n",
                            category.getId(),
                            category.getName(),
                            title,
                            downloadCount,
                            viewCount,
                            saveCount,
                            formattedDate));
                }
            } else {
                writer.write(String.format("%d,%s,%s,%d,%d,%s\n",
                        category.getId(),
                        category.getName(),
                        "No Solution Sets",
                        0,
                        0,
                        0,
                        formattedDate));
            }

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        String fileName = "whitepaperSet_" + category.getName().replaceAll("\\s+", "_") + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) +  ".csv";
        String headerValue = "attachment; filename=" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);

    }
    @GetMapping("/admin/category-by-id-report")
    public ResponseEntity<Map<String, Object>> getCategoryByIdReport(@RequestParam long id) {
        Category category = categoryRepo.findById(id).orElse(null);

        if (category == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Map<String, Object> responseData = new HashMap<>();
        List<Map<String, Object>> solutionSetDataList = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = "Invalid Date";

        if (category.getSolutionSets() != null) {
            for (SolutionSets solutionSet : category.getSolutionSets()) {
                List<UserDataStorage> userDataStorageList = userDataStorageRepository.findBySolutionSetIdList(solutionSet.getId());
                long downloadCount = userDataStorageList.stream().filter(s -> s.getDownload() == 1).count();
                long viewCount = userDataStorageList.stream().filter(s -> s.getView() == 1).count();
                long saveCount = userDataStorageList.stream().filter(s -> s.getSave() == 1).count();

                if (solutionSet.getDt1() != null && category.getDt1() instanceof LocalDateTime) {
                    LocalDateTime dateTime = (LocalDateTime) category.getDt1();
                    formattedDate = dateTime.format(dateFormatter);
                }

                Map<String, Object> solutionSetMap = new HashMap<>();
                solutionSetMap.put("categoryId", category.getId());
                solutionSetMap.put("categoryName", category.getName());
                solutionSetMap.put("whitePaperName", solutionSet.getTitle());
                solutionSetMap.put("totalDownload", downloadCount);
                solutionSetMap.put("totalViews", viewCount);
                solutionSetMap.put("totalSave", saveCount);
                solutionSetMap.put("registeredAt", formattedDate);

                solutionSetDataList.add(solutionSetMap);
            }
        } else {
            Map<String, Object> noSolutionSetMap = new HashMap<>();
            noSolutionSetMap.put("categoryId", category.getId());
            noSolutionSetMap.put("categoryName", category.getName());
            noSolutionSetMap.put("whitePaperName", "No Solution Sets");
            noSolutionSetMap.put("totalDownload", 0);
            noSolutionSetMap.put("totalViews", 0);
            noSolutionSetMap.put("totalSave", 0);
            noSolutionSetMap.put("registeredAt", formattedDate);
            solutionSetDataList.add(noSolutionSetMap);
        }

        responseData.put("solutionSets", solutionSetDataList);

        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/admin/whitepaper-by-id-download-csv")
    public ResponseEntity<ByteArrayResource> downloadWhitePaperCV(@RequestParam long id) {
        SolutionSets solutionSets = solutionSetsRepository.findById(id).orElse(null);

        if (solutionSets == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8)) {
            writer.write("WhitePaper ID,WhitePaper Name,Category Name,Prospect Name,Prospect Email,Prospect Designation,Prospect Company,Prospect Country,View,Download,Save,User Ip,View At,Saved At,Download At\n");

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = "Invalid Date";

            if (solutionSets.getDt1() != null && solutionSets.getDt1() instanceof LocalDateTime) {
                LocalDateTime dateTime = (LocalDateTime) solutionSets.getDt1();
                formattedDate = dateTime.format(dateFormatter);
            }

            List<UserDataStorage> userDataStorageList = userDataStorageRepository.findBySolutionSetIdList(solutionSets.getId());

            for (UserDataStorage userData : userDataStorageList) {
                String view = "No";
                String download = "No";
                String save = "No";
                String viewAt = "-";
                String savedAt = "-";
                String downloadAt = "-";

                if (userData.getDownload() == 1) {
                    download = "Yes";
                    downloadAt = formattedDate;
                } else if (userData.getSave() == 1) {
                    save = "Yes";
                    savedAt = formattedDate;
                } else if (userData.getView() == 1) {
                    view = "Yes";
                    viewAt = formattedDate;
                }

                User user = userService.getUserById(userData.getUser_id());

//                "WhitePaper ID,WhitePaper Name,Category Name,Prospect Name,Prospect Email" +
//                        ",Prospect Designation,Prospect Company,Prospect Country," +
//                        "View,Download,Save,User Ip,View At,Saved At,Download At\n
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        solutionSets.getId(),
                        solutionSets.getTitle(),
                        solutionSets.getCategory().getName(),
                        user.getName(),
                        user.getEmail(),
                        user.getJobTitle(),
                        user.getCompany(),
                        user.getCountry(),
                        view,
                        download,
                        save,
                        userData.getIp(),
                        viewAt,
                        savedAt,
                        downloadAt));
            }

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        String fileName = "whitepaper_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + solutionSets.getTitle().replaceAll("\\s+", "_") + ".csv";
        String headerValue = "attachment; filename=" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
    @GetMapping("/admin/whitepaper-download-csv")
    public ResponseEntity<ByteArrayResource> downloadWhitePaperCV() {
        List<SolutionSets> solutionSets = solutionSetsRepository.findAll();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8)) {
            writer.write("WhitePaper ID,WhitePaper Name,Category Name,Uploaded By,Total Download,Total Views,Total Save,Registered At\n");

            for (SolutionSets sets : solutionSets) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                List<UserDataStorage> userDataStorageList = userDataStorageRepository.findBySolutionSetIdList(sets.getId()) ;
                long downloadCount=userDataStorageList.stream().filter(s->s.getDownload()==1).count();
                long viewCount=userDataStorageList.stream().filter(s->s.getView()==1).count();
                long saveCount=userDataStorageList.stream().filter(s->s.getSave()==1).count();
                String formattedDate = null;
                LocalDateTime dateTime = null;
                if (sets.getDt1() != null) {
                    try {
                        if (sets.getDt1() instanceof LocalDateTime) {
                            dateTime = sets.getDt1();
                            formattedDate = dateTime.format(dateFormatter);
                            System.out.println("Formated Date" + formattedDate);
                            System.out.println("Formated Date" + dateFormatter);
                        }
                    } catch (Exception e) {
                        formattedDate = "Invalid Date";
                    }

                }
                String title = sets.getTitle().replace(","," ");

                writer.write(sets.getId() + "," +
                        title + "," +
                        sets.getCategory().getName() + "," +
                        sets.getUploadedBy().getName() + ","+
                        downloadCount + "," +
                        viewCount + "," +
                        saveCount + ","+
                        dateTime.format(dateFormatter) + "," +
                        "0\n");
            }

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }

        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        String fileName = "Allwhitepaper_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".csv";

        String headerValue = "attachment; filename=" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
    @GetMapping("/admin/whitepaper-report")
    public ResponseEntity<Map<String, Object>> getWhitePaperReport() {
        List<SolutionSets> solutionSets = solutionSetsRepository.findAll();
        Map<String, Object> reportData = new HashMap<>();
        List<Map<String, Object>> whitePaperDetails = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (SolutionSets sets : solutionSets) {
            Map<String, Object> whitePaperReport = new HashMap<>();
            List<UserDataStorage> userDataStorageList = userDataStorageRepository.findBySolutionSetIdList(sets.getId());
            long downloadCount = userDataStorageList.stream().filter(s -> s.getDownload() == 1).count();
            long viewCount = userDataStorageList.stream().filter(s -> s.getView() == 1).count();
            long saveCount = userDataStorageList.stream().filter(s -> s.getSave() == 1).count();

            String formattedDate = "Invalid Date";
            if (sets.getDt1() != null) {
                try {
                    if (sets.getDt1() instanceof LocalDateTime) {
                        LocalDateTime dateTime = sets.getDt1();
                        formattedDate = dateTime.format(dateFormatter);
                    }
                } catch (Exception e) {
                    formattedDate = "Invalid Date";
                }
            }

            whitePaperReport.put("whitePaperID", sets.getId());
            whitePaperReport.put("whitePaperName", sets.getTitle());
            whitePaperReport.put("categoryName", sets.getCategory().getName());
            whitePaperReport.put("uploadedBy", sets.getUploadedBy().getName());
            whitePaperReport.put("totalDownload", downloadCount);
            whitePaperReport.put("totalViews", viewCount);
            whitePaperReport.put("totalSave", saveCount);
            whitePaperReport.put("registeredAt", formattedDate);

            whitePaperDetails.add(whitePaperReport);
        }

        reportData.put("whitePapers", whitePaperDetails);

        return ResponseEntity.ok(reportData);
    }

    @GetMapping("/admin/whitepaper-by-id-download-report")
    public ResponseEntity<Map<String, Object>> getWhitePaperReport(@RequestParam long id) {
        SolutionSets solutionSets = solutionSetsRepository.findById(id).orElse(null);

        if (solutionSets == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("whitePaperId", solutionSets.getId());
        responseData.put("whitePaperName", solutionSets.getTitle());
        responseData.put("categoryName", solutionSets.getCategory().getName());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = "Invalid Date";

        if (solutionSets.getDt1() != null && solutionSets.getDt1() instanceof LocalDateTime) {
            LocalDateTime dateTime = (LocalDateTime) solutionSets.getDt1();
            formattedDate = dateTime.format(dateFormatter);
        }

        List<Map<String, Object>> userDataList = new ArrayList<>();
        List<UserDataStorage> userDataStorageList = userDataStorageRepository.findBySolutionSetIdList(solutionSets.getId());

        for (UserDataStorage userData : userDataStorageList) {
            Map<String, Object> userDataMap = new HashMap<>();

            String view = "No";
            String download = "No";
            String save = "No";
            String viewAt = "-";
            String savedAt = "-";
            String downloadAt = "-";

            if (userData.getDownload() == 1) {
                download = "Yes";
                downloadAt = formattedDate;
            } else if (userData.getSave() == 1) {
                save = "Yes";
                savedAt = formattedDate;
            } else if (userData.getView() == 1) {
                view = "Yes";
                viewAt = formattedDate;
            }

            User user = userService.getUserById(userData.getUser_id());

            userDataMap.put("userName", user.getName());
            userDataMap.put("userEmail", user.getEmail());
            userDataMap.put("userDesignation", user.getJobTitle());
            userDataMap.put("userCompany", user.getCompany());
            userDataMap.put("userCountry", user.getCountry());
            userDataMap.put("view", view);
            userDataMap.put("download", download);
            userDataMap.put("save", save);
            userDataMap.put("userIp", userData.getIp());
            userDataMap.put("viewAt", viewAt);
            userDataMap.put("savedAt", savedAt);
            userDataMap.put("downloadAt", downloadAt);

            userDataList.add(userDataMap);
        }

        responseData.put("userData", userDataList);

        return ResponseEntity.ok(responseData);
    }


    @GetMapping("/view-all-downloaded-by-userid")
    public ResponseEntity<ApiResponse1<Map<?,?>>> getAllViewDownloaded(@RequestParam long id){
        List<UserDataStorage> userDataStorage = userDataStorageRepository.findByUserIdList(id);
        List<UserDataStorage> filteredData=userDataStorage.stream().filter(userDataStorage1 -> userDataStorage1.getDownload()==1&&userDataStorage1.getUser_id()==id).toList();
        List<UserDataStorage> filteredSavedData=userDataStorage.stream().filter(userDataStorage1 -> userDataStorage1.getSave()==1&&userDataStorage1.getUser_id()==id).toList();
        List<UserDataStorage> filteredViewData=userDataStorage.stream().filter(userDataStorage1 -> userDataStorage1.getView()==1&&userDataStorage1.getUser_id()==id).toList();

        List<SolutionSets> solutionSets = new ArrayList<>();
        for (UserDataStorage userData : filteredData) {
            Long solutionSetId = userData.getSolutionSetId();

            Optional<SolutionSets> solutionSetOptional = solutionSetsRepository.findById(solutionSetId);

            solutionSetOptional.ifPresent(solutionSets::add);
        }List<SolutionSets> solutionViewSets = new ArrayList<>();
        for (UserDataStorage userData : filteredViewData) {
            Long solutionSetId = userData.getSolutionSetId();

            Optional<SolutionSets> solutionSetOptional = solutionSetsRepository.findById(solutionSetId);

            solutionSetOptional.ifPresent(solutionViewSets::add);
        }
        List<SolutionSets> solutionSaveSets = new ArrayList<>();
        for (UserDataStorage userData : filteredSavedData) {
            Long solutionSetId = userData.getSolutionSetId();

            Optional<SolutionSets> solutionSetOptional = solutionSetsRepository.findById(solutionSetId);

            solutionSetOptional.ifPresent(solutionSaveSets::add);
        }
        Map<String,Object> map = new HashMap<>();

        map.put("allDownloaded",solutionSets);
        map.put("allViewed",solutionViewSets);
        map.put("allSaved",solutionSaveSets);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(map,"SUCCESS",true));


    }
    @GetMapping("/admin/newsletter-report")
    public ResponseEntity<Map<String, Object>> getNewsLetterReport() {
        List<NewsLetter> newsLetterList = newsLetterRepository.findAll();
        Map<String, Object> reportData = new HashMap<>();
        List<Map<String, Object>> whitePaperDetails = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (NewsLetter newsLetter : newsLetterList) {
            Map<String, Object> whitePaperReport = new HashMap<>();

            String formattedDate = "Invalid Date";
            if (newsLetter.getDt1() != null) {
                try {
                    if (newsLetter.getDt1() instanceof LocalDateTime) {
                        LocalDateTime dateTime = newsLetter.getDt1();
                        formattedDate = dateTime.format(dateFormatter);
                    }
                } catch (Exception e) {
                    formattedDate = "Invalid Date";
                }
            }

            whitePaperReport.put("newsLetterId", newsLetter.getId());
            whitePaperReport.put("newsLetterName", newsLetter.getTitle());
            whitePaperReport.put("uploadedBy", newsLetter.getAdmin().getName());
            whitePaperReport.put("totalDownload", 0);
            whitePaperReport.put("totalViews", 0);
            whitePaperReport.put("totalSave", 0);
            whitePaperReport.put("uploadDate", formattedDate);

            whitePaperDetails.add(whitePaperReport);
        }

        reportData.put("newsLetters", whitePaperDetails);

        return ResponseEntity.ok(reportData);
    }
    @GetMapping("/admin/newsletter-download-csv")
    public ResponseEntity<ByteArrayResource> downloadNewsLetterCV() {
        List<NewsLetter> newsLetterList = newsLetterRepository.findAll();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8)) {
            writer.write("News Letter ID,News Letter Name,Uploaded By,Total Download,Total Views,Total Save,Upload Date\n");

            for (NewsLetter newsLetter : newsLetterList) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String formattedDate = null;
                LocalDateTime dateTime = null;
                if (newsLetter.getDt1() != null) {
                    try {
                        if (newsLetter.getDt1() instanceof LocalDateTime) {
                            dateTime = newsLetter.getDt1();
                            formattedDate = dateTime.format(dateFormatter);
                        }
                    } catch (Exception e) {
                        formattedDate = "Invalid Date";
                    }

                }
                String title = newsLetter.getTitle().replace(","," ");

                writer.write(newsLetter.getId() + "," +
                        title + "," +
                        newsLetter.getAdmin().getName() + "," +
                        0 + "," +
                        0 + "," +
                        0 + ","+
                        dateTime.format(dateFormatter) + "," +
                        "0\n");
            }

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }

        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        String fileName = "Allnewsletterr_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".csv";

        String headerValue = "attachment; filename=" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
    @GetMapping("/admin/blog-report")
    public ResponseEntity<Map<String, Object>> getBlogReport() {
        List<Blogs> blogsList = blogsRepository.findAll();
        Map<String, Object> reportData = new HashMap<>();
        List<Map<String, Object>> whitePaperDetails = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (Blogs blogs : blogsList) {
            Map<String, Object> whitePaperReport = new HashMap<>();

            String formattedDate = "Invalid Date";
            if (blogs.getDt1() != null) {
                try {
                    if (blogs.getDt1() instanceof LocalDateTime) {
                        LocalDateTime dateTime = blogs.getDt1();
                        formattedDate = dateTime.format(dateFormatter);
                    }
                } catch (Exception e) {
                    formattedDate = "Invalid Date";
                }
            }

            whitePaperReport.put("blogId", blogs.getId());
            whitePaperReport.put("blogName", blogs.getBlogName());
            whitePaperReport.put("blogCategory", blogs.getBlogsCategory().getBlogCategoryName());
            whitePaperReport.put("uploadDate", formattedDate);

            whitePaperDetails.add(whitePaperReport);
        }

        reportData.put("newsLetters", whitePaperDetails);

        return ResponseEntity.ok(reportData);
    }
    @GetMapping("/admin/blog-download-csv")
    public ResponseEntity<ByteArrayResource> downloadBlogCV() {
        List<Blogs> blogsList = blogsRepository.findAll();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8)) {
            writer.write("Blog ID,Blog Name,Blog Category,Uploaded By,Upload Date\n");


            for (Blogs blogs : blogsList) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String formattedDate = null;
                LocalDateTime dateTime = null;

                if (blogs.getDt1() != null) {
                    try {
                        if (blogs.getDt1() instanceof LocalDateTime) {
                            dateTime = blogs.getDt1();
                            formattedDate = dateTime.format(dateFormatter);
                        }
                    } catch (Exception e) {
                        formattedDate = "Invalid Date";
                    }

                }
                String title = blogs.getBlogName().replace(","," ");

                writer.write(blogs.getId() + "," +
                        title + "," +
                        blogs.getBlogsCategory().getBlogCategoryName() + "," +
                        blogs.getAdmin().getName()+ "," +
                        dateTime.format(dateFormatter) + "," +
                        "0\n");
            }

            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }

        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        String fileName = "AllBlogs" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".csv";

        String headerValue = "attachment; filename=" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, headerValue);
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
