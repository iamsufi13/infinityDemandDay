package com.contenttree.solutionsets;

import com.contenttree.admin.Admin;
import com.contenttree.category.Category;
import com.contenttree.vendor.Vendors;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "category")
public class SolutionSets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String fileType;

    private String title;

    private String description;

    private String imagePath;


//    @Lob
//    private byte[] filePath;
private String filePath;


    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendors uploadedBy;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin uploadedByAdmin;

    @Enumerated(EnumType.STRING)
    private SolutionSetsStatus status;

    @JsonIgnore
    private LocalDateTime dt1;

    @PrePersist
    public void prePersist() {
        this.dt1 = LocalDateTime.now();
    }
    }

