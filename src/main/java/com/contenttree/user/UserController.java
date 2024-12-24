package com.contenttree.user;

import com.contenttree.Jwt.JwtResponse;

import com.contenttree.admin.AdminService;
import com.contenttree.admin.DashboardWidgetsResponse;
import com.contenttree.admin.Status;
import com.contenttree.admin.Widget;
import com.contenttree.category.Category;
import com.contenttree.category.CategoryDto;
import com.contenttree.category.CategoryRepository;
import com.contenttree.category.CategoryService;
import com.contenttree.downloadlog.DownloadLogRepository;
import com.contenttree.downloadlog.DownloadLogService;
import com.contenttree.security.UserJwtHelper;
import com.contenttree.solutionsets.SolutionSetResponse;
import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.solutionsets.SolutionSetsRepository;
import com.contenttree.solutionsets.SolutionSetsService;
import com.contenttree.userdatastorage.IpInfo;
import com.contenttree.userdatastorage.UserDataStorage;
import com.contenttree.userdatastorage.UserDataStorageRepository;
import com.contenttree.userdatastorage.UserDataStorageService;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ApiResponseTemplate;
import com.contenttree.utils.ResponseUtils;
import com.contenttree.vendor.VendorsService;
import io.github.classgraph.Resource;
import io.swagger.annotations.Api;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.IdGeneratorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.number.AbstractNumberFormatter;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.RedirectUrlBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;


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
    UserDataStorageRepository userDataStorageRepository;
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
                                          @RequestParam String email,
                                          @RequestParam String lastName,
                                          @RequestParam String country,
                                          @RequestParam String jobTitle,
                                          @RequestParam String company,
                                          @RequestParam long phone) throws MessagingException, IOException {

        User user = new User();
        user.setName(name);
        String hasCode = passwordEncoder.encode(password);
        user.setPassword(hasCode);
        user.setEmail(email);
        user.setCompany(company);
        user.setStatus(UserStatus.ACTIVE);
        user.setIsSubscriber(1);
        user.setCountry(country);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setJobTitle(jobTitle);
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

//    @GetMapping("/save-pdf")
//    public ResponseEntity<ApiResponse1<SolutionSets>> savePdf(@RequestParam long id,@AuthenticationPrincipal User user,HttpServletRequest request){
//        SolutionSets solutionSets = solutionSetsService.getSolutionSetById(id);
//        List<Long> oldSaved = user.getSavedPdf();
//        List<Long> updatedSaved = new ArrayList<>();
//
//        String clientIp = getClientIp(request);
//
//
//        if (oldSaved == null) {
//            oldSaved = new ArrayList<>();
//        }
//
//        if (!oldSaved.contains(id)) {
//            updatedSaved.add(id);
//        }
//
//        updatedSaved.addAll(oldSaved);
//        user.setSavedPdf(updatedSaved);
//        com.contenttree.userdatastorage.IpInfo info = getIpInfo(clientIp);
//        UserDataStorage userDataStorage = new UserDataStorage();
//        userDataStorage.setUser_id(user.getId());
//        userDataStorage.setIp(clientIp);
//        System.out.println("City name " + info.getCity());
//        userDataStorage.setCity(info.getCity() != null ? info.getCity() : "Unknown");
//        userDataStorage.setCountry(info.getCountry());
//        userDataStorage.setRegion(info.getRegion());
//        userDataStorage.setOrg(info.getOrg());
//        userDataStorage.setPostal(info.getPostal());
//
//        System.out.println("UserDataStorage " + userDataStorage);
//
//
//        userDataStorageService.addUserDataStorage(userDataStorage);
//
//        user.setIpAddress(clientIp);
//        userService.updateUser(user);
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(solutionSets,"SUCCESS",true));
//    }
    @PostMapping("/view-pdf")
    public ResponseEntity<byte[]> viewPdf(@RequestParam long id, @AuthenticationPrincipal User user, HttpServletRequest request) throws IOException {
        SolutionSets solutionSets = solutionSetsService.getSolutionSetById(id);

        byte[] pdfData = solutionSetsService.downloadPdf(id);

        List<Long> oldView = user.getSavedPdf();
        List<Long> updatedView = new ArrayList<>();

        if (oldView == null) {
            oldView = new ArrayList<>();
        }

        if (!oldView.contains(id)) {
            updatedView.add(id);
        }

        updatedView.addAll(oldView);

        user.setViewdPdf(updatedView);

        String clientIp = getClientIp(request);
        com.contenttree.userdatastorage.IpInfo info = getIpInfo(clientIp);
        UserDataStorage userDataStorage = new UserDataStorage();
        userDataStorage.setUser_id(user.getId());
        userDataStorage.setIp(clientIp);
        userDataStorage.setCity(info.getCity() != null ? info.getCity() : "Unknown");
        userDataStorage.setCountry(info.getCountry());
        userDataStorage.setRegion(info.getRegion());
        userDataStorage.setView(1);
        userDataStorage.setOrg(info.getOrg());
        userDataStorage.setPostal(info.getPostal());
        userDataStorageService.addUserDataStorage(userDataStorage);
        user.setIpAddress(clientIp);
        userService.updateUser(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("solution.pdf").build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData);
    }
