package com.contenttree.NewsLetter;

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

    private String name;

    private String descrption;

    private LocalDateTime dt1;

    @PrePersist
    public void prepersit(){this.dt1=LocalDateTime.now();}

}
