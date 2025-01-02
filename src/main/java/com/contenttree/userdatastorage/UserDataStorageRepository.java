package com.contenttree.userdatastorage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataStorageRepository extends JpaRepository<UserDataStorage, Long> {
    @Query("SELECT u FROM UserDataStorage u WHERE u.user_id = :id")
    UserDataStorage findByUserId(@Param("id") long id);
    @Query("SELECT u FROM UserDataStorage u WHERE u.user_id = :id")
    List<UserDataStorage> findByUserIdList(@Param("id") long id);

    @Query("SELECT u FROM UserDataStorage u WHERE u.solutionSetId = :id")
    List<UserDataStorage> findBySolutionSetIdList(@Param("id") long id);

    @Query("SELECT u FROM UserDataStorage u WHERE u.categoryId = :id")
    List<UserDataStorage> findByCategoryIdList(@Param("id") long id);
    @Query("SELECT u FROM UserDataStorage u WHERE u.user_id = :userId AND u.save = 1 AND u.solutionSetId = :solutionSetId")
    Optional<UserDataStorage> findByUserIdAndSaveAndSolutionSetId(@Param("userId") Long userId, @Param("solutionSetId") Long solutionSetId);

    @Query("SELECT u FROM UserDataStorage u WHERE u.user_id = :userId AND u.solutionSetId = :solutionSetId")
    Optional<UserDataStorage> findByUserIdAndSolutionSetId(@Param("userId") Long userId, @Param("solutionSetId") Long solutionSetId);

}
