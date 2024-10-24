package com.contenttree.downloadlog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DownloadLogRepository extends JpaRepository<DownloadLog,Long> {
    List<DownloadLog> findByUserId(long user_id);
}
