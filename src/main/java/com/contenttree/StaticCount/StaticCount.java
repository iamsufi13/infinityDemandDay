package com.contenttree.StaticCount;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
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

    private long newsLetterCount;

    private long blogsCount;

    private long subscribers;

    private long campaignsCount;

    @JsonIgnore
    private LocalDateTime dt1;

    @PrePersist
    public void prePersist(){
        this.dt1=LocalDateTime.now();
    }
}