//    @GetMapping("/get-whitepaper/{id}")
//    public ResponseEntity<ApiResponse1<Map<?,?>>> getWhitePaperById(@PathVariable long id,@AuthenticationPrincipal User user){
//        SolutionSets solutionSets = solutionSetsRepository.findById(id).orElse(null);
//        Optional<Category> category = categoryRepository.findById(solutionSets.getCategory().getId());
//        if (user!=null) {
//            int isSubscribe = user.getIsSubscriber();
//            Map map = new HashMap<>();
//            map.put("category", category);
//            map.put("whitePaper", solutionSets);
//            return ResponseEntity.ok().body(ResponseUtils.createResponse1(map, "SUCCESS", true));
//        }
//        else {
//            Map map = new HashMap<>();
//            map.put("category", category);
//            map.put("isSubscribe", 0);
//            map.put("whitePaper", solutionSets);
//            return ResponseEntity.ok().body(ResponseUtils.createResponse1(map, "SUCCESS", true));
//        }
//    }
@GetMapping("/get-whitepaper/{id}")
public ResponseEntity<ApiResponse1<Map<?, ?>>> getWhitePaperById(@PathVariable long id, @AuthenticationPrincipal User user) {
    SolutionSets solutionSets = solutionSetsRepository.findById(id).orElse(null);

    if (solutionSets == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseUtils.createResponse1(null, "Whitepaper not found", false));
    }

    Optional<Category> category = categoryRepository.findById(solutionSets.getCategory().getId());

    Map<String, Object> map = new HashMap<>();

    map.put("category", category.orElse(null));
    map.put("whitePaper", solutionSets);

    String whitePaperPath = "https://infiniteb2b.com/whitepaper";
    if (user != null) {
        Optional<UserDataStorage> userDataStorage = userDataStorageRepository.findByUserIdAndSaveAndSolutionSetId(user.getId(), solutionSets.getId());
        int isSaved = (userDataStorage.isPresent() && userDataStorage.get().getSave() == 1) ? 1 : 0;

        map.put("isSaved", isSaved);
        String filePath = "https://infiniteb2b.com" + solutionSets.getFilePath();
        map.put("whitepaperUrl",filePath);
        map.put("whitePaperPathForRedirection",whitePaperPath + "/" + solutionSets.getId());

        int isSubscribe = user.getIsSubscriber();
        map.put("isSubscribe", isSubscribe);
    } else {
        map.put("isSaved", 0);
        String filePath = "https://infiniteb2b.com" + solutionSets.getFilePath();
        map.put("whitepaperUrl",filePath);
    }

    return ResponseEntity.ok().body(ResponseUtils.createResponse1(map, "SUCCESS", true));
}
//@GetMapping("/get-whitepaper/{id}")
//public ResponseEntity<ApiResponse1<Map<?, ?>>> getWhitePaperById(@PathVariable long id, @AuthenticationPrincipal User user) {
//
//    // Step 1: Print the current authentication object to debug the user authentication.
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    if (authentication != null) {
//        System.out.println("Authentication object: " + authentication.getPrincipal());
//    } else {
//        System.out.println("No Authentication found in the SecurityContext.");
//    }
//
//    // Step 2: Log the user object injected by @AuthenticationPrincipal.
//    if (user != null) {
//        System.out.println("Authenticated user: " + user.getUsername());
//    } else {
//        System.out.println("User is not authenticated or @AuthenticationPrincipal could not resolve the user.");
//    }
//
//    // Step 3: Retrieve SolutionSets by ID
//    SolutionSets solutionSets = solutionSetsRepository.findById(id).orElse(null);
//    if (solutionSets == null) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(ResponseUtils.createResponse1(null, "Whitepaper not found", false));
//    }
//
//    // Step 4: Retrieve the associated category of the solution set
//    Optional<Category> category = categoryRepository.findById(solutionSets.getCategory().getId());
//
//    // Prepare the response map
//    Map<String, Object> map = new HashMap<>();
//    map.put("category", category.orElse(null));
//    map.put("whitePaper", solutionSets);
//
//    // Step 5: Check if the user is authenticated and has saved the whitepaper
//    if (user != null) {
//        // Check if the whitepaper is saved by the user
//        Optional<UserDataStorage> userDataStorage = userDataStorageRepository
//                .findByUserIdAndSaveAndSolutionSetId(user.getId(), solutionSets.getId());
//
//        int isSaved = (userDataStorage.isPresent() && userDataStorage.get().getSave() == 1) ? 1 : 0;
//        map.put("isSaved", isSaved);
//
//        // Prepare the whitepaper URL
//        String filePath = "https://infiniteb2b.com" + solutionSets.getFilePath();
//        System.out.println("Whitepaper URL: " + filePath);  // Debugging the file path
//        map.put("whitepaperUrl", filePath);
//
//        // Add subscription status
//        int isSubscribe = user.getIsSubscriber();
//        map.put("isSubscribe", isSubscribe);
//    } else {
//        // User is not authenticated or `@AuthenticationPrincipal` couldn't resolve the user
//        map.put("isSaved", 0);
//        map.put("whitepaperUrl", "not login");
//    }
//
//    // Step 6: Return response with the data
//    return ResponseEntity.ok().body(ResponseUtils.createResponse1(map, "SUCCESS", true));
//}


    @PostMapping("/save-pdf")
public ResponseEntity<ApiResponse1<SolutionSets>> savePdf(@RequestParam long id,@RequestParam int save, @AuthenticationPrincipal User user, HttpServletRequest request) {
    SolutionSets solutionSets = solutionSetsService.getSolutionSetById(id);
    List<Long> oldSaved = user.getSavedPdf();

    if (oldSaved == null) {
        oldSaved = new ArrayList<>();
    }

    if (!oldSaved.contains(id)) {
        oldSaved.add(id);
    }

    user.setSavedPdf(oldSaved);
    userService.updateUser(user);

    String clientIp = getClientIp(request);
    com.contenttree.userdatastorage.IpInfo info = getIpInfo(clientIp);
    UserDataStorage userDataStorage = new UserDataStorage();
    userDataStorage.setUser_id(user.getId());
    userDataStorage.setIp(clientIp);
    userDataStorage.setCity(info.getCity() != null ? info.getCity() : "Unknown");
    userDataStorage.setCountry(info.getCountry());
    userDataStorage.setRegion(info.getRegion());
    userDataStorage.setSolutionSetId(id);
    userDataStorage.setSave(save);
    userDataStorage.setOrg(info.getOrg());
    userDataStorage.setLocation(info.getLocation());
    userDataStorage.setTimezone(info.getTimeZone());
    userDataStorage.setPostal(info.getPostal());

    userDataStorageService.addUserDataStorage(userDataStorage);

    user.setIpAddress(clientIp);
    userService.updateUser(user);
    return ResponseEntity.ok().body(ResponseUtils.createResponse1(solutionSets, "SUCCESS", true));
}


//    @GetMapping("/download-pdf")
//    public ResponseEntity<byte[]> downloadPdf(@RequestParam long id, @AuthenticationPrincipal User user, HttpServletRequest request) {
//        SolutionSets solutionSets = solutionSetsService.getSolutionSetById(id);
//        byte[] pdfData = solutionSetsService.downloadPdf1(id);
//
//        List<Long> oldDownloads = user.getViewdPdf();
//        if (oldDownloads == null) {
//            oldDownloads = new ArrayList<>();
//        }
//        if (!oldDownloads.contains(id)) {
//            oldDownloads.add(id);
//        }
//        user.setViewdPdf(oldDownloads);
//        userService.updateUser(user);
//
//        String clientIp = getClientIp(request);
//        com.contenttree.userdatastorage.IpInfo info = getIpInfo(clientIp);
//
//        UserDataStorage userDataStorage = new UserDataStorage();
//        userDataStorage.setUser_id(user.getId());
//        userDataStorage.setIp(clientIp);
//        userDataStorage.setSolutionSetId(id);
//        userDataStorage.setCity(info.getCity() != null ? info.getCity() : "Unknown");
//        userDataStorage.setCountry(info.getCountry());
//        userDataStorage.setRegion(info.getRegion());
//        userDataStorage.setOrg(info.getOrg());
//        userDataStorage.setLocation(info.getLocation());
//        userDataStorage.setDownload(1);
//        userDataStorage.setTimezone(info.getTimeZone());
//        userDataStorage.setPostal(info.getPostal());
//        userDataStorageService.addUserDataStorage(userDataStorage);
//
//        user.setIpAddress(clientIp);
//        userService.updateUser(user);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDisposition(ContentDisposition.attachment().filename("solution-download.pdf").build());
//
//        return ResponseEntity.ok().headers(headers).body(pdfData);
//    }





//        @GetMapping("/download-pdf")
//public ResponseEntity<String> downloadSolutionSets(@RequestParam long id,
//                                                   @AuthenticationPrincipal User user,
//                                                   HttpServletRequest request) throws MessagingException, IOException {
//
//    String pdfData = solutionSetsService.downloadPdf(id);
//
//    if (pdfData == null || pdfData.isEmpty()) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }
//
//    String clientIp = getClientIp(request);
//
//    SolutionSets solutionSets = solutionSetsService.getSolutionSetById(id);
//    String categoryName = solutionSets.getCategory().getName();
//
//    List<String> oldFavorites = user.getFavorites();
//    List<String> updatedFavorites = new ArrayList<>();
//
//    com.contenttree.userdatastorage.IpInfo info = getIpInfo(clientIp);
//
//
//    if (oldFavorites == null) {
//        oldFavorites = new ArrayList<>();
//    }
//
//    if (!oldFavorites.contains(categoryName)) {
//        updatedFavorites.add(categoryName);
//    }
//
//    updatedFavorites.addAll(oldFavorites);
//
//    user.setFavorites(updatedFavorites);
//
//    downloadLogService.logPdfDownload(id, user.getId(),clientIp);
//    System.out.println("Downloading Done " + id + " " + user.getName());
//    System.out.println(updatedFavorites);
//    System.out.println("IP: " + clientIp);
//
//    UserDataStorage userDataStorage = new UserDataStorage();
//    userDataStorage.setUser_id(user.getId());
//    userDataStorage.setIp(clientIp);
//    userDataStorage.setDownload(1);
//    userDataStorage.setSolutionSetId(id);
//    System.out.println("City name " + info.getCity());
//    userDataStorage.setCity(info.getCity() != null ? info.getCity() : "Unknown");
//    userDataStorage.setCountry(info.getCountry());
//        userDataStorage.setLocation(info.getLocation());
//        userDataStorage.setTimezone(info.getTimeZone());
//    userDataStorage.setRegion(info.getRegion());
//    userDataStorage.setOrg(info.getOrg());
//    userDataStorage.setPostal(info.getPostal());
//
//    System.out.println("UserDataStorage " + userDataStorage);
//
//
//    userDataStorageService.addUserDataStorage(userDataStorage);
//
//    user.setIpAddress(clientIp);
//    userService.updateUser(user);
//
//    return ResponseEntity.ok()
//            .contentType(MediaType.APPLICATION_PDF)
//            .body(pdfData);
//}
//    if we want this in byte
@PostMapping("/download-pdf")
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

    downloadLogService.logPdfDownload(id, user.getId(), clientIp);
    System.out.println("Downloading Done " + id + " " + user.getName());
    System.out.println(updatedFavorites);
    System.out.println("IP: " + clientIp);

    UserDataStorage userDataStorage = new UserDataStorage();
    userDataStorage.setUser_id(user.getId());
    userDataStorage.setIp(clientIp);
    userDataStorage.setDownload(1);
    userDataStorage.setSolutionSetId(id);
    System.out.println("City name " + info.getCity());
    userDataStorage.setCity(info.getCity() != null ? info.getCity() : "Unknown");
    userDataStorage.setCountry(info.getCountry());
    userDataStorage.setLocation(info.getLocation());
    userDataStorage.setTimezone(info.getTimeZone());
    userDataStorage.setRegion(info.getRegion());
    userDataStorage.setOrg(info.getOrg());
    userDataStorage.setPostal(info.getPostal());

    System.out.println("UserDataStorage " + userDataStorage);

    userDataStorageService.addUserDataStorage(userDataStorage);

    user.setIpAddress(clientIp);
    userService.updateUser(user);

    return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"solution-set-" + id + ".pdf\"")
            .body(pdfData);
}
//    @Transactional
//    @PostMapping("/category/subscribe")
//    public ResponseEntity<ApiResponse1<User>> subscribeCategory(@AuthenticationPrincipal User user, @RequestParam long categoryId) {
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtils.createResponse1(null, "User not authenticated", false));
//        }
//
//        Category category = categoryRepository.findById(categoryId).orElse(null);
//        if (category == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.createResponse1(null, "Category not found", false));
//        }
//
//        List<String> favo = new ArrayList<>(user.getFavorites());
//        System.out.println("FAVORITE LIST " + favo);
//
//        if (!favo.contains(category.getName())) {
//            System.out.println("ADDING FAVO" + category.getName());
//            favo.add(category.getName());
//        } else {
//            System.out.println("REMOVING FAVO" + category.getName());
//            favo.remove(category.getName());
//        }
//
//        user.setFavorites(favo);
//        System.out.println("Setting FAVO " + category);
//        userRepository.save(user);
//        System.out.println("Saving FAVO" + category);
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(user, "SUCCESS", true));
//    }
@PostMapping("/category/subscribe")
public ResponseEntity<ApiResponse1<?>> toggleFavorite(@AuthenticationPrincipal User user, @RequestParam long categoryId) {
    User user1 = userRepository.findById(user.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Category category = categoryRepository.findById(categoryId).orElse(null);
    System.out.println("category " + category);

    if (category == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ResponseUtils.createResponse1(null, "Category not found", false)
        );
    }

    if (user1.getFavorites().contains(category.getName())) {
        System.out.println("contains " + category.getName());
        user1.getFavorites().remove(category.getName());
        System.out.println("removing " + category.getName());
    } else {
        user1.getFavorites().add(category.getName());
        System.out.println("adding " + category.getName());
    }

    userRepository.save(user1);
    System.out.println();

    System.out.println("Favorites " + user1.getFavorites());

    return ResponseEntity.ok().body(ResponseUtils.createResponse1(user1.getFavorites(), "SUCCESS", true));
}





