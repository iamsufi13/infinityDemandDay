package com.contenttree.vendor;

import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendors,Long> {
    Optional<Vendors> findByEmail(String email);
}
