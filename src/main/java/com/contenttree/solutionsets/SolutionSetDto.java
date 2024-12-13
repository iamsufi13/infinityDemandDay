package com.contenttree.solutionsets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SolutionSetDto {
    private String fileType;
    private String name;
    private String category;
    private SolutionSetsStatus status;
    private long views;
    private long downloads;
    private String date;


}
