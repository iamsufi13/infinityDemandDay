package com.contenttree.solutionsets;

public class SolutionSetMapper {

    public static SolutionSetDto toSolutionSetDto(SolutionSets solutionSets) {
        if (solutionSets == null) {
            return null;
        }

        String categoryName = solutionSets.getCategory() != null ? solutionSets.getCategory().getName() : null;

        return new SolutionSetDto(
                solutionSets.getFileType(),
                solutionSets.getName(),
                categoryName,
                solutionSets.getStatus()
        );
    }
}
