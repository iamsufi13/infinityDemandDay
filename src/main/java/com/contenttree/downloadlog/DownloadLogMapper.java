package com.contenttree.downloadlog;

import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.solutionsets.SolutionSetsService;
import com.contenttree.user.User;
import com.contenttree.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DownloadLogMapper {

    private final SolutionSetsService solutionSetsService;
    private final UserService userService;

    @Autowired
    public DownloadLogMapper(SolutionSetsService solutionSetsService, UserService userService) {
        this.solutionSetsService = solutionSetsService;
        this.userService = userService;
    }

    public DownloadLogDto toDownloadLogDto(DownloadLog downloadLog) {
        if (downloadLog == null) {
            return null;
        }
        String username = (downloadLog.getUser().getName());
        long userId = downloadLog.getUser().getId();

        SolutionSets solutionSets = solutionSetsService.getSolutionSetById(downloadLog.getPdfId());

        return new DownloadLogDto(
                downloadLog.getId(),
                userId,
                username,
                solutionSets.getName()
        );
    }
}
