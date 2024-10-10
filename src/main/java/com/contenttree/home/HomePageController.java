package com.contenttree.home;

import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.solutionsets.SolutionSetsService;
import com.contenttree.user.User;
import com.contenttree.user.UserDto;
import com.contenttree.user.UserService;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import com.contenttree.vendor.Vendors;
import com.contenttree.vendor.VendorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.callback.ReactiveEntityCallbacks;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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


        List<Vendors> vendors = vendorsService.getAllVendors();
        List<SolutionSets> solutionSets= solutionSetsService.getAllSolutionSets();

        HomePageDto homePageDto = new HomePageDto(
                solutionSets,
                vendors
        );
        return ResponseEntity.ok()
                .body(ResponseUtils.createResponse1(homePageDto,"Success",true));
    }

}
