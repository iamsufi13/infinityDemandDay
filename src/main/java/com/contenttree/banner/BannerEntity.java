package com.contenttree.banner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String bannerType;

    @Lob
    private byte[] filePath;

    @JsonIgnore
    private LocalDateTime dt1;

    @PrePersist
    public void prepersist(){
        this.dt1=LocalDateTime.now();
    }
    public void dt1(LocalDateTime dt1){
        this.dt1=dt1;
    }
}
