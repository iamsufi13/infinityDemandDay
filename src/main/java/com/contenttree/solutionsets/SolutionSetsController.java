package com.contenttree.solutionsets;

import com.contenttree.downloadlog.DownloadLogService;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import com.contenttree.vendor.Vendors;
import com.contenttree.vendor.VendorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/solutionsets")
public class SolutionSetsController {
    @Autowired
    SolutionSetsService solutionSetsService;
    @Autowired
    DownloadLogService downloadLogService;
    @Autowired
    VendorsService vendorsService;

    @GetMapping
    public ResponseEntity<ApiResponse1<List<SolutionSets>>> getAllSolutionSets(){
        List<SolutionSets> list = solutionSetsService.getAllSolutioinSets();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS",true));
    }
    @GetMapping("/downloadpdf")
    public ResponseEntity<byte[]> downloadSolutionSets(@RequestParam long name,
                                                       @AuthenticationPrincipal Vendors vendor){
    byte[] pdfData = solutionSetsService.downloadPdf(name);
    if (pdfData==null || pdfData.length ==0){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    downloadLogService.logPdfDownload(name, vendor.getId());
        System.out.println("Downloading Done " + name  + " " + vendor.getName());
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfData);
    }

    @PostMapping
    public ResponseEntity<ApiResponse1<SolutionSets>> addSolutionSets(
                                                                      @RequestParam MultipartFile file,
                                                                      @RequestParam String category){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null || !(authentication.getPrincipal() instanceof UserDetails)){
            return ResponseEntity.status(401).body(null);
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Vendors vendors = vendorsService.getVendorsByEmail(userDetails.getUsername());

        if (vendors==null) {
            return ResponseEntity.notFound().build();
        }
        String uploadResponse = solutionSetsService.uploadSolutionSets(file, vendors.getId(),category);
        return ResponseEntity.ok(new ApiResponse1<>(true,"SUCCESS",null));
    }
}

