package com.contenttree.solutionsets;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolutionSetsRepository extends JpaRepository<SolutionSets,Long> {
    Optional<SolutionSets> findByName(String name);

    @Query("select s from SolutionSets s where s.uploadedBy.id = :vendorId")
    List<SolutionSets> findByUploadedBy(long vendorId);

    @Query("select s from SolutionSets s where s.category = :category")
    List<SolutionSets> findByCategoryIgnoreCase(String category);
}
