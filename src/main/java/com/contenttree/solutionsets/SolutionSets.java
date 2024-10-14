package com.contenttree.solutionsets;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolutionSets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String fileType;

//    @Lob
//    private byte[] filePath;
private String filePath;

    private String category;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendors uploadedBy;

    @Enumerated(EnumType.STRING)
    private SolutionSetsStatus status;

    @JsonIgnore
    private LocalDateTime dt1;

    @PrePersist
    public void prePersist() {
        this.dt1 = LocalDateTime.now();
    }
    }

