package com.contenttree.vendor;

import com.contenttree.Blogs.Blogs;
import com.contenttree.Blogs.BlogsRepository;
import com.contenttree.Jwt.JwtResponse;
import com.contenttree.NewsLetter.NewsLetter;
import com.contenttree.NewsLetter.NewsLetterRepository;
import com.contenttree.admin.Admin;
import com.contenttree.admin.AdminService;
import com.contenttree.admin.DashboardWidgetsResponse;
import com.contenttree.admin.Widget;
import com.contenttree.category.Category;
import com.contenttree.category.CategoryService;
import com.contenttree.security.VendorJwtHelper;
import com.contenttree.solutionsets.*;
import com.contenttree.user.EmailService;
import com.contenttree.user.User;
import com.contenttree.user.UserService;
import com.contenttree.userdatastorage.UserDataStorage;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    @Autowired
    VendorJwtHelper helper;

    @Autowired
    VendorsService vendorsService;

    @Autowired
    BlogsRepository blogsRepository;

    @Autowired
    NewsLetterRepository newsLetterRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;


    @Autowired
    AdminService adminService;

    @Autowired
    UserService userService;

    @Autowired
    SolutionSetsService solutionSetsService;

    @Autowired
    CategoryService categoryService;

    private static final String logoUrl = "https://infeedu.com/static/media/Infinite-b2b-1-scaled.f42a6998e6eac74721e6.png";

    private static final String whitepaperUrl = "https://infeedu.com/";

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email,
                                                     @RequestParam String password) {
        Vendors vendors = vendorsService.getVendorsByEmail(email);

        if (vendors == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        if (vendors.getStatus() == VendorStatus.PENDING) {
            Map<String, Object> errorResponse = new HashMap<>();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }

        if (vendors.getStatus() == VendorStatus.INACTIVE) {
            Map<String, Object> errorResponse = new HashMap<>();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponse);
        }

//        if (!passwordEncoder.matches(password, vendors.getPassword())) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            System.out.println(password+"password is here");
//            System.out.println(passwordEncoder.equals(password)+"password is here");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
//        }

        String token = this.helper.generateToken(vendors);

        Map<String, Object> vendorDetails = new HashMap<>();
        vendorDetails.put("_id", vendors.getId());
        vendorDetails.put("email", vendors.getEmail());
        vendorDetails.put("name", vendors.getName());
        vendorDetails.put("company_name", vendors.getCompanyName());
        vendorDetails.put("role", "vendor");
        vendorDetails.put("status", vendors.getStatus().name());
        vendorDetails.put("password", "********");
        vendorDetails.put("confirm_password", "********");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "success");
        response.put("token", token);
        response.put("data", vendorDetails);

        return ResponseEntity.ok(response);
    }



@PostMapping("/register")
public ResponseEntity<String> registerVendor(@RequestParam String email,
                                             @RequestParam String password,
                                             @RequestParam String name,
                                             @RequestParam String lastName,
                                             @RequestParam String phoneNo,
                                             @RequestParam String companyName,
                                             @RequestParam String designation,
                                             @RequestParam String country,
                                             @RequestParam String state,
                                             @RequestParam String zipCode) {

    if (email == null || email.isEmpty() || password == null || password.isEmpty() || name == null || name.isEmpty()) {
        return ResponseEntity.badRequest().body("Email, password, and name are required.");
    }

    if (adminService.getAdminByEmailId(email) != null || userService.getUserByEmail(email) != null) {
        return ResponseEntity.badRequest().body("Email Already Registered. Please Try Again");
    }

    Vendors vendor = Vendors.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .name(name)
            .lastName(lastName != null ? lastName : "")
            .phoneNo(phoneNo != null ? phoneNo : "")
            .companyName(companyName != null ? companyName : "")
            .designation(designation != null ? designation : "")
            .country(country != null ? country : "")
            .state(state != null ? state : "")
            .zipCode(zipCode != null ? zipCode : "")
            .status(VendorStatus.PENDING)
            .dt1(LocalDateTime.now())
            .build();

    vendorsService.registerVendors(vendor);

    return ResponseEntity.ok("Vendor Registered Successfully. Waiting for Approval From Central Team");
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
    @GetMapping("/by-id/{id}")
    public ResponseEntity<ApiResponse1<Vendors>> getVendorById(@PathVariable long id){
        Vendors vendors = vendorsService.getVendorsById(id);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(vendors,"SUCCESS",true));
    }


    @GetMapping("/byid")
    public ResponseEntity<ApiResponse1<SolutionSetDto>> getSolutionSetById(@RequestParam long id) {
        SolutionSets solutionSets = solutionSetsService.getById(id);
//        SolutionSetDto solutionSetDto = SolutionSetMapper(solutionSets); // Map to DTO
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(solutionSetDto, "SUCCESS", true));
        return null;
    }

    //    @PostMapping("/add-solutionset")
