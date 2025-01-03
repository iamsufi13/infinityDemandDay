package com.contenttree.NewsLetter;


import com.contenttree.admin.Admin;
import com.contenttree.category.Category;
import com.contenttree.category.CategoryRepository;
import com.contenttree.user.EmailService;
import com.contenttree.user.User;
import com.contenttree.user.UserRepository;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Autowired
    CategoryRepository categoryRepository;
    @PostMapping("/add-newsletter")
    public ResponseEntity<ApiResponse1<NewsLetter>> addNewsLetter(
            @RequestParam String name,
            @RequestParam String desc,
            @RequestParam long categoryId,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) MultipartFile image,
            @AuthenticationPrincipal Admin admin) {

        String uploadDir = "/var/www/infiniteb2b/springboot/newsletters";
        File directory = new File(uploadDir);
        Category category = categoryRepository.findById(categoryId).orElse(null);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = null;
        String imageName = null;

        try {
            if (file != null && !file.isEmpty()) {
                fileName = file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());
            }

            if (image != null && !image.isEmpty()) {
                imageName = image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDir  + imageName);
                Files.createDirectories(imagePath.getParent());
                Files.write(imagePath, image.getBytes());
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse1(null, "File or image upload failed: " + e.getMessage(), false));
        }

        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setTitle(name);
        newsLetter.setContent(desc);
        newsLetter.setCategory(category);
        newsLetter.setFilePath(fileName != null ? "files/" + fileName : "");
        newsLetter.setImagePath(imageName != null ? "images/" + imageName : "");
        newsLetter.setAdmin(admin);

        newsLetterService.newsLetterRepository.save(newsLetter);

        try {
            List<User> subscribedUsers = userRepository.findAll().stream()
                    .filter(user -> user.isEnabled() && user.getIsSubscriber() == 1
                            && user.getFavorites().contains(category != null ? category.getName() : null))
                    .toList();

            String logoUrl = "https://infiniteb2b.com/static/media/Infinite-b2b-1-scaled.f42a6998e6eac74721e6.png";
            String newsletterUrl = "https://infiniteb2b.com/newsletters";
            String title = newsLetter.getTitle();

            String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
                    "<div style=\"text-align: center; margin-bottom: 30px;\">" +
                    "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
                    "</div>" +
                    "<h2 style=\"text-align: center; color: #007bff; font-size: 24px;\">New Newsletter Released! " + title + "</h2>" +
                    "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
                    "We are excited to announce the release of our latest newsletter from <strong>InfiniteB2B</strong>! The new edition, titled <strong>" + title + "</strong>, is now available. Stay updated with the latest trends and insights!" +
                    "</p>" +
                    "<div style=\"text-align: center; margin: 30px 0;\">" +
                    "<a href=\"" + newsletterUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">Read the Newsletter</a>" +
                    "</div>" +
                    "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
                    "If the button above doesn’t work, copy and paste the following URL into your browser:" +
                    "</p>" +
                    "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
                    "<a href=\"" + newsletterUrl + "\" style=\"color: #007bff; word-wrap: break-word;\">" + newsletterUrl + "</a>" +
                    "</p>" +
                    "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
                    "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
                    "<p>&copy; 2024 InfiniteB2B. All rights reserved.</p>" +
                    "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
                    "</footer>" +
                    "</div>";

            for (User user : subscribedUsers) {
                emailService.sendHtmlEmail(user.getEmail(),
                        "New WhitePaper Added in your Favorite Category " + category.getName(),
                        htmlMessage);
            }
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse1(newsLetter, "Newsletter saved but email sending failed: " + e.getMessage(), false));
        }

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(newsLetter, "SUCCESS", true));
    }


