package com.contenttree.PreviewImage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PreviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String path;

    @JsonIgnore
    private LocalDateTime dt1;

    @PrePersist
    public void prepersit(){this.dt1=LocalDateTime.now();}
}
