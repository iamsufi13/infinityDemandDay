package com.contenttree.NewsLetter;


import com.contenttree.user.EmailService;
import com.contenttree.user.User;
import com.contenttree.user.UserRepository;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import io.swagger.annotations.Api;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/newsletter")
public class NewsLetterController {
    @Autowired
    NewsLetterService newsLetterService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;

    @PostMapping("/add-newsletter")
    public ResponseEntity<ApiResponse1<NewsLetter>> addNewsLetter(
            @RequestParam String name,
            @RequestParam String desc,
            @RequestParam(required = false) MultipartFile file) {

        String uploadDir = "/var/www/infiniteb2b/springboot/newsletters";
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = null;
        try {
            if (file != null && !file.isEmpty()) {
                fileName = file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, file.getBytes());
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse1(null, "File upload failed: " + e.getMessage(), false));
        }

        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setTitle(name);
        newsLetter.setContent(desc);
        newsLetter.setFilePath(fileName != null ? fileName : "");

        newsLetterService.newsLetterRepository.save(newsLetter);

        try {
            String filePath = fileName != null ? uploadDir + fileName : null;

            List<User> subscribedUsers = userRepository.findAll().stream()
                    .filter(user -> user.isEnabled() && user.getIsSubscriber() == 1)
                    .toList();


            for (User user : subscribedUsers) {
                emailService.sendHtmlEmailWithAttachment(user.getEmail(), name, desc, filePath);
            }
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse1(newsLetter, "Newsletter saved but email sending failed: " + e.getMessage(), false));
        }

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(newsLetter, "SUCCESS", true));
    }




    @GetMapping
    public ResponseEntity<ApiResponse1<List<NewsLetter>>> getAllNewsLetter(){
        List<NewsLetter> list = newsLetterService.newsLetterRepository.findAll();

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list,"SUCCESS",true));
    }
}
