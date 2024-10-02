package com.contenttree.config;

import com.contenttree.vendor.VendorRepository;
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
    @Autowired
    public CustomUserDetailsService(@Lazy VendorRepository vendorRepository){
        this.vendorRepository=vendorRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Vendors vendors = vendorRepository.findByEmail(username).orElseThrow(()
        -> new UsernameNotFoundException("Username not found with username " + username));
        return vendors;
    }
}
