package com.contenttree.NewsLetter;

import com.contenttree.admin.Admin;
import com.contenttree.user.User;
import com.contenttree.vendor.Vendors;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String content;

    private String filePath;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @JsonBackReference
//    @ManyToOne
//    @JoinColumn(name = "vendor_id")
//    private Vendors uploadedBy;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

//    @JsonIgnore
    private LocalDateTime dt1;

    @PrePersist
    public void prepersit(){this.dt1=LocalDateTime.now();}

}
