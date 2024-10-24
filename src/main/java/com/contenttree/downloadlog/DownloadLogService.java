package com.contenttree.downloadlog;

import com.contenttree.user.UserService;
import java.util.List;
import com.contenttree.vendor.VendorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DownloadLogService {
    @Autowired
    DownloadLogRepository downloadLogRepository;
    @Autowired
    VendorsService vendorsService;
    @Autowired
    UserService userService;

    public void logPdfDownload(long name,long vendorId){
        DownloadLog log = new DownloadLog();
        log.setPdfId(name);
        log.setVendor(vendorsService.getVendorsById(vendorId));
        log.setDt1(LocalDateTime.now());

        downloadLogRepository.save(log);

    }
    public List<DownloadLog> getAllDownloadLogByUser(long user_id){
        return downloadLogRepository.findByUserId(user_id);
    }
    public void lodPdfDownloadUser(long name, long userId){
        DownloadLog downloadLog= new DownloadLog();
        downloadLog.setPdfId(name);
        downloadLog.setUser(userService.getUserById(userId));
        downloadLog.setDt1(LocalDateTime.now());

        downloadLogRepository.save(downloadLog);
        }

    }

