package com.contenttree.StaticCount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaticCountRepository extends JpaRepository<StaticCount,Long> {
    Optional<StaticCount> findTopByOrderByDt1Desc();
}
