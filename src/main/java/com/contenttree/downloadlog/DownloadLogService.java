package com.contenttree.downloadlog;

import com.contenttree.vendor.Vendors;
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

    public void logPdfDownload(String name,long vendorId){
        DownloadLog log = new DownloadLog();
        log.setPdfName(name);
        log.setVendor(vendorsService.getVendorsById(vendorId));
        log.setDt1(LocalDateTime.now());

        downloadLogRepository.save(log);

    }
}
