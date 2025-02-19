package com.contenttree.NewsLetter;


import com.contenttree.PreviewImage.PreviewImage;
import com.contenttree.PreviewImage.PreviewImageRepository;
import com.contenttree.admin.Admin;
import com.contenttree.category.Category;
import com.contenttree.category.CategoryRepository;
import com.contenttree.user.EmailService;
import com.contenttree.user.User;
import com.contenttree.user.UserRepository;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import io.swagger.annotations.Api;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class NewsLetterController {
    @Autowired
    NewsLetterService newsLetterService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;

    @Autowired
    CategoryRepository categoryRepository;
//    @PostMapping("/add-newsletter")
//    public ResponseEntity<ApiResponse1<NewsLetter>> addNewsLetter(
//            @RequestParam String name,
//            @RequestParam String content,
//            @RequestParam(required = false) MultipartFile image,
//            @RequestParam String link,
//            @AuthenticationPrincipal Admin admin) {
//
//        String uploadDir = "/var/www/infiniteb2b/springboot/newsletters/";
//        File directory = new File(uploadDir);
//
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        String imageName = null;
//
//        try {
//
//            if (image != null && !image.isEmpty()) {
//                imageName = image.getOriginalFilename();
//                Path imagePath = Paths.get(uploadDir +"images/" + imageName);
//                Files.createDirectories(imagePath.getParent());
//                Files.write(imagePath, image.getBytes());
//            }
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(ResponseUtils.createResponse1(null, "File or image upload failed: " + e.getMessage(), false));
//        }
//
//        NewsLetter newsLetter = new NewsLetter();
//        newsLetter.setTitle(name);
//        newsLetter.setContent(content);
//        newsLetter.setPreviewLink(link);
//        newsLetter.setImagePath(imageName != null ? "images/" + imageName : "");
//        newsLetter.setAdmin(admin);
//
//        newsLetterService.newsLetterRepository.save(newsLetter);
//
//        try {
//            List<User> subscribedUsers = userRepository.findAll().stream()
//                    .filter(user -> user.getIsNewsLetterSubscriber() == 1)
//                    .toList();
//
//            String logoUrl = "https://infiniteb2b.com/static/media/Infinite-b2b-1-scaled.f42a6998e6eac74721e6.png";
//            String newsletterUrl = "https://infiniteb2b.com/newsletters";
//            String title = newsLetter.getTitle();
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
//            for (User user : subscribedUsers) {
//                emailService.sendHtmlEmail(user.getEmail(),
//                        "New NewsLetter Added" + newsLetter.getTitle(),
//                        htmlMessage);
//            }
//        } catch (MessagingException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(ResponseUtils.createResponse1(newsLetter, "Newsletter saved but email sending failed: " + e.getMessage(), false));
//        }
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(newsLetter, "SUCCESS", true));
//    }
@PostMapping("/add-newsletter")
public ResponseEntity<ApiResponse1<NewsLetter>> addNewsLetter(
        @RequestParam String name,
        @RequestParam String content,
        @RequestParam(required = false) MultipartFile image,
        @RequestParam String link,
        @AuthenticationPrincipal Admin admin) {

    String uploadDir = "/var/www/infiniteb2b/springboot/newsletters/";
//    String uploadDir = "src/main/resources/uploads/newsletters/";
    File directory = new File(uploadDir);

    // Add debug log for directory creation
    log.debug("Checking if upload directory exists: " + uploadDir);
    if (!directory.exists()) {
        log.debug("Directory does not exist, creating directory...");
        directory.mkdirs();
    }

    String imageName = null;

    try {
        if (image != null && !image.isEmpty()) {
            imageName = image.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir + "images/" + imageName);
            Files.createDirectories(imagePath.getParent());

            log.debug("Saving image to: " + imagePath.toString());
            Files.write(imagePath, image.getBytes());
        }
    } catch (IOException e) {
        log.error("File or image upload failed: " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtils.createResponse1(null, "File or image upload failed: " + e.getMessage(), false));
    }

    NewsLetter newsLetter = new NewsLetter();
    newsLetter.setTitle(name);
    newsLetter.setContent(content);
    newsLetter.setPreviewLink(link);
    newsLetter.setImagePath(imageName != null ? "images/" + imageName : "");
    newsLetter.setAdmin(admin);

    // Add debug log before saving to database
    log.debug("Saving newsletter to database: " + newsLetter);
    try {
        newsLetterService.newsLetterRepository.save(newsLetter);
    } catch (Exception e) {
        log.error("Error saving newsletter to database: " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtils.createResponse1(null, "Error saving newsletter: " + e.getMessage(), false));
    }

    try {
        List<User> subscribedUsers = userRepository.findAll().stream()
                .filter(user -> user.getIsNewsLetterSubscriber() == 1)
                .toList();

        log.debug("Found " + subscribedUsers.size() + " subscribed users.");

        String logoUrl = "https://infeedu.com/static/media/Infinite-b2b-1-scaled.f42a6998e6eac74721e6.png";
        String newsletterUrl = "https://infeedu.com/newsletters";
        String title = newsLetter.getTitle();

        String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
                "<div style=\"text-align: center; margin-bottom: 30px;\">" +
                "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
                "</div>" +
                "<h2 style=\"text-align: center; color: #007bff; font-size: 24px;\">New Newsletter Released! " + title + "</h2>" +
                "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
                "We are excited to announce the release of our latest newsletter from <strong>Infeedu</strong>! The new edition, titled <strong>" + title + "</strong>, is now available. Stay updated with the latest trends and insights!" +
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
                "<p>&copy; 2024 Infeedu. All rights reserved.</p>" +
                "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
                "</footer>" +
                "</div>";

        // Send emails to subscribed users
        for (User user : subscribedUsers) {
            log.debug("Sending email to: " + user.getEmail());
            emailService.sendHtmlEmail(user.getEmail(),
                    "New NewsLetter Added: " + newsLetter.getTitle(),
                    htmlMessage);
        }
    } catch (MessagingException e) {
        log.error("Error sending emails: " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtils.createResponse1(newsLetter, "Newsletter saved but email sending failed: " + e.getMessage(), false));
    }

    // Return successful response
    log.debug("Newsletter successfully created and emails sent.");
    return ResponseEntity.ok().body(ResponseUtils.createResponse1(newsLetter, "SUCCESS", true));
}

    @Autowired
