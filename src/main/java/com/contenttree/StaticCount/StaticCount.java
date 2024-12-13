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
public class StaticCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long whitePaperCount;

    private long categoryCount;

    private long usersCount;

    private long vendorsCount;

    @JsonIgnore
    private LocalDateTime dt1;

    @PrePersist
    public void prePersist(){
        this.dt1=LocalDateTime.now();
    }
}