package com.contenttree.solutionsets;

import java.util.Locale;

public class SolutionSetMapper {
    public static SolutionSetDto toSolutionSetDto(SolutionSets solutionSets){
        if (solutionSets==null){
            return null;
        }
        return  new SolutionSetDto(
                solutionSets.getFileType(),
                solutionSets.getName(),
                solutionSets.getCategory(),
                solutionSets.getStatus()
        );
    }
}
