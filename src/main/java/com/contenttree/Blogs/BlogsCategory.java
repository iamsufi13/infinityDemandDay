package com.contenttree.Blogs;

import com.contenttree.solutionsets.SolutionSets;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogsCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String blogCategoryName;

    private String blogCategoryDescp;

    @JsonManagedReference
    @OneToMany(mappedBy = "blogsCategory", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Blogs> blogs;

//    @JsonIgnore
    private LocalDateTime dt1;
    @jakarta.persistence.PrePersist
    public void prePersist() {
        this.dt1 = LocalDateTime.now();
    }
}
