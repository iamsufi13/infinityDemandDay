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
import com.contenttree.downloadlog.DownloadLog;
import com.contenttree.downloadlog.DownloadLogDto;
import com.contenttree.downloadlog.DownloadLogMapper;
import com.contenttree.downloadlog.DownloadLogRepository;
import com.contenttree.security.AdminJwtHelper;
import com.contenttree.solutionsets.*;
import com.contenttree.user.User;
import com.contenttree.user.UserService;
import com.contenttree.userdatastorage.UserDataStorage;
import com.contenttree.userdatastorage.UserDataStorageRepository;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ApiResponse2;
import com.contenttree.utils.ResponseUtils;
import com.contenttree.vendor.VendorRepository;
import com.contenttree.vendor.VendorStatus;
import com.contenttree.vendor.Vendors;
import com.contenttree.vendor.VendorsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

//    @PostMapping("/register/admin")
//    public ResponseEntity<ApiResponse1<String>> registerAdmin(@RequestParam String email,
//                                                              @RequestParam String password,
//                                                              @RequestParam String name,
//                                                              @RequestParam int isSuperAdmin) {
//        Admin admin = new Admin();
//        admin.setName(name);
//        admin.setEmail(email);
//        String hashcode = passwordEncoder.encode(password);
//        admin.setPassword(hashcode);
//        List<Role> roles = new ArrayList<>();
//
//        if (isSuperAdmin == 1) {
//            roles.add(Role.SUPERADMIN);
//        } else if (isSuperAdmin == 2) {
//            roles.add(Role.CAMPAIGNMANAGER);
//        } else if (isSuperAdmin == 3) {
//            roles.add(Role.ADMIN);
//        } else {
//            roles.add(Role.ADMIN);
//        }
//
//        admin.setRole(roles);
//
//        if (vendorsService.getVendorsByEmail(email) == null && userService.getUserByEmail(email) == null) {
//            adminService.registerAdmin(admin);
//            ApiResponse1<String> response = ResponseUtils.createResponse1("Admin Registered Successfully", "Success", true);
//            return ResponseEntity.ok(response);
//        }
//
//        ApiResponse1<String> response = ResponseUtils.createResponse1("Email Already Registered. Please Try With Another Email", "Failure", false);
//        return ResponseEntity.ok(response);
//    }
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


    //    @PostMapping("/login/admin")
