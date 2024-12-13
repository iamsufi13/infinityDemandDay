package com.contenttree.StaticCount;

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
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String countryName;

    private int percent;

    @JsonIgnore
    private LocalDateTime dt1;

    @PrePersist
    public void prepersist(){
        this.dt1=LocalDateTime.now();
    }

}
