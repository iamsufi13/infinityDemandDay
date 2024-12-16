package com.contenttree.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private long id;
    private String name;
    private String iconPath;
    private String bannerPath;
    private int isSubscribe;

    private long solutionSetCount;

    public CategoryDto(long id, String name, int size) {
    }

    public CategoryDto(long id, String name, String iconPath, String bannerPath, int size) {
    }
}
