package com.contenttree.home;

import com.contenttree.solutionsets.SolutionSetDto;
import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.user.User;
import com.contenttree.user.UserDto;
import com.contenttree.vendor.VendorDto;
import com.contenttree.vendor.Vendors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HomePageDto {
    private List<SolutionSets> solutionSets;
    private List<VendorDto> vendors;
    private HashMap counts;
}
