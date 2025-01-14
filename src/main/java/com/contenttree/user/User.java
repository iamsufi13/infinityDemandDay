package com.contenttree.user;

import com.contenttree.NewsLetter.NewsLetter;
import com.contenttree.admin.Status;
import com.contenttree.confirmationtoken.ConfirmationToken;
import com.contenttree.downloadlog.DownloadLog;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;
    private String lastName;
    private String country;
    private long phone;
    private String jobTitle;
    private String company;
    private int isSubscriber;


    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @JsonIgnore
    private String ipAddress;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_favorites", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "favorite_item")
    @JsonIgnore
    private List<String> favorites;


    @OneToMany
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    @JsonIgnore
    private List<NewsLetter> newsLetterList;

    @JsonIgnore
    private List<Long> savedPdf;
    @JsonIgnore
    private List<Long> viewdPdf;
    @JsonIgnore
    private String password;

    @JsonIgnore
    private boolean isEnabled;

    @JsonIgnore
    private int isNewsLetterSubscriber;

    @JsonIgnore
    private LocalDateTime dt1;

    @PrePersist
    public void prePersist() {
        this.dt1 = LocalDateTime.now();
    }

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ConfirmationToken> confirmationTokens;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<DownloadLog> downloadLogs;
  @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
