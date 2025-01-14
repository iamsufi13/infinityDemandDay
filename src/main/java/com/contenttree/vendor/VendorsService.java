package com.contenttree.vendor;

import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorsService {
    @Autowired
    VendorRepository vendorsRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public List<Vendors> getAllVendors(){
        return vendorsRepository.findAll();
    }

    public List<VendorDto> getApprovedVendors() {
        List<Vendors> vendors = vendorsRepository.findAll();
        List<Vendors> approvedVendors = vendors.stream()
                .filter(vendor -> "approved".equalsIgnoreCase(String.valueOf(vendor.getStatus())))
                .toList();

        List<VendorDto> vendorDtos = approvedVendors.stream()
                .map(VendorMapper::tovendorDto)
                .collect(Collectors.toList());

        return vendorDtos;
    }

    public void registerVendors(Vendors vendors){
        vendors.setStatus(VendorStatus.PENDING);
        vendorsRepository.save(vendors);
    }
    public Vendors getVendorsById(long id){
        return vendorsRepository.findById(id).orElse(null);
    }
    public Vendors updateVendorDetails(Vendors vendor) {
        Vendors existingVendor = vendorsRepository.findById(vendor.getId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        if (vendor.getName() != null && !vendor.getName().isEmpty()) {
            existingVendor.setName(vendor.getName());
        }
        if (vendor.getLastName() != null && !vendor.getLastName().isEmpty()) {
            existingVendor.setLastName(vendor.getLastName());
        }
        if (vendor.getPhoneNo() != null && !vendor.getPhoneNo().isEmpty()) {
            existingVendor.setPhoneNo(vendor.getPhoneNo());
        }
        if (vendor.getCompanyName() != null && !vendor.getCompanyName().isEmpty()) {
            existingVendor.setCompanyName(vendor.getCompanyName());
        }
        if (vendor.getDesignation() != null && !vendor.getDesignation().isEmpty()) {
            existingVendor.setDesignation(vendor.getDesignation());
        }
        if (vendor.getCountry() != null && !vendor.getCountry().isEmpty()) {
            existingVendor.setCountry(vendor.getCountry());
        }
        if (vendor.getState() != null && !vendor.getState().isEmpty()) {
            existingVendor.setState(vendor.getState());
        }
        if (vendor.getZipCode() != null && !vendor.getZipCode().isEmpty()) {
            existingVendor.setZipCode(vendor.getZipCode());
        }
        if (vendor.getPassword() != null && !vendor.getPassword().isEmpty()) {
            existingVendor.setPassword(passwordEncoder.encode(vendor.getPassword()));
        }

        return vendorsRepository.save(existingVendor);
    }



    public Vendors getVendorsByEmail(String email) {
        return vendorsRepository.findByEmail(email).orElse(null);
    }
}
