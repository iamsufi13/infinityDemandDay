package com.contenttree.home;

import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.solutionsets.SolutionSetsService;
import com.contenttree.user.User;
import com.contenttree.user.UserService;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import com.contenttree.vendor.VendorDto;
import com.contenttree.vendor.VendorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomePageController {

    @Autowired
    VendorsService vendorsService;
    @Autowired
    SolutionSetsService solutionSetsService;
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse1<HomePageDto>> getHomePageInfo(){


        List<VendorDto> vendors = vendorsService.getApprovedVendors();
        List<SolutionSets> solutionSets= solutionSetsService.getLatestUploadedSolutionSets();
        HashMap count = new HashMap<>();
        List<SolutionSets> a = solutionSetsService.getAllSolutionSets();
        long solutionSetsCount = a.size();
        count.put("b2bAssets",solutionSetsCount);
        long vendorsCount = vendors.size();
        count.put("Vendors",vendorsCount);

        List<User> usersCount = userService.getAllUsers();
        count.put("indsutries",usersCount.size());
        count.put("vendorsOffering",solutionSetsCount);



        HomePageDto homePageDto = new HomePageDto(
                solutionSets,
                vendors,
                count
        );
        return ResponseEntity.ok()
                .body(ResponseUtils.createResponse1(homePageDto,"Success",true));
    }

}
