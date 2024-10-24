package com.contenttree.downloadlog;

import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DownloadLogDto {
    private long id;
    private long userId;
    private String userName;
    private String pdfName;
}
