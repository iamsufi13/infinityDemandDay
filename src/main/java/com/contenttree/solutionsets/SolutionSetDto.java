package com.contenttree.solutionsets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SolutionSetDto {
    private String fileType;
    private String name;
    private String category;
    private SolutionSetsStatus status;


}
