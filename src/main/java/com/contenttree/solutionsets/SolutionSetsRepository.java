package com.contenttree.solutionsets;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolutionSetsRepository extends JpaRepository<SolutionSets,Long> {
    Optional<SolutionSets> findByName(String name);
}
