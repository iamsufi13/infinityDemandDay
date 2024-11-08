package com.contenttree.CampaignManager;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebinarRepository extends JpaRepository<Webinar,Long> {
    List<Webinar> findByAdminId(long id);
}