//    public ResponseEntity<ApiResponse1<SolutionSets>> addSolutionSet(@RequestParam MultipartFile file,@RequestParam(required = false) MultipartFile image, @RequestParam String category
//    ,@RequestParam String desc,@RequestParam String title) throws MessagingException {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
//            return ResponseEntity.status(401).body(null);
//        }
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Vendors vendors = vendorsService.getVendorsByEmail(userDetails.getUsername());
//
//        if (vendors == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//
//        Category categoryEntity = categoryService.getCategoryByName(category);
//        if (categoryEntity == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(ResponseUtils.createResponse1(null, "Invalid category name", false));
//        }
//        List<User> userList =userService.getAllUsers();
//        userList.stream().filter(a-> a.getIsSubscriber()==1).toList();
//        String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
//                "<div style=\"text-align: center; margin-bottom: 30px;\">" +
//                "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
//                "</div>" +
//                "<h2 style=\"text-align: center; color: #007bff; font-size: 24px;\">Latest Whitepapers Released! " +  title +" </h2>" +
//                "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
//                "We are excited to share the latest updates from <strong>InfiniteB2B</strong>! Our new whitepapers on <strong>" + categoryEntity.getName() + "</strong> are now available. Explore the latest insights and trends to stay ahead in your field." +
//                "</p>" +
//                "<div style=\"text-align: center; margin: 30px 0;\">" +
//                "<a href=\"" + whitepaperUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">Explore Now</a>" +
//                "</div>" +
//                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                "If the button above doesn’t work, copy and paste the following URL into your browser:" +
//                "</p>" +
//                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                "<a href=\"" + whitepaperUrl + "\" style=\"color: #007bff; word-wrap: break-word;\">" + whitepaperUrl + "</a>" +
//                "</p>" +
//                "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
//                "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
//                "<p>&copy; 2024 InfiniteB2B. All rights reserved.</p>" +
//                "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
//                "</footer>" +
//                "</div>";
//
//        solutionSetsService.uploadSolutionSets(file,image, vendors.getId(), categoryEntity.getId(),desc,title);
//
//        for (User user : userList){
//        emailService.sendHtmlEmail(user.getEmail(),"New WhitePaper Added in your Favorite Category " + categoryEntity.getName(), htmlMessage);}
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "SUCCESS", true));
//    }
//    @PostMapping("/add-solutionset")
//    public ResponseEntity<ApiResponse1<SolutionSets>> addSolutionSet(
//            @RequestParam MultipartFile file,
//            @RequestParam(required = false) MultipartFile image,
//            @RequestParam String category,
//            @RequestParam String desc,
//            @RequestParam String title) throws MessagingException {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
//            return ResponseEntity.status(401).body(null);
//        }
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Vendors vendors = vendorsService.getVendorsByEmail(userDetails.getUsername());
//        Admin admin = adminService.getAdminByEmailId(userDetails.getUsername());
//        if (vendors == null) {
//            vendors = vendorsService.getVendorsById(1);
//            if (vendors == null) {
//                return ResponseEntity.notFound().build();
//            }
//        }
//
//        if (admin != null) {
//            vendors = vendorsService.getVendorsById(1);
//        }
//
//        Category categoryEntity = categoryService.getCategoryByName(category);
//        if (categoryEntity == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(ResponseUtils.createResponse1(null, "Invalid category name", false));
//        }
//        int value =0;
//        if (vendors!=null){
//            value=1;
//        } else if (admin!=null) {
//            value=2;
//        }
//
//        String solutionSet = solutionSetsService.uploadSolutionSets(
//                file, image, vendors.getId(), categoryEntity.getId(), desc, title,value);
//        String baseUrl = "https://infiniteb2b.com";
//        String whitepaperUrl = baseUrl + "/category/" + categoryEntity.getId();
//
//        List<User> userList = userService.getAllUsers();
//        userList = userList.stream().filter(a -> a.getIsSubscriber() == 1).toList();
//
//        String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
//                "<div style=\"text-align: center; margin-bottom: 30px;\">" +
//                "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
//                "</div>" +
//                "<h2 style=\"text-align: center; color: #007bff; font-size: 24px;\">Latest Whitepapers Released! " + title + "</h2>" +
//                "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
//                "We are excited to share the latest updates from <strong>InfiniteB2B</strong>! Our new whitepapers on <strong>" + categoryEntity.getName() + "</strong> are now available. Explore the latest insights and trends to stay ahead in your field." +
//                "</p>" +
//                "<div style=\"text-align: center; margin: 30px 0;\">" +
//                "<a href=\"" + whitepaperUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">Explore Now</a>" +
//                "</div>" +
//                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                "If the button above doesn’t work, copy and paste the following URL into your browser:" +
//                "</p>" +
//                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                "<a href=\"" + whitepaperUrl + "\" style=\"color: #007bff; word-wrap: break-word;\">" + whitepaperUrl + "</a>" +
//                "</p>" +
//                "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
//                "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
//                "<p>&copy; 2024 InfiniteB2B. All rights reserved.</p>" +
//                "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
//                "</footer>" +
//                "</div>";
//
//        for (User user : userList) {
//            emailService.sendHtmlEmail(user.getEmail(),
//                    "New WhitePaper Added in your Favorite Category " + categoryEntity.getName(),
//                    htmlMessage);
//        }
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "SUCCESS", true));
//    }
    @PostMapping("/add-solutionset")
    public ResponseEntity<ApiResponse1<SolutionSets>> addSolutionSet(
            @RequestParam MultipartFile file,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam String category,
            @RequestParam String desc,
            @RequestParam String title) throws MessagingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(401).body(null);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Vendors vendors = vendorsService.getVendorsByEmail(userDetails.getUsername());
        Admin admin = adminService.getAdminByEmailId(userDetails.getUsername());

        if (vendors == null && admin == null) {
            return ResponseEntity.notFound().build();
        }

        if (admin != null) {
            vendors = vendorsService.getVendorsById(1);
        }

        Category categoryEntity = categoryService.getCategoryByName(category);
        if (categoryEntity == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtils.createResponse1(null, "Invalid category name", false));
        }

        int value;
        Vendors vendor = null;

        if (admin != null) {
            value = 1;
        } else {
            value = 2;
            vendor = vendors;
        }

        if (vendor != null) {
            String solutionSetResponse = solutionSetsService.uploadSolutionSets(
                    file, image, vendor.getId(), categoryEntity.getId(), desc, title, 2);
        } else {
            String solutionSetResponse = solutionSetsService.uploadSolutionSets(
                    file, image, 1, categoryEntity.getId(), desc, title, 1);
        }


