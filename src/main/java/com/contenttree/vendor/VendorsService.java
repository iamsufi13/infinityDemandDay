package com.contenttree.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorsService {
    @Autowired
    VendorRepository vendorsRepository;

    public void registerVendors(Vendors vendors){
        vendorsRepository.save(vendors);
    }


    public Vendors getVendorsByEmail(String email) {
        return vendorsRepository.findByEmail(email).orElse(null);
    }
}
