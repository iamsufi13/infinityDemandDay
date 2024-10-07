package com.contenttree.config;

import com.contenttree.admin.Admin;
import com.contenttree.admin.AdminRepository;
import com.contenttree.vendor.VendorRepository;
import com.contenttree.vendor.VendorStatus;
import com.contenttree.vendor.Vendors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final VendorRepository vendorRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public CustomUserDetailsService(@Lazy VendorRepository vendorRepository, AdminRepository adminRepository) {
        this.vendorRepository = vendorRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(username).orElse(null);

        if (admin != null) {
            return admin;
        }

        Vendors vendor = vendorRepository.findByEmail(username).orElse(null);

        if (vendor != null) {
            if (vendor.getStatus() != VendorStatus.APPROVED) {
                throw new UsernameNotFoundException("Vendor account is not approved");
            }
            return vendor;
        }

        throw new UsernameNotFoundException("User not found with email " + username);
    }
}
