package com.contenttree.Blogs;

import com.contenttree.admin.Admin;
import com.contenttree.category.Category;
import com.contenttree.vendor.Vendors;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String blogName;

    private String blogImage;

    private String blogPath;

//    @JsonBackReference
//    @ManyToOne
//    @JoinColumn(name = "vendor_id")
//    private Vendors uploadedBy;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "blogsCategory_id")
    private BlogsCategory blogsCategory;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
//    @JsonIgnore
    private LocalDateTime dt1;
    @jakarta.persistence.PrePersist
    public void prePersist() {
        this.dt1 = LocalDateTime.now();
    }
}