PreviewImageRepository previewImageRepository;
    @PostMapping("/upload-preview")
    public ResponseEntity<ApiResponse1<PreviewImage>> uploadNewPreviewImage(@RequestParam MultipartFile previewImage) {
        String uploadDir = "/var/www/infiniteb2b/springboot/new-preview/";
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String imageName = null;

        try {
            if (previewImage != null && !previewImage.isEmpty()) {
                imageName = previewImage.getOriginalFilename();
                Path imagePath = Paths.get(uploadDir + "images/" + imageName);
                Files.createDirectories(imagePath.getParent());
                Files.write(imagePath, previewImage.getBytes());
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtils.createResponse1(null, "File upload failed: " + e.getMessage(), false));
        }
        PreviewImage previewImage1 = new PreviewImage();
        assert previewImage != null;
        previewImage1.setName(previewImage.getName());


        String outputPath = "https://infeedu.com/var/www/infiniteb2b/springboot/new-preview/images/" + imageName;

        previewImage1.setPath(outputPath);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(previewImage1, "File uploaded successfully", true));
    }






    @GetMapping
    public ResponseEntity<ApiResponse1<List<NewsLetter>>> getAllNewsLetter() {
        String baseUrl = "https://infeedu.com/var/www/infiniteb2b/springboot/newsletters/";
        List<NewsLetter> list = newsLetterService.newsLetterRepository.findAll();

        for (NewsLetter newsLetter : list) {
            if (newsLetter.getImagePath() != null && !newsLetter.getImagePath().isEmpty()) {
                newsLetter.setImagePath(baseUrl + newsLetter.getImagePath());
            }
        }

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(list, "SUCCESS", true));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String>deleteNewsLetter(@PathVariable long id) {

        newsLetterService.newsLetterRepository.deleteById(id);
        return ResponseEntity.ok().body("DELETED SUCCESSFULLY");}


