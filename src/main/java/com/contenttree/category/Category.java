package com.contenttree.category;

import com.contenttree.NewsLetter.NewsLetter;
import com.contenttree.solutionsets.SolutionSets;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "solutionSets")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String iconPath;

    private String bannerPath;

    private String descp;

    private long is_Subscribe;

    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<SolutionSets> solutionSets;

    @JsonIgnore
    private LocalDateTime dt1;

    public Category(String line, Object o, Object o1, Object o2, Object o3) {
    }

    @jakarta.persistence.PrePersist
    public void prePersist() {
        this.dt1 = LocalDateTime.now();
    }
}