//    @PostMapping("/download-pdf")
//    public ResponseEntity<String> downloadSolutionSetsString(@RequestParam long id,
//                                                       @AuthenticationPrincipal User user,
//                                                       HttpServletRequest request) throws MessagingException, IOException {
//
//        byte[] pdfData = solutionSetsService.downloadPdf(id);
//
//        if (pdfData == null || pdfData.length == 0) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        String clientIp = getClientIp(request);
//
//        SolutionSets solutionSets = solutionSetsService.getSolutionSetById(id);
//        String categoryName = solutionSets.getCategory().getName();
//
//        List<String> oldFavorites = user.getFavorites();
//        List<String> updatedFavorites = new ArrayList<>();
//
//        com.contenttree.userdatastorage.IpInfo info = getIpInfo(clientIp);
//
//        if (oldFavorites == null) {
//            oldFavorites = new ArrayList<>();
//        }
//
//        if (!oldFavorites.contains(categoryName)) {
//            updatedFavorites.add(categoryName);
//        }
//
//        updatedFavorites.addAll(oldFavorites);
//
//        user.setFavorites(updatedFavorites);
//
//        downloadLogService.logPdfDownload(id, user.getId(), clientIp);
//        System.out.println("Downloading Done " + id + " " + user.getName());
//        System.out.println(updatedFavorites);
//        System.out.println("IP: " + clientIp);
//
//        UserDataStorage userDataStorage = new UserDataStorage();
//        userDataStorage.setUser_id(user.getId());
//        userDataStorage.setIp(clientIp);
//        userDataStorage.setDownload(1);
//        userDataStorage.setSolutionSetId(id);
//        System.out.println("City name " + info.getCity());
//        userDataStorage.setCity(info.getCity() != null ? info.getCity() : "Unknown");
//        userDataStorage.setCountry(info.getCountry());
//        userDataStorage.setLocation(info.getLocation());
//        userDataStorage.setTimezone(info.getTimeZone());
//        userDataStorage.setRegion(info.getRegion());
//        userDataStorage.setOrg(info.getOrg());
//        userDataStorage.setPostal(info.getPostal());
//
//        System.out.println("UserDataStorage " + userDataStorage);
//
//        userDataStorageService.addUserDataStorage(userDataStorage);
//
//        user.setIpAddress(clientIp);
//        userService.updateUser(user);
//
//        String base64Pdf = Base64.getEncoder().encodeToString(pdfData);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_PDF)
//                .body("{\"pdfData\": \"" + base64Pdf + "\"}");
//    }


