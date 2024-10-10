package com.contenttree.home;

import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.user.User;
import com.contenttree.user.UserDto;
import com.contenttree.vendor.Vendors;

import java.util.List;

public class HomePageMapper {

    public static HomePageDto homePageDto( List<SolutionSets>solutionSets, List<Vendors> vendors){
        if (solutionSets ==null && vendors==null){
            return null;
        }
        return new HomePageDto(
                solutionSets,
                vendors
        );

    }
}
