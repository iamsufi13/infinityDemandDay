package com.contenttree.userdatastorage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDataStorageService {
    @Autowired
    UserDataStorageRepository userDataStorageRepository;

    public void addUserDataStorage(UserDataStorage userDataStorage) {
        Optional<UserDataStorage> userDataStorageOpt = userDataStorageRepository.findByUserIdAndSolutionSetId(userDataStorage.getUser_id(), userDataStorage.getSolutionSetId());

        if (userDataStorageOpt.isEmpty()) {
            userDataStorageRepository.save(userDataStorage);
        } else {
            UserDataStorage existingUserDataStorage = userDataStorageOpt.get();

            if (userDataStorage.getSolutionSetId() != existingUserDataStorage.getSolutionSetId()) {
                userDataStorageRepository.save(userDataStorage);
            }

            existingUserDataStorage.setSave(userDataStorage.getSave());
            existingUserDataStorage.setDownload(userDataStorage.getDownload());
            existingUserDataStorage.setView(userDataStorage.getView());  // If you're updating 'view', also set it

            userDataStorageRepository.save(existingUserDataStorage);
        }
    }

    public UserDataStorage getUserDataByUser(long id){
        return userDataStorageRepository.findByUserId(id);
    }
}
