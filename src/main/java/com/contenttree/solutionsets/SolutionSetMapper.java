package com.contenttree.solutionsets;

import com.contenttree.userdatastorage.UserDataStorage;
import com.contenttree.userdatastorage.UserDataStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class SolutionSetMapper {
    @Autowired
    private UserDataStorageRepository userDataStorageRepository;

    public SolutionSetDto toSolutionSetDto(SolutionSets solutionSets) {
        if (solutionSets == null) {
            return null;
        }

        String categoryName = solutionSets.getCategory() != null ? solutionSets.getCategory().getName() : null;

        List<UserDataStorage> userDataStorage = userDataStorageRepository.findAll();
        userDataStorage.stream().filter(s -> s.getSolutionSetId() == solutionSets.getId());

        String formattedDate = null;
        if (solutionSets.getDt1() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            formattedDate = solutionSets.getDt1().format(formatter);
        }

        long views = userDataStorage.stream()
                .filter(userDataStorage1 -> userDataStorage1.getView() > 0&& userDataStorage1.getSolutionSetId()==(solutionSets.getId()))
                .count();

        long downloads = userDataStorage.stream()
                .filter(userDataStorage1 -> userDataStorage1.getDownload() > 0&& userDataStorage1.getSolutionSetId()==(solutionSets.getId()))
                .count();

        return new SolutionSetDto(
                solutionSets.getFileType(),
                solutionSets.getName(),
                categoryName,
                solutionSets.getStatus(),
                views,
                downloads,
                formattedDate
        );
    }

}
