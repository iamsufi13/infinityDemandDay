package com.contenttree.userdatastorage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDataStorageService {
    @Autowired
    UserDataStorageRepository userDataStorageRepository;

    public void addUserDataStorage(UserDataStorage userDataStorage) {
        userDataStorageRepository.save(userDataStorage);
    }
}
