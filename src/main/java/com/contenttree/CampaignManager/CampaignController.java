package com.contenttree.CampaignManager;

import com.contenttree.category.Category;
import com.contenttree.category.CategoryRepository;
import com.contenttree.category.CategoryService;
import com.contenttree.solutionsets.SolutionSets;
import com.contenttree.solutionsets.SolutionSetsService;
import com.contenttree.user.*;
import com.contenttree.utils.ApiResponse1;
import com.contenttree.utils.ResponseUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/campaign")
public class CampaignController {
    @Autowired
    CampaignReposiotry campaignReposiotry;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserService userService;
    @Autowired
    SolutionSetsService solutionSetsService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    EmailService emailService;
    String uploadDir = "/var/www/infiniteb2b/springboot/design";

    @PostMapping("/add")
    public ResponseEntity<ApiResponse1<Campaign>> addCampaign(
            @RequestParam String name,
            @RequestParam Long categoryId,
            @RequestParam String startDate,
            @RequestParam String endDate,@RequestParam MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID().toString() + "_" + Objects.requireNonNull(file.getOriginalFilename()).replace(" ","-");
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath);
        String filePathUpdated = filePath.toString();
        DateTimeFormatter formatter;
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDateTime startDateTime = start.atStartOfDay();

        LocalDate end = LocalDate.parse(endDate, formatter);
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        Category category = categoryRepository.findById(categoryId).orElse(null);
        Campaign campaign = new Campaign();
        campaign.setName(name);
        campaign.setCategory(category);
        campaign.setStartDate(startDateTime);
        campaign.setDesign(filePathUpdated);
        campaign.setEndDate(endDateTime);
        campaign.setStatus(CampaignStatus.Hold);

