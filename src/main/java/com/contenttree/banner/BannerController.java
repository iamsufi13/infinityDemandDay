package com.contenttree.banner;

import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/banner")
public class BannerController {

    @Autowired
    BannerService bannerService;

    @GetMapping
    public ResponseEntity<ApiResponse1<List<BannerEntity>>> getAllBanner(){
        List<BannerEntity> list = bannerService.getAllBanners();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS" , true));
    }
    @GetMapping("/getbyid")
    public ResponseEntity<ApiResponse1<BannerEntity>> getBannerById(@RequestParam long id){
        BannerEntity banner = bannerService.getById(id);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(banner,"SUCCESS",true));
    }

    @PostMapping
    public ResponseEntity<ApiResponse1<List<BannerEntity>>> saveBanner(
            @RequestParam String name,
            @RequestParam String bannerType,
            @RequestParam MultipartFile file) {

        String uploadResponse = bannerService.uploadFile(file);

        if (uploadResponse.startsWith("File uploaded successfully")) {
            List<BannerEntity> banners = bannerService.getAllBanners();

            return ResponseEntity.ok(new ApiResponse1<>(true,"SUCCESS",null));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse1<>(false, uploadResponse, null));
        }
    }

    @DeleteMapping("/deletebannerbyid")
    public ResponseEntity<ApiResponse1<String>> deleteBanner(@RequestParam long id){
        bannerService.deleteBannerById(id);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"DELETED",true));
    }
}
