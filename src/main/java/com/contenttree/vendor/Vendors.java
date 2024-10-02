package com.contenttree.vendor;

import com.contenttree.solutionsets.SolutionSets;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vendors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String password;

    private String email;

//    @ManyToOne()
//    private List<SolutionSets> solutionSets;
}
