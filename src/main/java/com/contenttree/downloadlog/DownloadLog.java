package com.contenttree.downloadlog;

import com.contenttree.user.User;
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
public class DownloadLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long pdfId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendors vendor;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
 
    @JsonIgnore
    private LocalDateTime dt1;


}
