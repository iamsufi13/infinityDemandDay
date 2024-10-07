package com.contenttree.vendor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorsService {
    @Autowired
    VendorRepository vendorsRepository;

    public void registerVendors(Vendors vendors){
        vendors.setStatus(VendorStatus.PENDING);
        vendorsRepository.save(vendors);
    }
    public Vendors getVendorsById(long id){
        return vendorsRepository.findById(id).orElse(null);
    }


    public Vendors getVendorsByEmail(String email) {
        return vendorsRepository.findByEmail(email).orElse(null);
    }
}
