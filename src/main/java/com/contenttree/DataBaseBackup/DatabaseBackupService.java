package com.contenttree.DataBaseBackup;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service

public class DatabaseBackupService {
//        @Value("${spring.datasource.username}")
//        private String dbUsername;
//
//        @Value("${spring.datasource.password}")
//        private String dbPassword;
//
//        @Value("${spring.datasource.url}")
//        private String dbUrl;
//
//        @Value("${backup.daily-dir}")
//        private String dailyBackupDir;
//
//        @Value("${backup.weekly-dir}")
//        private String weeklyBackupDir;
//
//        @Value("${backup.monthly-dir}")
//        private String monthlyBackupDir;
//
//        private String getDatabaseName() {
//            return dbUrl.substring(dbUrl.lastIndexOf("/") + 1, dbUrl.indexOf("?"));
//        }
//
//        private void runBackup(String directoryPath, String frequency) throws IOException {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//            String timestamp = dateFormat.format(new Date());
//            String backupFile = directoryPath + "/backup_" + frequency + "_" + timestamp + ".sql";
//
//            String command = String.format("mysqldump -u%s -p%s --databases %s -r %s",
//                    dbUsername, dbPassword, getDatabaseName(), backupFile);
//
//            Process process = Runtime.getRuntime().exec(command);
//            try {
//                if (process.waitFor() == 0) {
//                    System.out.println(frequency + " backup successful: " + backupFile);
//                } else {
//                    System.err.println(frequency + " backup failed.");
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private void cleanOldBackups(String directoryPath, int maxFiles) {
//            File directory = new File(directoryPath);
//            File[] files = directory.listFiles();
//            if (files != null && files.length > maxFiles) {
//                for (int i = 0; i < files.length - maxFiles; i++) {
//                    files[i].delete();
//                }
//            }
//        }
//
//        @Scheduled(cron = "0 0 1 * * *")
//        public void dailyBackup() throws IOException {
//            File dailyDir = new File(dailyBackupDir);
//            if (!dailyDir.exists()) dailyDir.mkdirs();
//            runBackup(dailyBackupDir, "daily");
//            cleanOldBackups(dailyBackupDir, 7);
//        }
//
//        @Scheduled(cron = "0 0 2 * * 1")
//        public void weeklyBackup() throws IOException {
//            File weeklyDir = new File(weeklyBackupDir);
//            if (!weeklyDir.exists()) weeklyDir.mkdirs();
//            runBackup(weeklyBackupDir, "weekly");
//            cleanOldBackups(weeklyBackupDir, 4);
//        }
//
//        @Scheduled(cron = "0 0 3 1 * *")
//        public void monthlyBackup() throws IOException {
//            File monthlyDir = new File(monthlyBackupDir);
//            if (!monthlyDir.exists()) monthlyDir.mkdirs();
//            runBackup(monthlyBackupDir, "monthly");
//            cleanOldBackups(monthlyBackupDir, 12);
//        }
    }