//@GetMapping("/download-pdf")
//public ResponseEntity<String> downloadSolutionSets(@RequestParam long id,
//                                                   @AuthenticationPrincipal User user,
//                                                   HttpServletRequest request) throws MessagingException, IOException {
//
//    String fileUrl = solutionSetsService.downloadPdf(id, request);
//
//    if (fileUrl == null || fileUrl.isEmpty()) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
//    }
//
//    String clientIp = getClientIp(request);
//
//    SolutionSets solutionSets = solutionSetsService.getSolutionSetById(id);
//    String categoryName = solutionSets.getCategory().getName();
//
//    List<String> oldFavorites = user.getFavorites();
//    List<String> updatedFavorites = new ArrayList<>();
//
//    if (oldFavorites == null) {
//        oldFavorites = new ArrayList<>();
//    }
//
//    if (!oldFavorites.contains(categoryName)) {
//        updatedFavorites.add(categoryName);
//    }
//
//    updatedFavorites.addAll(oldFavorites);
//
//    user.setFavorites(updatedFavorites);
//
//    downloadLogService.logPdfDownload(id, user.getId(), clientIp);
//    System.out.println("Downloading Done " + id + " " + user.getName());
//    System.out.println(updatedFavorites);
//    System.out.println("IP: " + clientIp);
//
//    UserDataStorage userDataStorage = new UserDataStorage();
//    userDataStorage.setUser_id(user.getId());
//    userDataStorage.setIp(clientIp);
//    userDataStorage.setDownload(1);
//    userDataStorage.setSolutionSetId(id);
//    com.contenttree.userdatastorage.IpInfo info = getIpInfo(clientIp);
//    System.out.println("City name " + info.getCity());
//    userDataStorage.setCity(info.getCity() != null ? info.getCity() : "Unknown");
//    userDataStorage.setCountry(info.getCountry());
//    userDataStorage.setLocation(info.getLocation());
//    userDataStorage.setTimezone(info.getTimeZone());
//    userDataStorage.setRegion(info.getRegion());
//    userDataStorage.setOrg(info.getOrg());
//    userDataStorage.setPostal(info.getPostal());
//
//    System.out.println("UserDataStorage " + userDataStorage);
//
//    userDataStorageService.addUserDataStorage(userDataStorage);
//
//    user.setIpAddress(clientIp);
//    userService.updateUser(user);
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.TEXT_PLAIN);
//    headers.set("Access-Control-Allow-Origin", "*"); // Allow requests from any origin
//
//    return ResponseEntity.ok().headers(headers).body(fileUrl);
//}
//    @GetMapping("/download-pdf")
//    public ResponseEntity<?> downloadSolutionSets(@RequestParam long id,
//                                                         @AuthenticationPrincipal User user,
//                                                         HttpServletRequest request) throws MessagingException, IOException {
//        String filePath = solutionSetsService.downloadPdf(id, request);
//
//        if (filePath == null || filePath.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        File file = new File(filePath);
//        if (!file.exists()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        String clientIp = getClientIp(request);
//
//        SolutionSets solutionSets = solutionSetsService.getSolutionSetById(id);
//        String categoryName = solutionSets.getCategory().getName();
//
//        List<String> oldFavorites = user.getFavorites();
//        List<String> updatedFavorites = new ArrayList<>();
//        if (oldFavorites == null) {
//            oldFavorites = new ArrayList<>();
//        }
//        if (!oldFavorites.contains(categoryName)) {
//            updatedFavorites.add(categoryName);
//        }
//        updatedFavorites.addAll(oldFavorites);
//        user.setFavorites(updatedFavorites);
//
//        downloadLogService.logPdfDownload(id, user.getId(), clientIp);
//
//        UserDataStorage userDataStorage = new UserDataStorage();
//        userDataStorage.setUser_id(user.getId());
//        userDataStorage.setIp(clientIp);
//        userDataStorage.setDownload(1);
//        userDataStorage.setSolutionSetId(id);
//        com.contenttree.userdatastorage.IpInfo info = getIpInfo(clientIp);
//        userDataStorage.setCity(info.getCity() != null ? info.getCity() : "Unknown");
//        userDataStorage.setCountry(info.getCountry());
//        userDataStorage.setLocation(info.getLocation());
//        userDataStorage.setTimezone(info.getTimeZone());
//        userDataStorage.setRegion(info.getRegion());
//        userDataStorage.setOrg(info.getOrg());
//        userDataStorage.setPostal(info.getPostal());
//        userDataStorageService.addUserDataStorage(userDataStorage);
//
//        user.setIpAddress(clientIp);
//        userService.updateUser(user);
//
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//        String contentDisposition = "attachment; filename=\"" + file.getName() + "\"";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentLength(file.length());
//        headers.set("Access-Control-Allow-Origin", "*");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(resource);
//    }




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
    @GetMapping("")





    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
