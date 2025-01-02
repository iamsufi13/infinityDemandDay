package com.contenttree.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCountDto {

    private long id;

    private String name;

    private String iconPath;

    private String bannerPath;

    private String descp;

    private long totalDownloads;
    private long totalSubscribers;
    private int totalWhitePapers;
}