//    @PutMapping("/edit-newsletter")
//    public ResponseEntity<ApiResponse1<NewsLetter>> editNewsLetter(
//            @RequestParam Long id,
//            @RequestParam(required = false) String name,
//            @RequestParam (required = false)String content,
//            @RequestParam(required = false) MultipartFile image,
//            @RequestParam(required = false) String link,
//            @AuthenticationPrincipal Admin admin) {
//
//        NewsLetter existingNewsLetter = newsLetterService.newsLetterRepository.findById(id)
//                .orElseThrow(null);
//
//        String uploadDir = "/var/www/infiniteb2b/springboot/newsletters/";
//        File directory = new File(uploadDir);
//
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        String imageName = existingNewsLetter.getImagePath();
//
//        try {
//            if (image != null && !image.isEmpty()) {
//                imageName = image.getOriginalFilename();
//                Path imagePath = Paths.get(uploadDir + "images/" + imageName);
//                Files.createDirectories(imagePath.getParent());
//                Files.write(imagePath, image.getBytes());
//            }
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(ResponseUtils.createResponse1(null, "File or image upload failed: " + e.getMessage(), false));
//        }
//
//        existingNewsLetter.setTitle(name);
//        existingNewsLetter.setContent(content);
//        existingNewsLetter.setPreviewLink(link);
//        existingNewsLetter.setImagePath(imageName != null ? "images/" + imageName : existingNewsLetter.getImagePath());
//        existingNewsLetter.setAdmin(admin);
//
//        newsLetterService.newsLetterRepository.save(existingNewsLetter);
//
//        return ResponseEntity.ok().body(ResponseUtils.createResponse1(existingNewsLetter, "Newsletter updated successfully", true));
//    }
@PutMapping("/edit-newsletter")
public ResponseEntity<ApiResponse1<NewsLetter>> editNewsLetter(
        @RequestParam Long id,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String content,
        @RequestParam(required = false) MultipartFile image,
        @RequestParam(required = false) String link,
        @AuthenticationPrincipal Admin admin) throws Exception {

    NewsLetter existingNewsLetter = newsLetterService.newsLetterRepository.findById(id)
            .orElseThrow(() -> new Exception("Newsletter not found with ID: " + id));

    String uploadDir = "/var/www/infiniteb2b/springboot/newsletters/";
    File directory = new File(uploadDir);

    if (!directory.exists()) {
        directory.mkdirs();
    }

    String imageName = existingNewsLetter.getImagePath();

    try {
        if (image != null && !image.isEmpty()) {
            imageName = image.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir + "images/" + imageName);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, image.getBytes());
        }
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtils.createResponse1(null, "File or image upload failed: " + e.getMessage(), false));
    }

    // Update only the provided fields
    if (name != null && !name.isEmpty()) {
        existingNewsLetter.setTitle(name);
    }

    if (content != null && !content.isEmpty()) {
        existingNewsLetter.setContent(content);
    }

    if (link != null && !link.isEmpty()) {
        existingNewsLetter.setPreviewLink(link);
    }

    if (image != null && !image.isEmpty()) {
        existingNewsLetter.setImagePath("images/" + imageName);
    }

    existingNewsLetter.setAdmin(admin);

    newsLetterService.newsLetterRepository.save(existingNewsLetter);

    return ResponseEntity.ok().body(ResponseUtils.createResponse1(existingNewsLetter, "Newsletter updated successfully", true));
}

    @DeleteMapping("/delete-newsletter/{id}")
    public ResponseEntity<ApiResponse1<String>> deleteNewsletterBYId(@PathVariable long id){

        newsLetterService.newsLetterRepository.deleteById(id);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"Newsletter Deleted",true));
    }


}