//    @PostMapping("/add-newsletter")
//    public ResponseEntity<ApiResponse1<NewsLetter>> addNewsLetter(
//            @RequestParam String name,
//            @RequestParam String desc,
//            @RequestParam long categoryId,
//            @RequestParam(required = false) MultipartFile file,
//            @AuthenticationPrincipal Admin admin) {
//
//        String uploadDir = "/var/www/infiniteb2b/springboot/newsletters/";
////        String uploadDir = "src/main/resources/upload/newsletters/";
//        File directory = new File(uploadDir);
//        Category category = categoryRepository.findById(categoryId).orElse(null);
//
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        String fileName = null;
//
//        try {
//            if (file != null && !file.isEmpty()) {
//                fileName = file.getOriginalFilename();
//                Path filePath = Paths.get(uploadDir + fileName);
//                Files.write(filePath, file.getBytes());
//            }
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(ResponseUtils.createResponse1(null, "File upload failed: " + e.getMessage(), false));
//        }
//
//        NewsLetter newsLetter = new NewsLetter();
//        newsLetter.setTitle(name);
//        newsLetter.setContent(desc);
//        newsLetter.setCategory(category);
//        newsLetter.setFilePath(fileName != null ? fileName : "");
//        newsLetter.setAdmin(admin);
//
//        newsLetterService.newsLetterRepository.save(newsLetter);
//
//        try {
//            String filePath = fileName != null ? uploadDir + fileName : null;
//
//            List<User> subscribedUsers = userRepository.findAll().stream()
//                    .filter(user -> user.isEnabled() && user.getIsSubscriber() == 1&& user.getFavorites().contains(category != null ? category.getName() : null))
//                    .toList();
//             String logoUrl = "https://infiniteb2b.com/static/media/Infinite-b2b-1-scaled.f42a6998e6eac74721e6.png";
//             String newsletterUrl = "https://infiniteb2b.com/newsletters";
//             String title = newsLetter.getTitle();
//
//            String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
//                    "<div style=\"text-align: center; margin-bottom: 30px;\">" +
//                    "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
//                    "</div>" +
//                    "<h2 style=\"text-align: center; color: #007bff; font-size: 24px;\">New Newsletter Released! " + title + "</h2>" +
//                    "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
//                    "We are excited to announce the release of our latest newsletter from <strong>InfiniteB2B</strong>! The new edition, titled <strong>" + title + "</strong>, is now available. Stay updated with the latest trends and insights!" +
//                    "</p>" +
//                    "<div style=\"text-align: center; margin: 30px 0;\">" +
//                    "<a href=\"" + newsletterUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">Read the Newsletter</a>" +
//                    "</div>" +
//                    "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                    "If the button above doesn’t work, copy and paste the following URL into your browser:" +
//                    "</p>" +
//                    "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
//                    "<a href=\"" + newsletterUrl + "\" style=\"color: #007bff; word-wrap: break-word;\">" + newsletterUrl + "</a>" +
//                    "</p>" +
//                    "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
//                    "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
//                    "<p>&copy; 2024 InfiniteB2B. All rights reserved.</p>" +
//                    "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
//                    "</footer>" +
//                    "</div>";
//
//
//            for (User user : subscribedUsers) {
//                emailService.sendHtmlEmail(user.getEmail(),"New WhitePaper Added in your Favorite Category " + category.getName(),
//                        htmlMessage);
//            }
//        } catch (MessagingException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(ResponseUtils.createResponse1(newsLetter, "Newsletter saved but email sending failed: " + e.getMessage(), false));
//        }
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(newsLetter, "SUCCESS", true));
//    }





    @GetMapping
    public ResponseEntity<ApiResponse1<List<NewsLetter>>> getAllNewsLetter() {
        String baseUrl = "https://infiniteb2b.com/var/www/infiniteb2b/springboot/newsletters/";
        List<NewsLetter> list = newsLetterService.newsLetterRepository.findAll();

        for (NewsLetter newsLetter : list) {
            if (newsLetter.getFilePath() != null && !newsLetter.getFilePath().isEmpty()) {
                newsLetter.setFilePath(baseUrl + newsLetter.getFilePath());
            }
            if (newsLetter.getImagePath() != null && !newsLetter.getImagePath().isEmpty()) {
                newsLetter.setImagePath(baseUrl + newsLetter.getImagePath());
            }
        }

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list, "SUCCESS", true));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String>deleteNewsLetter(@RequestParam long id) {

        newsLetterService.newsLetterRepository.deleteById(id);
        return ResponseEntity.ok().body("DELETED SUCCESSFULLY");}
}
