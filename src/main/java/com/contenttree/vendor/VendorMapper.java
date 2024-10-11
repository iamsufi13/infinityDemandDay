package com.contenttree.vendor;

import com.contenttree.user.UserDto;

public class VendorMapper {
    public static VendorDto tovendorDto(Vendors vendors){
        if (vendors==null){
            return null;
        }
        return new VendorDto(vendors.getEmail(), vendors.getName());
    }
}