//        String solutionSetResponse = solutionSetsService.uploadSolutionSets(
//                file, image, vendors.getId(), categoryEntity.getId(), desc, title, value);


        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "SUCCESS", true));
    }
    @PostMapping("/add-solutionset-infeedu")
    public ResponseEntity<ApiResponse1<SolutionSets>> addSolutionSetAiInfeedu(
            @RequestParam MultipartFile file,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam long category,
            @RequestParam String desc,
            @RequestParam String title) throws MessagingException {


        Optional<Category> categoryEntity = categoryService.getCategoryBySolutionSet(category);
        if (categoryEntity == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtils.createResponse1(null, "Invalid category name", false));
        }

            String solutionSetResponse = solutionSetsService.uploadSolutionSets(
                    file, image, 17, category, desc, title, 1);



        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "SUCCESS", true));
    }


    @Autowired
    SolutionSetMapper solutionSetMapper;

    @GetMapping("/solutionsets-by-vendor")
    public ResponseEntity<ApiResponse1<List<SolutionSetDto>>> getSolutionSetsByVendor(@RequestParam long vendorId) {
        List<SolutionSets> list = solutionSetsService.getSolutionSetsByVendorId(vendorId);
        List<SolutionSetDto> dtoList = list.stream().map(solutionSetMapper::toSolutionSetDto).toList();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(dtoList, "SUCCESS", true));
    }

    //    @GetMapping("/dashboard")
