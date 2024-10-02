package com.contenttree.solutionsets;

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

    @Lob
    private byte[] filePath;

    @JsonIgnore
    private LocalDateTime dt1;

    public void dt1(LocalDateTime dt1){
        this.dt1=dt1;
    }
}
