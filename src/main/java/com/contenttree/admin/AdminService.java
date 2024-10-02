package com.contenttree.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    AdminRepository adminRepository;

    public void registerAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    public Admin getAdminByEmailId(String email) {
        return adminRepository.findByEmail(email).orElse(null);
    }
}