//    public ResponseEntity<ApiResponse1<Map<?, ?>>> getVendorDashboard(@AuthenticationPrincipal Vendors vendors) {
//        List<SolutionSets> solutionSetsList = solutionSetsService.getAllSolutionSets();
//
//        long whitepapersSubmitted = solutionSetsList.stream()
//                .filter(sets -> sets.getUploadedBy() != null
//                        && sets.getUploadedBy().getId() == vendors.getId()) // Primitive comparison
//                .count();
//
//        long whitepapersApproved = solutionSetsList.stream()
//                .filter(sets -> sets.getUploadedBy() != null
//                        && sets.getUploadedBy().getId() == vendors.getId()
//                        && "APPROVED".equalsIgnoreCase(String.valueOf(sets.getStatus())))
//                .count();
//
//        long whitepapersPending = solutionSetsList.stream()
//                .filter(sets -> sets.getUploadedBy() != null
//                        && sets.getUploadedBy().getId() == vendors.getId()
//                        && "PENDING".equalsIgnoreCase(String.valueOf(sets.getStatus())))
//                .count();
//
//        long whitepapersRejected = solutionSetsList.stream()
//                .filter(sets -> sets.getUploadedBy() != null
//                        && sets.getUploadedBy().getId() == vendors.getId()
//                        && "REJECTED".equalsIgnoreCase(String.valueOf(sets.getStatus())))
//                .count();
//
//        Map<String, Long> map = new HashMap<>();
//        map.put("whitePaperSubmitted", whitepapersSubmitted);
//        map.put("whitePaperApproved", whitepapersApproved);
//        map.put("whitePaperPending", whitepapersPending);
//        map.put("whitepapersRejected", whitepapersRejected);
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(map, "SUCCESS", true));
//    }
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardWidgetsResponse> getWidgets(@AuthenticationPrincipal Vendors vendors) {
        List<Widget> ecomWidgets = new ArrayList<>();
        List<Widget> totalecomWidgets = new ArrayList<>();

        List<SolutionSets> solutionSetsList = solutionSetsService.getAllSolutionSets();

        long whitepapersSubmitted = solutionSetsList.stream()
                .filter(sets -> sets.getUploadedBy() != null
                        && Objects.equals(sets.getUploadedBy().getId(), vendors.getId()))
                .count();

        long whitepapersApproved = solutionSetsList.stream()
                .filter(sets -> sets.getUploadedBy() != null
                        && Objects.equals(sets.getUploadedBy().getId(), vendors.getId())
                        && "APPROVED".equalsIgnoreCase(String.valueOf(sets.getStatus())))
                .count();

        long whitepapersPending = solutionSetsList.stream()
                .filter(sets -> sets.getUploadedBy() != null
                        && Objects.equals(sets.getUploadedBy().getId(), vendors.getId())
                        && "PENDING".equalsIgnoreCase(String.valueOf(sets.getStatus())))
                .count();
        long whitepapersRejected = solutionSetsList.stream()
                .filter(sets -> sets.getUploadedBy() != null
                        && Objects.equals(sets.getUploadedBy().getId(), vendors.getId())
                        && "REJECTED".equalsIgnoreCase(String.valueOf(sets.getStatus())))
                .count();


        ecomWidgets.add(new Widget(1L, "primary", "WhitePapers Submitted", String.valueOf(whitepapersSubmitted), "View All", "secondary", "bx bx-file", 0));
        ecomWidgets.add(new Widget(2L, "secondary", "WhitePapers Approved", String.valueOf(whitepapersApproved), "View All", "primary", "bx bx-book", 0));
        ecomWidgets.add(new Widget(3L, "success", "WhitePapers Pending", String.valueOf(whitepapersPending), "View All", "success", "bx bx-user-circle", 0));
        ecomWidgets.add(new Widget(4L, "success", "WhitePapers Rejected", String.valueOf(whitepapersRejected), "View All", "success", "bx bx-user-circle", 0));

        DashboardWidgetsResponse response = new DashboardWidgetsResponse();
        response.setEcomWidgets(ecomWidgets);
        response.setTotalecomWidgets(totalecomWidgets);

        return ResponseEntity.ok(response);
    }

    @Autowired
    SolutionSetsRepository solutionSetsRepository;
