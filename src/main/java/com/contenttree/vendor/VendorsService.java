package com.contenttree.vendor;

import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorsService {
    @Autowired
    VendorRepository vendorsRepository;

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
    public Vendors updateVendorDetails(Long vendorId, String name, String phone, String companyName,
                                       String location, String password) {
        Vendors vendor = vendorsRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        if (name != null) {
            vendor.setName(name);
        }
        if (phone != null) {
            vendor.setPhone(phone);
        }
        if (companyName != null) {
            vendor.setCompanyName(companyName);
        }
        if (location != null) {
            vendor.setLocation(location);
        }
        if (password != null) {
            vendor.setPassword(password);
        }

        return vendorsRepository.save(vendor);
    }


    public Vendors getVendorsByEmail(String email) {
        return vendorsRepository.findByEmail(email).orElse(null);
    }
}