//    @GetMapping("/confirm-account")
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        System.out.println("in the confirm account portal");

        return userService.confirmEmail(confirmationToken);
    }
    @PostMapping("/upadate-status")
    public ResponseEntity<ApiResponse1<User>> upadteUserStatus(@AuthenticationPrincipal User loggedInUser,@RequestParam long id){

        if (id==1){
            loggedInUser.setStatus(UserStatus.ACTIVE);
        }
        else {
            loggedInUser.setStatus(UserStatus.INACTIVE);
        }
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(loggedInUser,"SUCCESS",true));

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
        if ("INACTIVE".equals(user.getStatus())) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "User account is not active.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
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
                .username(user.getName()+ " "+user.getLastName())
                .id(user.getId())
                .build();

        ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(jwtResponse, "Login Successful", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/subscribecategory")
    public ResponseEntity<ApiResponse1<User>> unSubscribe(@RequestParam int subscribe,@AuthenticationPrincipal User user) throws MessagingException, IOException {
        User user1 = userRepository.findById(user.getId()).orElse(null);
        user1.setIsSubscriber(subscribe);
        userService.saveUser(user1);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(user1,"SUCCESS",true));
    }


    @PutMapping("/unsave-whitepaper")
    public ResponseEntity<ApiResponse1<UserDataStorage>> unsaveWhitePaper(@RequestParam long id,@AuthenticationPrincipal User user){
        UserDataStorage userDataStorage = userDataStorageRepository.findByUserIdAndSaveAndSolutionSetId(user.getId(),id).orElse(null);
        assert userDataStorage != null;
        userDataStorage.setSave(0);
        userDataStorageService.addUserDataStorage(userDataStorage);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(userDataStorage,"SUCCESS",true));

    }


    @GetMapping("/dashboard")
        public ResponseEntity<DashboardWidgetsResponse> getWidgets(@AuthenticationPrincipal User user) {
            List<Widget> ecomWidgets = new ArrayList<>();
            List<Widget> totalecomWidgets = new ArrayList<>();

            List<UserDataStorage> userDataStorageList = userDataStorageRepository.findAll();
            List<User> userList = userRepository.findAll();
            long whitePaperSaved = userDataStorageList.stream()
                    .filter(u -> u.getUser_id() == user.getId() && u.getSave() == 1)
                    .count();
            long whitePaperCategorySubscribed = userList.size();
            long whitePapersDownloaded= userDataStorageList.stream()
                    .filter(u -> u.getUser_id() == user.getId() && u.getDownload() == 1)
                    .count();
//            long newsLetterSubscribed = userList.stream().filter(u-> u.getNewsLetterList().stream().count()).count();



            ecomWidgets.add(new Widget(1L, "primary", "SAVED", String.valueOf(whitePaperSaved), "View All", "secondary", "bx bx-file", 0));
            ecomWidgets.add(new Widget(1L, "primary", "DOWNLOADED", String.valueOf(whitePapersDownloaded), "View All", "secondary", "bx bx-file", 0));
            ecomWidgets.add(new Widget(2L, "secondary", "WHITE-PAPERS SET", String.valueOf(whitePaperCategorySubscribed), "View All", "primary", "bx bx-book", 0));
            ecomWidgets.add(new Widget(3L, "success", "NEWS-LETTER", "Yes", "Update", "success", "bx bx-user-circle", 0));

            DashboardWidgetsResponse response = new DashboardWidgetsResponse();
            response.setEcomWidgets(ecomWidgets);
            response.setTotalecomWidgets(totalecomWidgets);

            return ResponseEntity.ok(response);
        }


    @GetMapping("/allwhitepapers-saved")
    public ResponseEntity<ApiResponse1<List<SolutionSetResponse>>> getAllWhitePapersSaved(
            @AuthenticationPrincipal User user) {

        List<UserDataStorage> userDataStorages = userDataStorageRepository.findAll();

        List<UserDataStorage> savedWhitepapers = userDataStorages.stream()
                .filter(u -> u.getUser_id() == user.getId() && u.getSave() == 1)
                .collect(Collectors.toList());

        List<SolutionSetResponse> responseList = new ArrayList<>();

        for (UserDataStorage savedWhitepaper : savedWhitepapers) {
            SolutionSets solutionSet = solutionSetsService.getSolutionSetById(savedWhitepaper.getSolutionSetId());

            if (solutionSet != null) {
                Category category = categoryRepository.findById(solutionSet.getCategory().getId()).orElse(null);
                String categoryName = category != null ? category.getName() : "Unknown";

                SolutionSetResponse response = new SolutionSetResponse();
                response.setId(solutionSet.getId());
                response.setWhitePaperName(solutionSet.getTitle());
                response.setWhitePaperSetName(categoryName);

                responseList.add(response);
            }
        }

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(responseList, "SUCCESS", true));
    }

    @GetMapping("/allwhitepapers-download")
    public ResponseEntity<ApiResponse1<List<?>>> getAllWhitePapersDownload(@AuthenticationPrincipal User user){
        List<UserDataStorage> userDataStorages = userDataStorageRepository.findAll();

        List<UserDataStorage> savedWhitepapers = userDataStorages.stream()
                .filter(u -> u.getUser_id() == user.getId() && u.getDownload() == 1)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(savedWhitepapers, "SUCCESS", true));
    }
    @GetMapping("/allwhitepapers-viewd")
    public ResponseEntity<ApiResponse1<List<?>>> getAllWhitePapersViewed(@AuthenticationPrincipal User user){
        List<UserDataStorage> userDataStorages = userDataStorageRepository.findAll();

        List<UserDataStorage> savedWhitepapers = userDataStorages.stream()
                .filter(u -> u.getUser_id() == user.getId() && u.getView() == 1)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(savedWhitepapers, "SUCCESS", true));
    }
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
    @GetMapping("/getall-favortie-category")
    public ResponseEntity<ApiResponse1<List<?>>> getAllFavoritesCategoryByUser(@AuthenticationPrincipal User user){
        List<String> favoritesList = user.getFavorites();
        List<Category> categoryList = new ArrayList<>();
        for (String category : favoritesList){
            Category category1 = categoryService.getCategoryByName(category);

            categoryList.add(category1);
        }
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(categoryList,"SUCCESS",true));
    }

    @PutMapping("/remove-favorite")
    @Transactional
    public ResponseEntity<ApiResponse1<?>> removeFavoritesCategoryByUser(@AuthenticationPrincipal User user, @RequestParam long id) throws MessagingException, IOException {
        List<String> favoritesList = user.getFavorites();

        Category category = categoryRepository.findById(id).orElse(null);

        if (category != null && favoritesList.contains(category.getName())) {
            favoritesList.remove(category.getName());

            System.out.println("Removing category: " + category.getName());

            user.setFavorites(favoritesList);

            userService.saveUser(user);

            userRepository.flush();

            return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "SUCCESS", true));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtils.createResponse1(null, "Category not found or not in favorites", false));
        }
    }
    @Autowired
    SolutionSetsRepository solutionSetsRepository;
    @GetMapping("/solution-sets-homepage")
    public ResponseEntity<ApiResponse1<Map<?,?>>> getCategoryForHomePage(@AuthenticationPrincipal User user) {
        List<SolutionSets> solutionSets = solutionSetsRepository.findAll();

        solutionSets.sort((set1, set2) -> Long.compare(set2.getId(), set1.getId()));

        List<SolutionSets> updatedCategory = solutionSets.stream()
                .limit(24)
                .toList();



        if (user != null) {
            List<UserDataStorage> userDataStorageList = userDataStorageRepository.findAll();

            Map<Long, UserDataStorage> userDataStorageMap = userDataStorageList.stream()
                    .filter(userData -> userData.getUser_id() == user.getId())
                    .collect(Collectors.toMap(UserDataStorage::getSolutionSetId, userData -> userData));

            List<Map<String, Object>> maps = updatedCategory.stream().map(category -> {
                Map<String, Object> categoryMap = new HashMap<>();
                categoryMap.put("title", category.getTitle());
                categoryMap.put("id", category.getId());
                categoryMap.put("imgSrc", category.getImagePath());
                categoryMap.put("description", category.getDescription());
                categoryMap.put("category", category.getCategory().getName());
                categoryMap.put("category_id", category.getCategory().getId());

                UserDataStorage userData = userDataStorageMap.get(category.getId());
                categoryMap.put("isSavedByUser", userData != null && userData.getSave() > 0 ? 1 : 0);

                return categoryMap;
            }).collect(Collectors.toList());

            Map<String,Long> statsCount = new HashMap<>();
            long userCount = userService.getAllUsers().stream().count();
            long whitePaperCount = solutionSets.stream().count();
            long categoryCount = categoryRepository.findAll().stream().count();
            long vendorCount = vendorsService.getAllVendors().stream().count();
            statsCount.put("userCount",userCount);
            statsCount.put("whitePaperCount",whitePaperCount);
            statsCount.put("categoryCount",categoryCount);
            statsCount.put("vendorCount",vendorCount);

            List<SolutionSets> curatedWhitePapers = solutionSetsRepository.findAll()
                    .stream()
                    .limit(9)
                    .collect(Collectors.toList());

            List<Category> topicsThatMatterYou = categoryRepository.findAll()
                    .stream()
                    .limit(14)
                    .collect(Collectors.toList());
            Map<String,Object> map = new HashMap<>();
            map.put("poweringYourBusiness",maps);
            map.put("curatedWhitePapers",curatedWhitePapers);
            map.put("topicsThatMatterYou",topicsThatMatterYou);
            map.put("STATISTICS",statsCount);


            return ResponseEntity.ok().body(ResponseUtils.createResponse1(map, "SUCCESS", true));
        } else {
            List<Map<String, Object>> maps = updatedCategory.stream().map(category -> {
                Map<String, Object> categoryMap = new HashMap<>();
                categoryMap.put("title", category.getTitle());
                categoryMap.put("id", category.getId());
                categoryMap.put("imgSrc", category.getImagePath());
                categoryMap.put("description", category.getDescription());
                categoryMap.put("category", category.getCategory().getName());
                categoryMap.put("category_id", category.getCategory().getId());

                categoryMap.put("isSavedByUser", 0);

                return categoryMap;
            }).collect(Collectors.toList());
            Map<String,Long> statsCount = new HashMap<>();
            long userCount = userService.getAllUsers().stream().count();
            long whitePaperCount = solutionSets.stream().count();
            long categoryCount = categoryRepository.findAll().stream().count();
            long vendorCount = vendorsService.getAllVendors().stream().count();
            statsCount.put("userCount",userCount);
            statsCount.put("whitePaperCount",whitePaperCount);
            statsCount.put("categoryCount",categoryCount);
            statsCount.put("vendorCount",vendorCount);
            List<SolutionSets> curatedWhitePapers = solutionSetsRepository.findAll()
                    .stream()
                    .limit(9)
                    .collect(Collectors.toList());

            List<Category> topicsThatMatterYou = categoryRepository.findAll()
                    .stream()
                    .limit(14)
                    .collect(Collectors.toList());
            Map<String,Object> map = new HashMap<>();
            map.put("poweringYourBusiness",maps);
            map.put("curatedWhitePapers",curatedWhitePapers);
            map.put("topicsThatMatterYou",topicsThatMatterYou);
            map.put("STATISTICS",statsCount);

            return ResponseEntity.ok().body(ResponseUtils.createResponse1(map, "SUCCESS", true));
        }
    }

    @PutMapping("/update/whitepaper-filepath")
    public ResponseEntity<String> updateFilePath() {
        List<SolutionSets> solutionSetsList = solutionSetsRepository.findAll();

        solutionSetsList.forEach(solutionSets -> {
            String updatedImagePath = solutionSets.getImagePath() != null ? solutionSets.getImagePath().replace(" ", "-") : "";
            solutionSets.setImagePath(updatedImagePath);

            String updatedFilePath = solutionSets.getFilePath() != null ? solutionSets.getFileType().replace(" ", "-") : "";
            solutionSets.setFilePath(updatedFilePath);
        });
        solutionSetsRepository.saveAll(solutionSetsList);
        return ResponseEntity.ok("File paths updated successfully");
    }
    @PostMapping("/update-details")
    public ResponseEntity<ApiResponse1<String>> updateUserDetails(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Long phone,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String password) {

        User updatedUser = userService.updateUserDetails(user.getId(), name, lastName, country, phone, jobTitle, company, password);

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null, "Details updated successfully", true));
    }
    @GetMapping("/view-all-saved")
    public ResponseEntity<ApiResponse1<Map<?,?>>> getAllViewSaved(@AuthenticationPrincipal User user){
        List<UserDataStorage> userDataStorage = userDataStorageRepository.findByUserIdList(user.getId());
        List<UserDataStorage> filteredData=userDataStorage.stream().filter(userDataStorage1 -> userDataStorage1.getSave()==1&&userDataStorage1.getUser_id()==user.getId()).toList();

        int count=filteredData.size();
        List<SolutionSets> solutionSets = new ArrayList<>();
        for (UserDataStorage userData : filteredData) {
            Long solutionSetId = userData.getSolutionSetId();

            Optional<SolutionSets> solutionSetOptional = solutionSetsRepository.findById(solutionSetId);

            solutionSetOptional.ifPresent(solutionSets::add);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("totalSavedCout", (long) filteredData.size());
        System.out.println("filterdata"+count);
        map.put("allSaved",solutionSets);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(map,"SUCCESS",true));


    }
    @GetMapping("/view-all-downloaded")
    public ResponseEntity<ApiResponse1<Map<?,?>>> getAllViewDownloaded(@AuthenticationPrincipal User user){
        List<UserDataStorage> userDataStorage = userDataStorageRepository.findByUserIdList(user.getId());
        List<UserDataStorage> filteredData=userDataStorage.stream().filter(userDataStorage1 -> userDataStorage1.getDownload()==1&&userDataStorage1.getUser_id()==user.getId()).toList();

        int count=filteredData.size();
        List<SolutionSets> solutionSets = new ArrayList<>();
        for (UserDataStorage userData : filteredData) {
            Long solutionSetId = userData.getSolutionSetId();

            Optional<SolutionSets> solutionSetOptional = solutionSetsRepository.findById(solutionSetId);

            solutionSetOptional.ifPresent(solutionSets::add);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("totalDownloadsCount", (long) filteredData.size());
        System.out.println("filterdata"+count);
        map.put("allDownloaded",solutionSets);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(map,"SUCCESS",true));


    }
    @GetMapping("/view-all-viewed")
    public ResponseEntity<ApiResponse1<Map<?,?>>> getAllViewViewed(@AuthenticationPrincipal User user){
        List<UserDataStorage> userDataStorage = userDataStorageRepository.findByUserIdList(user.getId());
        List<UserDataStorage> filteredData=userDataStorage.stream().filter(userDataStorage1 -> userDataStorage1.getView()==1&&userDataStorage1.getUser_id()==user.getId()).toList();

        int count=filteredData.size();
        List<SolutionSets> solutionSets = new ArrayList<>();
        for (UserDataStorage userData : filteredData) {
            Long solutionSetId = userData.getSolutionSetId();

            Optional<SolutionSets> solutionSetOptional = solutionSetsRepository.findById(solutionSetId);

            solutionSetOptional.ifPresent(solutionSets::add);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("totalViewedCount", (long) filteredData.size());
        System.out.println("filterdata"+count);
        map.put("allViewed",solutionSets);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(map,"SUCCESS",true));


    }









}

