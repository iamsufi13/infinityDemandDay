package com.contenttree.home;

import com.contenttree.solutionsets.SolutionSetDto;
import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.user.User;
import com.contenttree.user.UserDto;
import com.contenttree.vendor.VendorDto;
import com.contenttree.vendor.Vendors;

import java.util.HashMap;
import java.util.List;

public class HomePageMapper {

    public static HomePageDto homePageDto(List<SolutionSets>solutionSets, List<VendorDto> vendors, HashMap counts){
        if (solutionSets ==null && vendors==null && counts==null){
            return null;
        }
        return new HomePageDto(
                solutionSets,
                vendors,
                counts
        );

    }
}
