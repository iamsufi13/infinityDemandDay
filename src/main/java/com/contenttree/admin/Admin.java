package com.contenttree.admin;

import com.contenttree.Blogs.Blogs;
import com.contenttree.CampaignManager.Campaign;
import com.contenttree.CampaignManager.Event;
import com.contenttree.CampaignManager.Webinar;
import com.contenttree.NewsLetter.NewsLetter;
import com.contenttree.solutionsets.SolutionSets;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString(exclude = "solutionSets")
public class Admin implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private long phone;

    private String location;

    @JsonIgnore
    @OneToMany(mappedBy = "uploadedByAdmin", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<SolutionSets> solutionSets;
    @JsonIgnore
    private String password;
    @Column(unique = true)
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Webinar> webinars;
    @JsonIgnore
    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    private List<Campaign> campaignList;
    @JsonIgnore
    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    private List<Event> events;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonIgnore
    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    private List<Blogs> blogs;

    @JsonIgnore
    @OneToMany(mappedBy = "admin",cascade = CascadeType.ALL)
    private List<NewsLetter> newsLetters;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "admin_roles",joinColumns = @JoinColumn(name = "admin_id"))
    @Column(name = "role", length = 50)
    @JsonIgnore
    private List<Role> role;
    @JsonIgnore
    private LocalDateTime dt1;

    @PrePersist
    public void prePersist() {
        this.dt1 = LocalDateTime.now();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.stream().map(r-> new SimpleGrantedAuthority(r.name())).toList();
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
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
