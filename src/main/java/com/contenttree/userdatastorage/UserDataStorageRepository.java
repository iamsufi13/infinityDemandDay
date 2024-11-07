package com.contenttree.userdatastorage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataStorageRepository extends JpaRepository<UserDataStorage, Long> {
}
