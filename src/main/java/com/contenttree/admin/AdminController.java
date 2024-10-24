package com.contenttree.admin;

import com.contenttree.Jwt.JwtResponse;
import com.contenttree.downloadlog.DownloadLog;
import com.contenttree.downloadlog.DownloadLogDto;
import com.contenttree.downloadlog.DownloadLogMapper;
import com.contenttree.downloadlog.DownloadLogRepository;
import com.contenttree.security.AdminJwtHelper;
import com.contenttree.security.JwtHelper;
import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.solutionsets.SolutionSetsRepository;
import com.contenttree.solutionsets.SolutionSetsStatus;
import com.contenttree.user.User;
import com.contenttree.user.UserService;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import com.contenttree.vendor.VendorRepository;
import com.contenttree.vendor.VendorStatus;
import com.contenttree.vendor.Vendors;
import com.contenttree.vendor.VendorsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Slf4j
@CrossOrigin("http://localhost:3000/")
public class AdminController
{
    @Autowired
    AdminJwtHelper jwtHelper;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    SolutionSetsRepository solutionSetsRepository;
    @Autowired
    DownloadLogRepository downloadLogRepository;

    @Autowired
    AdminService adminService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    VendorsService vendorsService;

    @Autowired
    UserService userService;
    @GetMapping("/admin/hello-world")
    public ResponseEntity<String> helloWorld(){
        return ResponseEntity.ok("Hello World in Admin Controller");
    }

    @PostMapping("/register/admin")
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

        if (vendorsService.getVendorsByEmail(email)==null&&userService.getUserByEmail(email)==null){
            adminService.registerAdmin(admin);
            return ResponseEntity.ok("Admin Registered SuccessFully");
        }

        return ResponseEntity.ok("Email Already Registered Please Try With Other Email");
    }
    @PostMapping("/login/admin")
    public ResponseEntity<?> adminLogin(@RequestParam String email,@RequestParam String password
    )
    {

     Admin admin=adminService.getAdminByEmailId(email);
        log.info("ADMIN LOADED {}", admin);
        log.info("ADMIN Authority {}", admin.getAuthorities());

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            ApiResponse1<JwtResponse> response = ResponseUtils.createResponse1(null, "Email & Password Does Not Match", false);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        String token = this.jwtHelper.generateToken(admin);
        JwtResponse jwtResponse = JwtResponse.builder().jwtToken(token).username(admin.getEmail()).build();
        HashMap<String, Object> response = new HashMap<>();
        response.put("jwtToken", jwtResponse);
        System.out.println("Admin Logged in");
        System.out.println(admin.getRole().toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/admin/approve-vendor")
    public ResponseEntity<ApiResponse1<String>> approveVendor(@RequestParam long vendorId){
        Vendors vendor = vendorRepository.findById(vendorId).orElseThrow
                (()-> new RuntimeException("Vendor not Found"));
        System.out.println("**********************************");
        System.out.println(vendor);
        System.out.println("**********************************");
        vendor.setStatus(VendorStatus.APPROVED);
        vendorRepository.save(vendor);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Vendor approved SuccessFully",true));
    }
    @PutMapping("/admin/reject-vendor")
    public ResponseEntity<String> rejectVendor(@RequestParam long vendorId){
        Vendors vendor = vendorRepository.findById(vendorId).orElseThrow(()-> new RuntimeException("Vendor Not Found"));
        vendor.setStatus(VendorStatus.REJECTED);
        vendorRepository.save(vendor);
        return ResponseEntity.ok("Vendor Rejected successfully");
    }
    @PutMapping("/admin/approve-solutionset")
    public ResponseEntity<String> approveSolution(@RequestParam Long solutionId) {
        log.info("inside the Admin controller");
        SolutionSets solutionSet = solutionSetsRepository.findById(solutionId)
                .orElseThrow(() -> new RuntimeException("Solution Set not found"));
        solutionSet.setStatus(SolutionSetsStatus.APPROVED);
        solutionSetsRepository.save(solutionSet);
        return ResponseEntity.ok("Solution set approved successfully");
    }

    @PutMapping("/admin/reject-solutionset")
    public ResponseEntity<String> rejectSolution(@RequestParam Long solutionId) {
        SolutionSets solutionSet = solutionSetsRepository.findById(solutionId)
                .orElseThrow(() -> new RuntimeException("Solution Set not found"));
        solutionSet.setStatus(SolutionSetsStatus.REJECTED); // Reject PDF
        solutionSetsRepository.save(solutionSet);
        return ResponseEntity.ok("Solution set rejected successfully");
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
}
