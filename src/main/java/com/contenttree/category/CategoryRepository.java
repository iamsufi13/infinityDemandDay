package com.contenttree.category;

import com.contenttree.solutionsets.SolutionSets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


    Category findByNameIgnoreCase(String name);

    boolean existsByName(String name);
}