        campaignReposiotry.save(campaign);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaign, "SUCCESS", true));
    }
    @PostMapping("/template")
    public ResponseEntity<ApiResponse1<Campaign>> addCampaignTemplate(
            @RequestParam String name,
            @RequestParam String jsonFormat,
            @RequestParam String htmlFormat) throws IOException {

        Campaign campaign = new Campaign();
        campaign.setName(name);
        campaign.setJsonFormat(jsonFormat);
        campaign.setHtmlFormat(htmlFormat);
        campaign.setStatus(CampaignStatus.Hold);

        campaignReposiotry.save(campaign);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaign, "SUCCESS", true));
    }
    @GetMapping
    public ResponseEntity<ApiResponse1<List<Campaign>>> getAllCampaign(){
        List<Campaign> campaignList = campaignReposiotry.findAll();
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaignList,"SUCCESS",true
        ));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse1<Campaign>> getCampaign(@PathVariable long id){
        Campaign campaignList = campaignReposiotry.findById(id).orElse(null);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaignList,"SUCCESS",true
        ));
    }
    @PutMapping("/{campaignId}")
    public ResponseEntity<ApiResponse1<Campaign>> editCampaign(
            @PathVariable Long campaignId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) MultipartFile file) throws IOException {

        Campaign campaign = campaignReposiotry.findById(campaignId).orElse(null);

        if (campaign == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseUtils.createResponse1(null, "Campaign not found", false)
            );
        }

        if (name != null && !name.isEmpty()) {
            campaign.setName(name);
        }

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null) {
                campaign.setCategory(category);
            }
        }

        if (startDate != null && !startDate.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDateTime startDateTime = start.atStartOfDay();
            campaign.setStartDate(startDateTime);
        }

        if (endDate != null && !endDate.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate end = LocalDate.parse(endDate, formatter);
            LocalDateTime endDateTime = end.atTime(23, 59, 59);
            campaign.setEndDate(endDateTime);
        }

        if (file != null) {
            String fileName = UUID.randomUUID().toString() + "_" + Objects.requireNonNull(file.getOriginalFilename()).replace(" ", "-");
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath);
            String filePathUpdated = filePath.toString();
            campaign.setDesign(filePathUpdated);
        }

        campaignReposiotry.save(campaign);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaign, "Campaign updated successfully", true));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse1<String>> deleteCampaign(@PathVariable long id){
        campaignReposiotry.deleteById(id);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"DELETED SUCCESSFULLY",true));
    }

    @PutMapping("/active-campaign")
    public ResponseEntity<ApiResponse1<Campaign>> activeCampaign(@RequestParam long id){
        Campaign campaign = campaignReposiotry.findById(id).orElse(null);
        campaign.setStatus(CampaignStatus.Active);
        campaignReposiotry.save(campaign);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaign,"SUCCESS",true));
    }
    @PutMapping("/unactive-campaign")
    public ResponseEntity<ApiResponse1<Campaign>> rejectCampaign(@RequestParam long id){
        Campaign campaign = campaignReposiotry.findById(id).orElse(null);
        campaign.setStatus(CampaignStatus.NotActive);
        campaignReposiotry.save(campaign);
        return ResponseEntity.ok().body(ResponseUtils.createResponse1(campaign,"SUCCESS",true));
    }

    private static final String logoUrl = "https://infinitydemand.com/wp-content/uploads/2024/01/cropped-Logo-22.png";
    private static final String whitepaperUrl="https://infeedu.com";

     @PostMapping("/send-mail-all-user")
    public ResponseEntity<ApiResponse1<String>> sendMailToAllUser(@RequestParam String subject) throws MessagingException {
        List<User> userList = userService.getAllUsers();

         String path="https://infeedu.com";

        String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
                "<div style=\"text-align: center; margin-bottom: 30px;\">" +
                "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
                "</div>" +
                "<h2 style=\"text-align: center; color: #007bff; font-size: 24px;\">Latest Whitepapers Released!</h2>" +
                "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
                "We are excited to share the latest updates from <strong>Infeedu</strong>! Our new whitepapers on <strong> </strong> are now available. Explore the latest insights and trends to stay ahead in your field." +
                "</p>" +
                "<div style=\"text-align: center; margin: 30px 0;\">" +
                "<a href=\"" + whitepaperUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">Explore Now</a>" +
                "</div>" +
                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
                "If the button above doesn’t work, copy and paste the following URL into your browser:" +
                "</p>" +
                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
                "<a href=\"" + whitepaperUrl + "\" style=\"color: #007bff; word-wrap: break-word;\">" + whitepaperUrl + "</a>" +
                "</p>" +
                "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
                "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
                "<p>&copy; 2024 Infeedu. All rights reserved.</p>" +
                "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
                "</footer>" +
                "</div>";
        for (User user : userList){
            emailService.sendHtmlEmailWithAttachment(user.getEmail(), subject,htmlMessage,path);
        }

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"SUCCESS",true));

    }

    @Autowired
    UserRepository userRepository;
    @PostMapping("/send-mail-subscriber")
    public ResponseEntity<ApiResponse1<String>> sendMailToAllSubscriber(@RequestParam long solutionSetId,
                                                                  @RequestParam String subject) throws MessagingException {
        List<User> subscribedUsers = userRepository.findAll().stream()
                .filter(user -> user.isEnabled() && user.getIsSubscriber() == 1)
                .toList();
        SolutionSets solutionSets = solutionSetsService.getSolutionSetById(solutionSetId);
        Optional<Category> category = categoryService.getCategoryBySolutionSet(solutionSetId);
        String path = solutionSets.getFilePath();
        String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
                "<div style=\"text-align: center; margin-bottom: 30px;\">" +
                "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
                "</div>" +
                "<h2 style=\"text-align: center; color: #007bff; font-size: 24px;\">Latest Whitepapers Released!</h2>" +
                "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
                "We are excited to share the latest updates from <strong>Infeedu</strong>! Our new whitepapers on <strong>" + category.get().getName() + "</strong> are now available. Explore the latest insights and trends to stay ahead in your field." +
                "</p>" +
                "<div style=\"text-align: center; margin: 30px 0;\">" +
                "<a href=\"" + whitepaperUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">Explore Now</a>" +
                "</div>" +
                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
                "If the button above doesn’t work, copy and paste the following URL into your browser:" +
                "</p>" +
                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
                "<a href=\"" + whitepaperUrl + "\" style=\"color: #007bff; word-wrap: break-word;\">" + whitepaperUrl + "</a>" +
                "</p>" +
                "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
                "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
                "<p>&copy; 2024 Infeedu. All rights reserved.</p>" +
                "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
                "</footer>" +
                "</div>";


        for (User user : subscribedUsers){
            emailService.sendHtmlEmailWithAttachment(user.getEmail(), subject,htmlMessage,path);
        }

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"SUCCESS",true));

    }
    @PostMapping("/send-mail-all-unsubscriber")
    public ResponseEntity<ApiResponse1<String>> sendMailToAllUnSubsriver(@RequestParam long solutionSetId,
                                                                  @RequestParam String subject) throws MessagingException {
        List<User> subscribedUsers = userRepository.findAll().stream()
                .filter(user -> user.isEnabled() && user.getIsSubscriber() == 1)
                .toList();
        SolutionSets solutionSets = solutionSetsService.getSolutionSetById(solutionSetId);
        String path = solutionSets.getFilePath();
        Optional<Category> category = categoryService.getCategoryBySolutionSet(solutionSetId);

        String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
                "<div style=\"text-align: center; margin-bottom: 30px;\">" +
                "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
                "</div>" +
                "<h2 style=\"text-align: center; color: #007bff; font-size: 24px;\">Latest Whitepapers Released!</h2>" +
                "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
                "We are excited to share the latest updates from <strong>Infeedu</strong>! Our new whitepapers on <strong>" + category.get().getName()+ "</strong> are now available. Explore the latest insights and trends to stay ahead in your field." +
                "</p>" +
                "<div style=\"text-align: center; margin: 30px 0;\">" +
                "<a href=\"" + whitepaperUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">Explore Now</a>" +
                "</div>" +
                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
                "If the button above doesn’t work, copy and paste the following URL into your browser:" +
                "</p>" +
                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
                "<a href=\"" + whitepaperUrl + "\" style=\"color: #007bff; word-wrap: break-word;\">" + whitepaperUrl + "</a>" +
                "</p>" +
                "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
                "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
                "<p>&copy; 2024 Infeedu. All rights reserved.</p>" +
                "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
                "</footer>" +
                "</div>";
        for (User user : subscribedUsers){
            emailService.sendHtmlEmailWithAttachment(user.getEmail(), subject,htmlMessage,path);
        }

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"SUCCESS",true));

    }
    @PostMapping("/send-mail-category")
    public ResponseEntity<ApiResponse1<String>> sendMailToCategorySubscribers(@RequestParam long categoryId,
                                                                         @RequestParam String subject) throws MessagingException {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        List<User> subscribedUsers = userRepository.findAll().stream()
                .filter(user -> user.isEnabled() && user.getIsSubscriber() == 1&&user.getFavorites().contains(category.getName()))
                .toList();
        String path = "https://infeedu.com";

        String htmlMessage = "<div style=\"font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #f9f9f9;\">" +
                "<div style=\"text-align: center; margin-bottom: 30px;\">" +
                "<img src=\"" + logoUrl + "\" alt=\"Company Logo\" style=\"max-width: 150px;\">" +
                "</div>" +
                "<h2 style=\"text-align: center; color: #007bff; font-size: 24px;\">Latest Whitepapers Released!</h2>" +
                "<p style=\"font-size: 16px; line-height: 1.5; text-align: center;\">" +
                "We are excited to share the latest updates from <strong>Infeedu</strong>! Our new whitepapers on <strong>" + category.getName()+ "</strong> are now available. Explore the latest insights and trends to stay ahead in your field." +
                "</p>" +
                "<div style=\"text-align: center; margin: 30px 0;\">" +
                "<a href=\"" + whitepaperUrl + "\" style=\"font-size: 18px; padding: 12px 24px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;\">Explore Now</a>" +
                "</div>" +
                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
                "If the button above doesn’t work, copy and paste the following URL into your browser:" +
                "</p>" +
                "<p style=\"font-size: 14px; line-height: 1.5; text-align: center;\">" +
                "<a href=\"" + whitepaperUrl + "\" style=\"color: #007bff; word-wrap: break-word;\">" + whitepaperUrl + "</a>" +
                "</p>" +
                "<hr style=\"border: 0; border-top: 1px solid #e0e0e0; margin: 40px 0;\">" +
                "<footer style=\"text-align: center; font-size: 12px; color: #999;\">" +
                "<p>&copy; 2024 Infeedu. All rights reserved.</p>" +
                "<p><a href=\"#\" style=\"color: #999; text-decoration: none;\">Unsubscribe</a> | <a href=\"#\" style=\"color: #999; text-decoration: none;\">Contact Us</a></p>" +
                "</footer>" +
                "</div>";
        for (User user : subscribedUsers){
            emailService.sendHtmlEmailWithAttachment(user.getEmail(), subject,htmlMessage,path);
        }

        return ResponseEntity.ok().body(ResponseUtils.createResponse1(null,"SUCCESS",true));

    }

}