//    public ResponseEntity<ApiResponse1<HashMap<String, Object>>> adminLogin(@RequestParam String email,
//                                                                            @RequestParam String password) {
//
//        Admin admin = adminService.getAdminByEmailId(email);
//        log.info("ADMIN LOADED {}", admin);
//        log.info("ADMIN Authority {}", admin.getAuthorities());
//
//        if (!passwordEncoder.matches(password, admin.getPassword())) {
//            ApiResponse1<HashMap<String, Object>> response = ResponseUtils.createResponse1(
//                    null,
//                    "Email & Password Does Not Match",
//                    false
//            );
//            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//        }
//
//        String token = this.jwtHelper.generateToken(admin);
//        JwtResponse jwtResponse = JwtResponse.builder()
//                .jwtToken(token)
//                .username(admin.getEmail())
//                .build();
//
//        HashMap<String, Object> responseMap = new HashMap<>();
//        responseMap.put("jwtToken", jwtResponse);
//
//        ApiResponse1<HashMap<String, Object>> response = ResponseUtils.createResponse1(
//                responseMap,
//                "Admin Logged in Successfully",
//                true
//        );
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//@PostMapping("/login/admin")
//public ResponseEntity<ApiResponseTemplate<Map<String, Object>>> adminLogin(
//        @RequestParam String email,
//        @RequestParam String password) {
//
//    Admin admin = adminService.getAdminByEmailId(email);
//    if (admin == null || !passwordEncoder.matches(password, admin.getPassword())) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(ResponseUtils.createResponse( "Email & Password Do Not Match", null, null));
//    }
//
//    String token = jwtHelper.generateToken(admin);
//
//    Map<String, Object> responseData = new HashMap<>();
//    Map<String, Object> userDetails = new HashMap<>();
//    userDetails.put("id", admin.getId());
//    userDetails.put("name", admin.getName());
//    userDetails.put("email", admin.getEmail());
//
//    responseData.put("user", userDetails);
//
//    ApiResponseTemplate<Map<String, Object>> response = ResponseUtils.createResponse( "success", token, responseData);
//    return ResponseEntity.ok().body(response);
//}
@PostMapping("/login/admin")
public ResponseEntity<Map<String, Object>> adminLogin(
        @RequestParam String email,
        @RequestParam String password) {

    Admin admin = adminService.getAdminByEmailId(email);
    if (admin == null || !passwordEncoder.matches(password, admin.getPassword())) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("message", "Email & Password Do Not Match");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    String token = jwtHelper.generateToken(admin);

    Map<String, Object> userDetails = new HashMap<>();
    userDetails.put("_id",admin.getId());
    userDetails.put("email", admin.getEmail());
    userDetails.put("first_name", admin.getName());
    List<Role> role = admin.getRole();
    userDetails.put("role",role.get(0));
    userDetails.put("password", "********");
    userDetails.put("confirm_password", "********");

    Map<String, Object> response = new HashMap<>();
    response.put("message", "success");
    response.put("token", token);
    response.put("data", userDetails);

    return ResponseEntity.ok(response);
}






    @PutMapping("/admin/approve-vendor")
    public ResponseEntity<ApiResponse1<String>> approveVendor(@RequestParam long vendorId){
        Vendors vendor = vendorRepository.findById(vendorId).orElseThrow
                (()-> new RuntimeException("Vendor not Found"));
        System.out.println(vendor);
        vendor.setStatus(VendorStatus.APPROVED);
        vendorRepository.save(vendor);
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
    public ResponseEntity<ApiResponse1<String>> rejectVendor(@RequestParam long vendorId){
        Vendors vendor = vendorRepository.findById(vendorId).orElseThrow(()-> new RuntimeException("Vendor Not Found"));
        vendor.setStatus(VendorStatus.REJECTED);
        vendorRepository.save(vendor);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Vendor Rejected SuccessFully", true));
    }
    @PutMapping("/admin/inactive-vendor")
    public ResponseEntity<ApiResponse1<String>> inactiveVendor(@RequestParam long vendorId){
        Vendors vendor = vendorRepository.findById(vendorId).orElseThrow(()-> new RuntimeException("Vendor Not Found"));
        vendor.setStatus(VendorStatus.REJECTED);
        vendorRepository.save(vendor);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Vendor inactive SuccessFully", true));
    }
    @PutMapping("/admin/approve-solutionset")
    public ResponseEntity<ApiResponse1<String>> approveSolution(@RequestParam Long solutionId) {
        log.info("inside the Admin controller");
        SolutionSets solutionSet = solutionSetsRepository.findById(solutionId)
                .orElseThrow(() -> new RuntimeException("Solution Set not found"));
        solutionSet.setStatus(SolutionSetsStatus.APPROVED);
        solutionSetsRepository.save(solutionSet);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Solution set approved successfully",true));
    }

    @PutMapping("/admin/reject-solutionset")
    public ResponseEntity<ApiResponse1<String>> rejectSolution(@RequestParam Long solutionId) {
        SolutionSets solutionSet = solutionSetsRepository.findById(solutionId)
                .orElseThrow(() -> new RuntimeException("Solution Set not found"));
        solutionSet.setStatus(SolutionSetsStatus.REJECTED); // Reject PDF
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

            String categoryName = (blog.getBlogsCategory() != null) ? blog.getBlogsCategory().getBlogCategoryName() : "Unknown";
            map.put("category", categoryName);

//            map.put("totalViews", blog.getViews() != null ? blog.getViews() : 0);
            map.put("totalViews",  0);

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
    public ResponseEntity<ApiResponse1> getAllBlogsCategory() {
        List<BlogsCategory> list = blogsCategoryRepository.findAll();

        List<Map<String, Object>> responseData = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        for (BlogsCategory blog : list) {
            Map<String, Object> map = new LinkedHashMap<>();

            map.put("name", blog.getBlogCategoryName());

            String categoryName = (blog.getBlogCategoryDescp() != null) ? blog.getBlogCategoryDescp(): "Unknown";
            map.put("description", categoryName);

//            map.put("totalViews", blog.getViews() != null ? blog.getViews() : 0);
            map.put("totalBlogsPosted",  blog.getBlogs().stream().count());

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

//            String categoryName = (newsletter.get() != null) ? newsletter.getCategory().getName() : "Unknown";
            map.put("category", "Name");

//            map.put("totalViews", newsletter.getViews() != null ? newsletter.getViews() : 0);
            map.put("totalViews", 0);

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
            .filter(vendor -> "PENDING".equalsIgnoreCase(String.valueOf(vendor.getStatus())))
            .collect(Collectors.toList());
    return ResponseEntity.ok().body(ResponseUtils.createResponse1(updatedList,"SUCCESS",true));
}
    @GetMapping("/admin/get-all-approved-vendors")
    public ResponseEntity<ApiResponse1<List<Vendors>>> getAllApprovedVendorList(){
        List<Vendors> list = vendorRepository.findAll();
        List<Vendors> updatedList = list.stream()
                .filter(vendor -> "APPROVED".equalsIgnoreCase(String.valueOf(vendor.getStatus())))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(updatedList,"SUCCESS",true));
    }
    @GetMapping("/admin/getadmin")
public ResponseEntity<ApiResponse1<List<Admin>>> getAllAdminList() {
    // Fetch all admins from the repository
    List<Admin> list = adminService.adminRepository.findAll();

    // Filter for SUPERADMIN role and sort by name (or any desired field)
    List<Admin> updatedList = list.stream()
            .filter(admin -> admin.getAuthorities().stream()
                    .anyMatch(auth -> "SUPERADMIN".equalsIgnoreCase(auth.getAuthority())))
            .sorted(Comparator.comparing(Admin::getName)) // Sort by name (or change field as needed)
            .collect(Collectors.toList());

    // Create the response
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
            .filter(Objects::nonNull)
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
                        .map(authority -> authority.getAuthority())
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
                            .map(authority -> authority.getAuthority())
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
//        List<Vendors> list = vendorRepository.findAll();
//        list.stream().filter(vendors -> "APPROVED".equalsIgnoreCase(String.valueOf(vendors.getStatus()))).collect(Collectors.toList());;
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS",true));
            List<Vendors> list = vendorRepository.findAll();
            List<Vendors> updatedList = list.stream()
                    .filter(vendor -> "APPROVED".equalsIgnoreCase(String.valueOf(vendor.getStatus())))
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
    public ResponseEntity<ApiResponse1<List<User>>> getAllUserList(){
        List<User> list = userService.getAllUsers();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS",true));
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
@GetMapping("/admin/get-allwhitepapers")
public ResponseEntity<ApiResponse1<List<SolutionSetDto>>> getAllWhitePapersList() {
    try {
        List<SolutionSets> list = solutionSetsRepository.findAll();
        System.out.println("Fetched list: " + list.size());
        list.forEach(System.out::println);

        List<SolutionSetDto> solutionSetDtos = list.stream()
                .map(solutionSetMapper::toSolutionSetDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(solutionSetDtos, "SUCCESS", true));
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


}