//    @GetMapping("/get-allwhitepapers")
//    public ResponseEntity<ApiResponse1<List<SolutionSetDto>>> getAllWhitePapersList(@AuthenticationPrincipal Vendors vendors){
//        List<SolutionSets> list = solutionSetsRepository.findAll();
//        List<SolutionSetDto> solutionSetDtos = list.stream().filter(s->s.getUploadedBy().getId()== vendors.getId())
//                .map(solutionSetMapper::toSolutionSetDto)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(solutionSetDtos,"SUCCESS",true));
//    }


//    @GetMapping("/get-allwhitepapers")
//    public ResponseEntity<ApiResponse1<List<SolutionSetDto>>> getAllWhitePapersList(
//            @AuthenticationPrincipal Vendors vendors,
//            @RequestParam(required = false) Integer status) { // Adding status as query parameter
//        // Get the list of all solution sets
//        List<SolutionSets> list = solutionSetsRepository.findAll();
//
//        // Filter based on the vendor's ID and optionally by status
//        List<SolutionSetDto> solutionSetDtos = list.stream()
//                .filter(s -> s.getUploadedBy().getId() == vendors.getId()) // Filter by vendor
//                .filter(s -> (status == null || mapStatus(status) == s.getStatus())) // Filter by status if status is provided
//                .map(solutionSetMapper::toSolutionSetDto)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(solutionSetDtos, "SUCCESS", true));
//    }
@GetMapping("/get-allwhitepapers")
public ResponseEntity<ApiResponse1<List<SolutionSets>>> getAllWhitePapersList(
        @AuthenticationPrincipal Vendors vendors,
        @RequestParam(required = false) Integer status) {

    List<SolutionSets> list = solutionSetsRepository.findAll();

    List<SolutionSets> filteredSolutionSets = list.stream()
            .filter(s -> s.getUploadedBy().getId() == vendors.getId())
            .filter(s -> (status == null || mapStatus(status) == s.getStatus()))
            .collect(Collectors.toList());

    return ResponseEntity.ok().body(ResponseUtils.createResponse1(filteredSolutionSets, "SUCCESS", true));
}


    // Helper method to map integer to enum
    private SolutionSetsStatus mapStatus(int status) {
        switch (status) {
            case 1:
                return SolutionSetsStatus.APPROVED;
            case 2:
                return SolutionSetsStatus.PENDING;
            case 3:
                return SolutionSetsStatus.REJECTED;
            default:
                throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }
    @PostMapping("/update-details")
    public ResponseEntity<ApiResponse1<Vendors>> updateVendorDetails(
            @AuthenticationPrincipal Vendors vendor,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phoneNo,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String designation,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String zipCode,
            @RequestParam(required = false) String password) {

        if (vendor == null) {
            return ResponseEntity.badRequest().body(ResponseUtils.createResponse1(null, "Vendor not found", false));
        }

        if (name != null && !name.isEmpty()) {
            vendor.setName(name);
        }
        if (lastName != null && !lastName.isEmpty()) {
            vendor.setLastName(lastName);
        }
        if (phoneNo != null && !phoneNo.isEmpty()) {
            vendor.setPhoneNo(phoneNo);
        }
        if (companyName != null && !companyName.isEmpty()) {
            vendor.setCompanyName(companyName);
        }
        if (designation != null && !designation.isEmpty()) {
            vendor.setDesignation(designation);
        }
        if (country != null && !country.isEmpty()) {
            vendor.setCountry(country);
        }
        if (state != null && !state.isEmpty()) {
            vendor.setState(state);
        }
        if (zipCode != null && !zipCode.isEmpty()) {
            vendor.setZipCode(zipCode);
        }

        if (password != null && !password.isEmpty()) {
            vendor.setPassword(passwordEncoder.encode(password));
        }

        Vendors updatedVendor = vendorsService.updateVendorDetails(vendor);

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(updatedVendor, "Details updated successfully", true));
    }

    @PostMapping("/update-solutionset")
    public ResponseEntity<ApiResponse1<SolutionSets>> updateSolutionSet(
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) String desc,
            @RequestParam(required = false) String title,
            @RequestParam long solutionSetId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(401).body(null);
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Vendors vendors = vendorsService.getVendorsByEmail(userDetails.getUsername());
        Admin admin = adminService.getAdminByEmailId(userDetails.getUsername());
        if (vendors == null) {
            vendors = vendorsService.getVendorsById(1);
            if (vendors == null) {
                return ResponseEntity.notFound().build();
            }
        }

        if (admin != null) {
            vendors = vendorsService.getVendorsById(1);
        }



        SolutionSets existingSolutionSet = solutionSetsRepository.findById(solutionSetId).orElse(null);
        if (existingSolutionSet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtils.createResponse1(null, "Solution set not found", false));
        }
        Category categoryEntity = categoryService.getCategoryByName(existingSolutionSet.getCategory().getName());
        if (categoryEntity == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtils.createResponse1(null, "Invalid category name", false));
        }

        if (file != null) {
            String updatedFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = "/var/www/infiniteb2b/springboot/whitePapers" + File.separator + updatedFileName;
            try {
                Files.write(Paths.get(filePath), file.getBytes());
                existingSolutionSet.setFilePath(filePath);
                existingSolutionSet.setFileType(file.getContentType());
                existingSolutionSet.setName(updatedFileName);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ResponseUtils.createResponse1(null, "PDF Upload failed: " + e.getMessage(), false));
            }
        }

        if (image != null) {
            String updatedImageName = image.getOriginalFilename();
            String imagePath = "/var/www/infiniteb2b/springboot/whitepapersImages" + File.separator + updatedImageName;
//            imagePath = imagePath.replace(" ", "-");
            try {
                Files.write(Paths.get(imagePath), image.getBytes());
                existingSolutionSet.setImagePath(imagePath);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ResponseUtils.createResponse1(null, "Image upload failed: " + e.getMessage(), false));
            }
        }

        if (desc != null) {
            existingSolutionSet.setDescription(desc);
        }
        if (title != null) {
            existingSolutionSet.setTitle(title);
        }
        if (categoryEntity != null) {
            existingSolutionSet.setCategory(categoryEntity);
        }
        existingSolutionSet.setStatus(SolutionSetsStatus.PENDING);

        solutionSetsRepository.save(existingSolutionSet);

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(existingSolutionSet, "Solution set updated successfully", true));
    }


}

