package com.contenttree.vendor;

import com.contenttree.Blogs.Blogs;
import com.contenttree.NewsLetter.NewsLetter;
import com.contenttree.downloadlog.DownloadLog;
import com.contenttree.solutionsets.SolutionSets;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "solutionSets")
public class Vendors implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;

    private String phoneNo;

    private String companyName;

    private String designation;

    private String country;
    private String state;

    private String zipCode;
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private VendorStatus status;

    @Column(unique = true)
    private String email;
    @JsonIgnore
    private LocalDateTime dt1;

    @PrePersist
    public void prePersist() {
        this.dt1 = LocalDateTime.now();
    }

    @JsonBackReference
    @OneToMany(mappedBy = "uploadedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SolutionSets> solutionSets;

    @JsonBackReference
    @OneToMany(mappedBy = "vendor")
    private List<DownloadLog> downloadLogs;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_VENDOR"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
